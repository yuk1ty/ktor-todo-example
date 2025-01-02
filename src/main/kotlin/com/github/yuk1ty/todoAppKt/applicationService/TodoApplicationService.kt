package com.github.yuk1ty.todoAppKt.applicationService

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.coroutines.coroutineBinding
import com.github.yuk1ty.todoAppKt.domain.model.UnvalidatedTodo
import com.github.yuk1ty.todoAppKt.domain.model.ValidatedTodo
import com.github.yuk1ty.todoAppKt.domain.repository.TodoRepository
import com.github.yuk1ty.todoAppKt.shared.AppErrors

class TodoApplicationService(private val repository: TodoRepository) {
    suspend fun createTodo(unvalidatedTodo: UnvalidatedTodo): Result<Unit, AppErrors> = coroutineBinding {
        val validatedTodo = ValidatedTodo(unvalidatedTodo).bind()
        repository.create(validatedTodo)
    }
}