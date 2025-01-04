package com.github.yuk1ty.todoAppKt.application.repository

import com.github.michaelbull.result.Result
import com.github.yuk1ty.todoAppKt.domain.model.TodoId
import com.github.yuk1ty.todoAppKt.domain.model.ValidatedTodo
import com.github.yuk1ty.todoAppKt.shared.AppErrors

interface TodoRepository {
    fun getById(id: TodoId): Result<ValidatedTodo?, AppErrors>
    fun create(validatedTodo: ValidatedTodo): Result<Unit, AppErrors>
    fun update(validatedTodo: ValidatedTodo): Result<Unit, AppErrors>
    fun delete(id: TodoId): Result<Unit, AppErrors>
}