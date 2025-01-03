package com.github.yuk1ty.todoAppKt.adapter.repository

import com.github.michaelbull.result.*
import com.github.michaelbull.result.runCatching
import com.github.michaelbull.result.coroutines.coroutineBinding
import com.github.yuk1ty.todoAppKt.adapter.database.TodoTable
import com.github.yuk1ty.todoAppKt.adapter.error.AdapterErrors
import com.github.yuk1ty.todoAppKt.adapter.models.TodoRow
import com.github.yuk1ty.todoAppKt.application.repository.TodoRepository
import com.github.yuk1ty.todoAppKt.domain.model.TodoId
import com.github.yuk1ty.todoAppKt.domain.model.ValidatedTodo
import com.github.yuk1ty.todoAppKt.shared.AppErrors
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import java.time.LocalDateTime

object TodoRepositoryImpl : TodoRepository {
    override suspend fun getById(id: TodoId): Result<ValidatedTodo?, AppErrors> = coroutineBinding {
        val row = runCatching {
            TodoTable.selectAll().where { TodoTable.id.eq(id.value) }.singleOrNull()
        }.map { it?.let { TodoRow.fromResultRow(it) } }.mapError { AdapterErrors.DatabaseError(it) }.bind()
        val validatedTodo = row?.intoDomain()?.let { ValidatedTodo(it) }?.bind()

        validatedTodo
    }

    override suspend fun create(validatedTodo: ValidatedTodo): Result<Unit, AppErrors> =
        coroutineBinding {
            val row = validatedTodo.run {
                TodoRow(
                    id = id.value,
                    title = title.value,
                    description = description?.value,
                    due = due?.value?.toLocalDateTime(),
                    status = status.asString(),
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now(),
                )
            }
            runCatching {
                TodoTable.insert {
                    it[id] = row.id
                    it[title] = row.title
                    it[description] = row.description
                    it[due] = row.due
                    it[status] = row.status
                    it[createdAt] = row.createdAt
                    it[updatedAt] = row.updatedAt
                }
            }
                .mapError { AdapterErrors.DatabaseError(it) }
                .toErrorIf(
                    { it.insertedCount != 1 },
                    { AdapterErrors.InvalidAffectionResult("Affected row count was invalid: $it") }
                ).bind()

            Ok(Unit)
        }

    override suspend fun update(validatedTodo: ValidatedTodo): Result<Unit, AppErrors> = coroutineBinding {
        val row = validatedTodo.run {
            TodoRow(
                id = id.value,
                title = title.value,
                description = description?.value,
                due = due?.value?.toLocalDateTime(),
                status = status.asString(),
                createdAt = createdAt.toLocalDateTime(),
                updatedAt = LocalDateTime.now(),
            )
        }
        runCatching {
            TodoTable.update({ TodoTable.id.eq(row.id) }) {
                it[title] = row.title
                it[description] = row.description
                it[due] = row.due
                it[status] = row.status
                it[createdAt] = row.createdAt
                it[updatedAt] = row.updatedAt
            }
        }
            .mapError { AdapterErrors.DatabaseError(it) }
            // TODO: This seems not to work?
            .toErrorIf(
                { it != 1 },
                { AdapterErrors.InvalidAffectionResult("Affected row count was invalid: $it") }
            ).bind()

        Ok(Unit)
    }

    override suspend fun delete(id: TodoId): Result<Unit, AppErrors> = coroutineBinding {
        runCatching {
            TodoTable.deleteWhere { TodoTable.id.eq(id.value) }
        }
            .mapError { AdapterErrors.DatabaseError(it) }
            .toErrorIf(
                { it != 1 },
                { AdapterErrors.InvalidAffectionResult("Affected row count was invalid: $it") }
            ).bind()

        Ok(Unit)
    }
}