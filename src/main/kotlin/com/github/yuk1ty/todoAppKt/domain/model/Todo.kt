package com.github.yuk1ty.todoAppKt.domain.model

import com.github.michaelbull.result.*
import com.github.yuk1ty.todoAppKt.domain.error.DomainErrors
import com.github.yuk1ty.todoAppKt.domain.model.common.Strings.String1024
import com.github.yuk1ty.todoAppKt.domain.model.common.Strings.String2048
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.UUID

data class UnvalidatedTodoDTO(
    val id: UUID,
    val title: String,
    val description: String?,
    val due: LocalDateTime,
    val status: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

data class ValidatedTodoDTO private constructor(
    val id: TodoId,
    val title: String1024,
    val description: String2048?,
    val due: TodoDue,
    val status: TodoStatus,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
) {
    companion object {
        // TODO: ideally here should represent the type transition as like Unvalidated -> Validated
        // TODO: Maybe I need to make a new type like UnvalidatedTodoDTO
        operator fun invoke(
            from: UnvalidatedTodoDTO
        ): Result<ValidatedTodoDTO, DomainErrors.ValidationErrors> =
            from.run {
                zipOrAccumulate(
                    { String1024(title) },
                    { description?.let { String2048(it) } ?: Ok(null) },
                    { TodoStatus.fromString(status) },
                ) { validatedTitle, validatedDescription, validatedStatus ->
                    ValidatedTodoDTO(
                        id = TodoId(id),
                        title = validatedTitle,
                        description = validatedDescription,
                        due = TodoDue(due.atOffset(ZoneOffset.UTC)),
                        status = validatedStatus,
                        createdAt = createdAt.atOffset(ZoneOffset.UTC),
                        updatedAt = updatedAt.atOffset(ZoneOffset.UTC)
                    )
                }.mapError { DomainErrors.ValidationErrors(it) }
            }
    }
}

data class ValidatedTodo private constructor(
    val id: TodoId,
    val title: String1024,
    val description: String2048?,
    val due: TodoDue,
    val status: TodoStatus
) {
    companion object {
        operator fun invoke(
            title: String,
            description: String,
            due: LocalDateTime,
            status: String
        ): Result<ValidatedTodo, DomainErrors.ValidationErrors> =
            zipOrAccumulate(
                { String1024(title) },
                { String2048(description) },
                { TodoStatus.fromString(status) },
            ) { validatedTitle, validatedDescription, validatedStatus ->
                ValidatedTodo(
                    id = TodoId(UUID.randomUUID()),
                    title = validatedTitle,
                    description = validatedDescription,
                    due = TodoDue(due.atOffset(ZoneOffset.UTC)),
                    status = validatedStatus
                )
            }.mapError { DomainErrors.ValidationErrors(it) }
    }
}

@JvmInline
value class TodoId(val value: UUID)

@JvmInline
value class TodoDue(val value: OffsetDateTime)

enum class TodoStatus {
    Ready,
    InProgress,
    Done,
    Suspended,
    Archived;

    companion object {
        fun fromString(value: String): Result<TodoStatus, DomainErrors.ValidationError> {
            return when (value) {
                "Ready" -> Ok(Ready)
                "InProgress" -> Ok(InProgress)
                "Done" -> Ok(Done)
                "Suspended" -> Ok(Suspended)
                "Archived" -> Ok(Archived)
                else -> Err(DomainErrors.ValidationError("Invalid TodoStatus"))
            }
        }
    }
}
