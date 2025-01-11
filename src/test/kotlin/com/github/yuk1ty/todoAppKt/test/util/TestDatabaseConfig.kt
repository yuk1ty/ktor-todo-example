package com.github.yuk1ty.todoAppKt.test.util

import com.github.yuk1ty.todoAppKt.adapter.database.DatabaseConn
import com.github.yuk1ty.todoAppKt.adapter.database.Permission
import com.github.yuk1ty.todoAppKt.shared.config.DatabaseConfig
import com.github.yuk1ty.todoAppKt.shared.modules.establishDatabaseConnection

object TestDatabaseConfig {
    private val readonly: DatabaseConfig = DatabaseConfig(
        driverName = "org.postgresql.Driver",
        jdbcUrl = "jdbc:postgresql://localhost:5432/todo_app_kt_test",
        user = "readonly",
        password = "password"
    )
    private val writable: DatabaseConfig = DatabaseConfig(
        driverName = "org.postgresql.Driver",
        jdbcUrl = "jdbc:postgresql://localhost:5432/todo_app_kt_test",
        user = "writable",
        password = "password"
    )

    fun establishTestConnection(): Pair<DatabaseConn<Permission.ReadOnly>, DatabaseConn<Permission.Writable>> =
        establishDatabaseConnection(readonly, writable)
}