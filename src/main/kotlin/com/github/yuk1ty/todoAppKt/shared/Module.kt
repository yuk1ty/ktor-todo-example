package com.github.yuk1ty.todoAppKt.shared

import com.github.yuk1ty.todoAppKt.shared.modules.registerDependencies
import com.github.yuk1ty.todoAppKt.shared.modules.registerExceptionHandlers
import com.github.yuk1ty.todoAppKt.shared.modules.registerSerializers
import io.ktor.server.application.*

fun Application.registerAppModules() {
    registerDependencies()
    registerSerializers()
    registerExceptionHandlers()
}
