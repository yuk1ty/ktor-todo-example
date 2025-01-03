package com.github.yuk1ty.todoAppKt.application.command

import com.github.yuk1ty.todoAppKt.domain.model.UnvalidatedTodo
import java.time.LocalDateTime
import java.util.UUID

object TodoCommands {
    data class Create(val title: String, val description: String?, val due: LocalDateTime?) {
        fun toUnvalidatedDomain(): UnvalidatedTodo {
            return UnvalidatedTodo(
                title = title,
                description = description,
                due = due,
                status = "Ready",
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        }
    }

    data class Update(val id: UUID, val title: String?, val description: String?, val due: LocalDateTime?, val status: String?)
}