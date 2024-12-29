package com.github.yuk1ty.todoAppKt.api.handler

import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

private sealed interface HealthCheck {
    @Resource("/health")
    data object DiagnoseApp : HealthCheck

    @Resource("/health/db")
    data object DiagnoseDb : HealthCheck
}

private fun Route.healthCheckHandler() {
    get<HealthCheck.DiagnoseApp> {
        call.respond(HttpStatusCode.OK)
    }

    get<HealthCheck.DiagnoseDb> {
        call.respond(HttpStatusCode.OK)
    }
}

internal fun Route.registerHealthCheckHandler() {
    healthCheckHandler()
}