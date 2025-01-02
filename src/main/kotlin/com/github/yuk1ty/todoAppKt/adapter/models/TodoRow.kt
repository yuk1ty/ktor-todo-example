package com.github.yuk1ty.todoAppKt.adapter.models

import com.github.yuk1ty.todoAppKt.adapter.database.TodoTable
import com.github.yuk1ty.todoAppKt.domain.model.UnvalidatedTodoDTO
import org.jetbrains.exposed.sql.ResultRow
import java.time.LocalDateTime
import java.util.UUID

data class TodoRow(
    val id: UUID,
    val title: String,
    val description: String?,
    val due: LocalDateTime?,
    val status: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun fromResultRow(row: ResultRow): TodoRow = TodoRow(
            id = row[TodoTable.id],
            title = row[TodoTable.title],
            description = row[TodoTable.description],
            due = row[TodoTable.due],
            status = row[TodoTable.status],
            createdAt = row[TodoTable.createdAt],
            updatedAt = row[TodoTable.updatedAt]
        )
    }

    fun intoDomain(): UnvalidatedTodoDTO = UnvalidatedTodoDTO(
        id = id,
        title = title,
        description = description,
        due = due,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}