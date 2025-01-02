package com.github.yuk1ty.todoAppKt.adapter.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object TodoTable : Table() {
    val id = uuid("todo_id")
    val title = varchar("title", 1024)
    val description = varchar("description", 2048).nullable()
    val due = datetime("due").nullable()
    val status = varchar("status", 32)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")

    override val primaryKey: PrimaryKey = PrimaryKey(id, name = "pk_todo_id")
}