package com.github.yuk1ty.todoAppKt.shared.modules

import com.github.yuk1ty.todoAppKt.adapter.database.DatabaseConn
import com.github.yuk1ty.todoAppKt.adapter.database.Permission
import io.ktor.server.application.*
import io.ktor.server.config.*
import org.jetbrains.exposed.sql.Database

internal fun Application.establishDatabaseConnection(cfg: ApplicationConfig): Pair<DatabaseConn<Permission.ReadOnly>, DatabaseConn<Permission.Writable>> {
    val read = cfg.run {
        val jdbcUrl = property("db.read.jdbcURL").getString()
        val user = property("db.read.user").getString()
        val password = property("db.read.password").getString()
        val driverName = property("db.read.driverClassName").getString()
        DatabaseConn.establishRead(Database.connect(jdbcUrl, driver = driverName, user = user, password = password))
    }
    val write = cfg.run {
        val jdbcUrl = property("db.write.jdbcURL").getString()
        val user = property("db.write.user").getString()
        val password = property("db.write.password").getString()
        val driverName = property("db.write.driverClassName").getString()
        DatabaseConn.establishWrite(Database.connect(jdbcUrl, driver = driverName, user = user, password = password))
    }
    return Pair(read, write)
}