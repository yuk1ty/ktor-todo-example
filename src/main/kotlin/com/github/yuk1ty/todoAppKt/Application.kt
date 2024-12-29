package com.github.yuk1ty.todoAppKt

import com.github.yuk1ty.todoAppKt.api.routing.todoAppRoutes
import com.github.yuk1ty.todoAppKt.shared.registerAppModules
import io.ktor.server.application.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    registerAppModules()
    todoAppRoutes()
}