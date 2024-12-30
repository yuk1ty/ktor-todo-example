package com.github.yuk1ty.todoAppKt.shared.modules

import io.ktor.server.application.*
import io.ktor.server.config.*
import org.jetbrains.exposed.sql.Database

sealed interface Permission {
    data object ReadOnly : Permission
    data object Writable : Permission
}

@JvmInline
value class DatabaseConn<K : Permission>(val inner: Database) {
    companion object {
        fun establishRead(conn: Database): DatabaseConn<Permission.ReadOnly> = DatabaseConn(conn)
        fun establishWrite(conn: Database): DatabaseConn<Permission.Writable> = DatabaseConn(conn)
    }
}

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