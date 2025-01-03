package com.github.yuk1ty.todoAppKt.application.repository

import com.github.michaelbull.result.Result
import com.github.yuk1ty.todoAppKt.domain.model.TodoId
import com.github.yuk1ty.todoAppKt.domain.model.ValidatedTodo
import com.github.yuk1ty.todoAppKt.shared.AppErrors

interface TodoRepository {
    suspend fun getById(id: TodoId): Result<ValidatedTodo?, AppErrors>
    suspend fun create(validatedTodo: ValidatedTodo): Result<Unit, AppErrors>
    suspend fun update(validatedTodo: ValidatedTodo): Result<Unit, AppErrors>
    suspend fun delete(id: TodoId): Result<Unit, AppErrors>
}