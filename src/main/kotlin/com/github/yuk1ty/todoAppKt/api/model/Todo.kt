package com.github.yuk1ty.todoAppKt.api.model

import com.github.yuk1ty.todoAppKt.domain.model.ValidatedTodo
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.UUID

@Serializable
internal data class TodoResponse(
    @Contextual val id: UUID,
    val title: String,
    val description: String,
    @Contextual val due: LocalDateTime,
    val status: String,
) {
    companion object {
        fun fromTodo(todo: ValidatedTodo): TodoResponse {
            return TodoResponse(
                id = todo.id.value,
                title = todo.title.value,
                description = todo.description.value,
                due = todo.due.value.toLocalDateTime(),
                status = todo.status.name,
            )
        }
    }
}

internal data class CreateTodoRequest(
    val title: String,
    val description: String,
    val due: LocalDateTime,
)
