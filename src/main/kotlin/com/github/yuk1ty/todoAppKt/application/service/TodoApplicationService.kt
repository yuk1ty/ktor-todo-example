package com.github.yuk1ty.todoAppKt.application.service

import com.github.michaelbull.result.*
import com.github.michaelbull.result.coroutines.coroutineBinding
import com.github.yuk1ty.todoAppKt.adapter.database.DatabaseConn
import com.github.yuk1ty.todoAppKt.adapter.database.Permission
import com.github.yuk1ty.todoAppKt.adapter.database.tryBeginWriteTransaction
import com.github.yuk1ty.todoAppKt.application.command.TodoCommands
import com.github.yuk1ty.todoAppKt.application.error.ApplicationServiceErrors
import com.github.yuk1ty.todoAppKt.domain.repository.TodoRepository
import com.github.yuk1ty.todoAppKt.domain.model.TodoId
import com.github.yuk1ty.todoAppKt.domain.model.UnvalidatedTodo
import com.github.yuk1ty.todoAppKt.domain.model.ValidatedTodo
import com.github.yuk1ty.todoAppKt.shared.AppErrors
import java.time.LocalDateTime

class TodoApplicationService(
    private val conn: DatabaseConn<Permission.Writable>,
    private val repository: TodoRepository
) {
    suspend fun createTodo(command: TodoCommands.Create): Result<Unit, AppErrors> = coroutineBinding {
        val validatedTodo = ValidatedTodo(command.toUnvalidatedDomain()).bind()

        conn.tryBeginWriteTransaction {
            repository.create(validatedTodo)
        }
    }

    suspend fun updateTodo(command: TodoCommands.Update): Result<Unit, AppErrors> =
        conn.tryBeginWriteTransaction {
            binding {
                val existingTodo =
                    repository.getById(TodoId(command.id))
                        .toErrorIfNull { ApplicationServiceErrors.EntityNotFound(command.id) }
                        .bind()
                // TODO: Make update function to ValidatedTodo?
                val toBeUpdated = UnvalidatedTodo(
                    id = existingTodo.id.value,
                    title = command.title ?: existingTodo.title.value,
                    description = command.description ?: existingTodo.description?.value,
                    due = command.due ?: existingTodo.due?.value?.toLocalDateTime(),
                    status = command.status ?: existingTodo.status.asString(),
                    createdAt = existingTodo.createdAt.toLocalDateTime(),
                    updatedAt = LocalDateTime.now()
                ).let { ValidatedTodo(it) }.bind()

                repository.update(toBeUpdated)
            }
        }

    suspend fun deleteTodo(command: TodoCommands.Delete): Result<Unit, AppErrors> =
        conn.tryBeginWriteTransaction {
            repository.delete(TodoId(command.id))
        }
}