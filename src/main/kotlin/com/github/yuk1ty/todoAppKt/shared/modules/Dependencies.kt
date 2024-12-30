package com.github.yuk1ty.todoAppKt.shared.modules

import com.github.yuk1ty.todoAppKt.queryService.TodoQueryService
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

internal fun Application.registerDependencies(
    readConn: DatabaseConn<Permission.ReadOnly>,
    writeConn: DatabaseConn<Permission.Writable>
) {
    install(Koin) {
        slf4jLogger()
        modules(appModule(readConn, writeConn))
    }
}

private fun appModule(readConn: DatabaseConn<Permission.ReadOnly>, writeConn: DatabaseConn<Permission.Writable>) =
    module {
        single { TodoQueryService(readConn) }
    }