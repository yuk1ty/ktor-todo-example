package com.github.yuk1ty.todoAppKt.queryService

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.binding
import com.github.michaelbull.result.combine
import com.github.yuk1ty.todoAppKt.adapter.database.DatabaseConn
import com.github.yuk1ty.todoAppKt.adapter.database.Permission
import com.github.yuk1ty.todoAppKt.adapter.database.TodoTable
import com.github.yuk1ty.todoAppKt.adapter.database.beginRead
import com.github.yuk1ty.todoAppKt.adapter.models.TodoRow
import com.github.yuk1ty.todoAppKt.domain.error.AppErrors
import com.github.yuk1ty.todoAppKt.domain.model.ValidatedTodoDTO
import org.jetbrains.exposed.sql.selectAll

class TodoQueryService(private val conn: DatabaseConn<Permission.ReadOnly>) {
    fun getTodos(): Result<List<ValidatedTodoDTO>, AppErrors> = binding {
        val allTodosFromDatabase = conn.beginRead {
            TodoTable.selectAll().map { row ->
                TodoRow(
                    id = row[TodoTable.id],
                    title = row[TodoTable.title],
                    description = row[TodoTable.description],
                    due = row[TodoTable.due],
                    status = row[TodoTable.status],
                    createdAt = row[TodoTable.createdAt],
                    updatedAt = row[TodoTable.updatedAt]
                )
            }.toList()
        }
        val validatedTodos = allTodosFromDatabase.map {
            it.run {
                ValidatedTodoDTO(id, title, description, due, status, createdAt, updatedAt)
            }
        }.combine().bind()

        validatedTodos
    }
}
