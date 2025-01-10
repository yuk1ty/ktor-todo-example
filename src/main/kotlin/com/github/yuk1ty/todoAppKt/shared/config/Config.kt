package com.github.yuk1ty.todoAppKt.shared.config

import io.ktor.server.config.*

data class DatabaseConfig(
    val jdbcUrl: String,
    val driverName: String,
    val user: String,
    val password: String,
)

data class ServerConfig(
    val host: String,
    val port: Int,
)

data class AppConfig(
    val readableDatabase: DatabaseConfig,
    val writableDatabase: DatabaseConfig,
    val server: ServerConfig,
) {
    companion object {
        fun from(cfg: ApplicationConfig): AppConfig {
            val readable = cfg.run {
                val jdbcUrl = property("db.read.jdbcURL").getString()
                val user = property("db.read.user").getString()
                val password = property("db.read.password").getString()
                val driverName = property("db.read.driverClassName").getString()
                DatabaseConfig(jdbcUrl, driverName, user, password)
            }
            val writable = cfg.run {
                val jdbcUrl = property("db.write.jdbcURL").getString()
                val user = property("db.write.user").getString()
                val password = property("db.write.password").getString()
                val driverName = property("db.write.driverClassName").getString()
                DatabaseConfig(jdbcUrl, driverName, user, password)
            }
            val server = cfg.run {
                val host = property("ktor.deployment.host").getString()
                val port = property("ktor.deployment.port").getString().toInt()
                ServerConfig(host, port)
            }
            return AppConfig(readable, writable, server)
        }
    }
}