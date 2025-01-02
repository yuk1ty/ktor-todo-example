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

data class UnvalidatedTodo private constructor(
    val title: String,
    val description: String?,
    val due: LocalDateTime,
    val status: String
) {
    companion object {
        operator fun invoke(
            title: String,
            description: String?,
            due: LocalDateTime,
            status: String
        ): UnvalidatedTodo =
            UnvalidatedTodo(
                title = title,
                description = description,
                due = due,
                status = status
            )
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
            unvalidatedTodo: UnvalidatedTodo
        ): Result<ValidatedTodo, DomainErrors.ValidationErrors> = unvalidatedTodo.run {
            zipOrAccumulate(
                { String1024(title) },
                { description?.let { String2048(it) } ?: Ok(null) },
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

    fun asString(): String = when (this) {
        Ready -> "Ready"
        InProgress -> "InProgress"
        Done -> "Done"
        Suspended -> "Suspended"
        Archived -> "Archived"
    }
}
