package com.github.yuk1ty.todoAppKt.shared.modules

import com.github.yuk1ty.todoAppKt.adapter.database.DatabaseConn
import com.github.yuk1ty.todoAppKt.adapter.database.Permission
import com.github.yuk1ty.todoAppKt.shared.config.AppConfig
import org.jetbrains.exposed.sql.Database

internal fun establishDatabaseConnection(cfg: AppConfig): Pair<DatabaseConn<Permission.ReadOnly>, DatabaseConn<Permission.Writable>> {
    val read = cfg.readableDatabase.run {
        DatabaseConn.establishRead(Database.connect(jdbcUrl, driver = driverName, user = user, password = password))
    }
    val write = cfg.writableDatabase.run {
        DatabaseConn.establishWrite(Database.connect(jdbcUrl, driver = driverName, user = user, password = password))
    }
    return Pair(read, write)
}