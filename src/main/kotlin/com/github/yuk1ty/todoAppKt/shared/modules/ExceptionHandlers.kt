package com.github.yuk1ty.todoAppKt.shared.modules

import com.github.yuk1ty.todoAppKt.domain.error.AppErrors
import com.github.yuk1ty.todoAppKt.domain.error.DomainErrors
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun StatusPagesConfig.registerDomainExceptions() {
    exception<AppErrors> { call, cause ->
        when (cause) {
            is DomainErrors.ValidationError -> call.respondText(status = HttpStatusCode.BadRequest, text = cause.why)
        }
    }
}

internal fun Application.registerExceptionHandlers() {
    install(StatusPages) {
        registerDomainExceptions()
    }
}