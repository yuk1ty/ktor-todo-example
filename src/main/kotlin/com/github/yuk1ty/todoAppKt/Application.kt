package com.github.yuk1ty.todoAppKt

import com.github.yuk1ty.todoAppKt.api.routing.todoAppRoutes
import com.github.yuk1ty.todoAppKt.shared.config.AppConfig
import com.github.yuk1ty.todoAppKt.shared.registerAppModules
import io.ktor.server.application.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    val cfg = AppConfig.from(environment.config)

    registerAppModules(cfg)
    // Turn on in the future
    // openAPIRoutes()
    todoAppRoutes()

    environment.log.info("Start to run the server on ${cfg.server.host}:${cfg.server.port}")
}