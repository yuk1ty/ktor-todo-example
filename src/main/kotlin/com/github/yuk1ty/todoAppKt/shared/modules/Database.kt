package com.github.yuk1ty.todoAppKt.shared.modules

import com.github.yuk1ty.todoAppKt.adapter.database.DatabaseConn
import com.github.yuk1ty.todoAppKt.adapter.database.Permission
import com.github.yuk1ty.todoAppKt.shared.config.DatabaseConfig
import org.jetbrains.exposed.sql.Database

internal fun establishDatabaseConnection(readConfig: DatabaseConfig, writeConfig: DatabaseConfig): Pair<DatabaseConn<Permission.ReadOnly>, DatabaseConn<Permission.Writable>> {
    val read = readConfig.run {
        DatabaseConn.establishRead(Database.connect(jdbcUrl, driver = driverName, user = user, password = password))
    }
    val write = writeConfig.run {
        DatabaseConn.establishWrite(Database.connect(jdbcUrl, driver = driverName, user = user, password = password))
    }
    return Pair(read, write)
}