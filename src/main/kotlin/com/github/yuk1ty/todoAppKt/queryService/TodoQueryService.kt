package com.github.yuk1ty.todoAppKt.queryService

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.binding
import com.github.yuk1ty.todoAppKt.domain.error.AppErrors
import com.github.yuk1ty.todoAppKt.domain.model.ValidatedTodo
import com.github.yuk1ty.todoAppKt.shared.modules.DatabaseConn
import com.github.yuk1ty.todoAppKt.shared.modules.Permission

class TodoQueryService(conn: DatabaseConn<Permission.ReadOnly>) {
    fun getTodos(): Result<List<ValidatedTodo>, AppErrors> = binding {
        // TODO: ValidatedTodo shouldn't be used here.
        // TODO: This is a tentative solution to make the code work.
        val allTodos = ValidatedTodo(
            title = "title",
            description = "description",
            due = java.time.LocalDateTime.now(),
            status = "Ready"
        ).bind()
        listOf(allTodos)
    }
}
