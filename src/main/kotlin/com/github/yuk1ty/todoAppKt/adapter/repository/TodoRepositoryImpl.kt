package com.github.yuk1ty.todoAppKt.adapter.repository

import com.github.michaelbull.result.*
import com.github.michaelbull.result.coroutines.coroutineBinding
import com.github.yuk1ty.todoAppKt.adapter.database.DatabaseConn
import com.github.yuk1ty.todoAppKt.adapter.database.Permission
import com.github.yuk1ty.todoAppKt.adapter.database.TodoTable
import com.github.yuk1ty.todoAppKt.adapter.database.beginWriteTransaction
import com.github.yuk1ty.todoAppKt.adapter.error.AdapterErrors
import com.github.yuk1ty.todoAppKt.adapter.models.TodoRow
import com.github.yuk1ty.todoAppKt.domain.model.ValidatedTodo
import com.github.yuk1ty.todoAppKt.domain.repository.TodoRepository
import com.github.yuk1ty.todoAppKt.shared.utilities.errorIdentity
import org.jetbrains.exposed.sql.insert
import java.time.LocalDateTime

class TodoRepositoryImpl(private val conn: DatabaseConn<Permission.Writable>) : TodoRepository {
    override suspend fun create(validatedTodo: ValidatedTodo): Result<Unit, AdapterErrors> = coroutineBinding {
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

        conn.beginWriteTransaction {
            return@beginWriteTransaction TodoTable.insert {
                it[id] = row.id
                it[title] = row.title
                it[description] = row.description
                it[due] = row.due
                it[status] = row.status
                it[createdAt] = row.createdAt
                it[updatedAt] = row.updatedAt
            }
        }.flatMapBoth(
            { rowsAffected ->
                if (rowsAffected.insertedCount == 1) Ok(Unit) else Err(
                    AdapterErrors.InvalidInsertionResult(
                        "Affected row count was invalid: ${rowsAffected.insertedCount}"
                    )
                )
            },
            { errorIdentity(it) }
        )
    }
}