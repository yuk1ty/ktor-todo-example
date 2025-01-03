package com.github.yuk1ty.todoAppKt.application.service

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.coroutines.coroutineBinding
import com.github.yuk1ty.todoAppKt.adapter.database.DatabaseConn
import com.github.yuk1ty.todoAppKt.adapter.database.Permission
import com.github.yuk1ty.todoAppKt.adapter.database.tryBeginWriteTransaction
import com.github.yuk1ty.todoAppKt.application.command.TodoCommands
import com.github.yuk1ty.todoAppKt.application.repository.TodoRepository
import com.github.yuk1ty.todoAppKt.domain.model.ValidatedTodo
import com.github.yuk1ty.todoAppKt.shared.AppErrors

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
}