package com.github.yuk1ty.todoAppKt.shared

import com.github.yuk1ty.todoAppKt.shared.modules.establishDatabaseConnection
import com.github.yuk1ty.todoAppKt.shared.modules.registerDependencies
import com.github.yuk1ty.todoAppKt.shared.modules.registerExceptionHandlers
import com.github.yuk1ty.todoAppKt.shared.modules.registerSerializers
import io.ktor.server.application.*
import io.ktor.server.config.*

fun Application.registerAppModules(cfg: ApplicationConfig) {
    val (readConn, writeConn) = establishDatabaseConnection(cfg)
    registerDependencies(readConn, writeConn)
    registerSerializers()
    registerExceptionHandlers()
}
