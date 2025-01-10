package com.github.yuk1ty.todoAppKt.shared

import com.github.yuk1ty.todoAppKt.shared.config.AppConfig
import com.github.yuk1ty.todoAppKt.shared.modules.establishDatabaseConnection
import com.github.yuk1ty.todoAppKt.shared.modules.registerDependencies
import com.github.yuk1ty.todoAppKt.shared.modules.registerExceptionHandlers
import com.github.yuk1ty.todoAppKt.shared.modules.registerSerializers
import io.ktor.server.application.*

fun Application.registerAppModules(cfg: AppConfig) {
    val (readConn, writeConn) = establishDatabaseConnection(cfg)
    registerDependencies(readConn, writeConn)
    registerSerializers()
    registerExceptionHandlers()
}
