package com.github.yuk1ty.todoAppKt.queryService

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.binding
import com.github.michaelbull.result.combine
import com.github.michaelbull.result.coroutines.coroutineBinding
import com.github.yuk1ty.todoAppKt.adapter.database.DatabaseConn
import com.github.yuk1ty.todoAppKt.adapter.database.Permission
import com.github.yuk1ty.todoAppKt.adapter.database.TodoTable
import com.github.yuk1ty.todoAppKt.adapter.database.beginReadTransaction
import com.github.yuk1ty.todoAppKt.adapter.models.TodoRow
import com.github.yuk1ty.todoAppKt.domain.model.ValidatedTodoDTO
import com.github.yuk1ty.todoAppKt.shared.AppErrors
import org.jetbrains.exposed.sql.selectAll

class TodoQueryService(private val conn: DatabaseConn<Permission.ReadOnly>) {
    suspend fun getTodos(): Result<List<ValidatedTodoDTO>, AppErrors> = coroutineBinding {
        val allTodosFromDatabase = conn.beginReadTransaction {
            TodoTable.selectAll().map { TodoRow.fromResultRow(it) }.toList()
        }.bind()
        val unvalidatedTodos = allTodosFromDatabase.map { it.intoDomain() }
        // TODO: handle all errors
        val validatedTodos = unvalidatedTodos.map { ValidatedTodoDTO(it) }.combine().bind()

        validatedTodos
    }
}
