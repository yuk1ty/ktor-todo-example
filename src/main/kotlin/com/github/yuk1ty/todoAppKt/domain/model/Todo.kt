package com.github.yuk1ty.todoAppKt.domain.model

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.binding
import com.github.yuk1ty.todoAppKt.domain.error.DomainErrors
import com.github.yuk1ty.todoAppKt.domain.model.common.Strings.String1024
import com.github.yuk1ty.todoAppKt.domain.model.common.Strings.String2048
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.UUID

data class ValidatedTodo private constructor(
    val id: TodoId,
    val title: String1024,
    val description: String2048,
    val due: TodoDue,
    val status: TodoStatus
) {
    companion object {
        operator fun invoke(
            title: String,
            description: String,
            due: LocalDateTime,
            status: String
        ): Result<ValidatedTodo, DomainErrors> =
            binding {
                val id = UUID.randomUUID().let(::TodoId)
                val title = String1024(title).bind()
                val description = String2048(description).bind()
                val due = due.atOffset(ZoneOffset.UTC).let(::TodoDue)
                val status = TodoStatus.fromString(status).bind()
                ValidatedTodo(id, title, description, due, status)
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
        fun fromString(value: String): Result<TodoStatus, DomainErrors> {
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
