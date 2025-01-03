package com.github.yuk1ty.todoAppKt.shared.modules

import com.github.yuk1ty.todoAppKt.adapter.error.AdapterErrors
import com.github.yuk1ty.todoAppKt.api.error.HandlerErrors
import com.github.yuk1ty.todoAppKt.application.error.ApplicationServiceErrors
import com.github.yuk1ty.todoAppKt.domain.error.DomainErrors
import com.github.yuk1ty.todoAppKt.shared.AppErrors
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

internal fun Application.registerExceptionHandlers() {
    install(StatusPages) {
        exception<AppErrors> { call, cause ->
            when (cause) {
                is HandlerErrors.InvalidPathParameter -> call.respond(HttpStatusCode.BadRequest)

                is ApplicationServiceErrors.EntityNotFound -> call.respond(HttpStatusCode.NotFound)

                is DomainErrors.ValidationError -> call.respondText(
                    status = HttpStatusCode.BadRequest,
                    text = cause.why
                )

                is DomainErrors.ValidationErrors -> call.respondText(
                    status = HttpStatusCode.BadRequest,
                    text = cause.errors.joinToString(", ")
                )

                is AdapterErrors.DatabaseError -> {
                    call.application.environment.log.error("Database-related error happened", cause.cause)
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }
        }
    }
}