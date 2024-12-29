package com.github.yuk1ty.todoAppKt.surface.queryService

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.binding
import com.github.yuk1ty.todoAppKt.domain.error.DomainErrors
import com.github.yuk1ty.todoAppKt.domain.model.ValidatedTodo

class TodoQueryService {
    fun getTodos(): Result<List<ValidatedTodo>, DomainErrors> = binding {
        val allTodos = ValidatedTodo(
            title = "title",
            description = "description",
            due = java.time.LocalDateTime.now(),
            status = "Ready"
        ).bind()
        listOf(allTodos)
    }
}