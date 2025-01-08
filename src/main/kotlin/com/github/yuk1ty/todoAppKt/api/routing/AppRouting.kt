package com.github.yuk1ty.todoAppKt.api.routing

import com.github.yuk1ty.todoAppKt.api.handler.registerHealthCheckHandler
import com.github.yuk1ty.todoAppKt.api.handler.registerTodoHandler
import io.ktor.server.application.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.routing.*
import io.ktor.server.resources.Resources

const val API_V1 = "/api/v1"

fun Application.openAPIRoutes() {
    routing {
        openAPI("/openapi", swaggerFile = "openapi/documentation.yaml")
    }
}

fun Application.todoAppRoutes() {
    install(Resources)
    routing {
        registerHealthCheckHandler()
        registerTodoHandler()
    }
}