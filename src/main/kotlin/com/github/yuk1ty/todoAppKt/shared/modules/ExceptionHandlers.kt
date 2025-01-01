package com.github.yuk1ty.todoAppKt.shared.modules

import com.github.yuk1ty.todoAppKt.adapter.error.AdapterErrors
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
                is DomainErrors.ValidationError -> call.respondText(
                    status = HttpStatusCode.BadRequest,
                    text = cause.why
                )

                is DomainErrors.ValidationErrors -> call.respondText(
                    status = HttpStatusCode.BadRequest,
                    text = cause.errors.joinToString(", ")
                )

                is AdapterErrors.TransactionError -> {
                    call.application.environment.log.error("Database-related error happened", cause.cause)
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }
        }
    }
}