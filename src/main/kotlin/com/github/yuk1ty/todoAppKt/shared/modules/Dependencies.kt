package com.github.yuk1ty.todoAppKt.shared.modules

import com.github.yuk1ty.todoAppKt.queryService.TodoQueryService
import io.ktor.server.application.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

internal fun Application.registerDependencies() {
    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }
}

private val appModule = module {
    singleOf(::TodoQueryService)
}