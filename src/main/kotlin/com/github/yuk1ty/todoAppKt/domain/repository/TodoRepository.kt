package com.github.yuk1ty.todoAppKt.domain.repository

import com.github.michaelbull.result.Result
import com.github.yuk1ty.todoAppKt.adapter.error.AdapterErrors
import com.github.yuk1ty.todoAppKt.domain.model.ValidatedTodo

interface TodoRepository {
    suspend fun create(validatedTodo: ValidatedTodo): Result<Unit, AdapterErrors>
}