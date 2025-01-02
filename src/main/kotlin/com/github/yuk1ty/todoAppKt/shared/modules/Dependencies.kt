package com.github.yuk1ty.todoAppKt.shared.modules

import com.github.yuk1ty.todoAppKt.adapter.database.DatabaseConn
import com.github.yuk1ty.todoAppKt.adapter.database.Permission
import com.github.yuk1ty.todoAppKt.adapter.repository.TodoRepositoryImpl
import com.github.yuk1ty.todoAppKt.applicationService.TodoApplicationService
import com.github.yuk1ty.todoAppKt.domain.repository.TodoRepository
import com.github.yuk1ty.todoAppKt.queryService.TodoQueryService
import io.ktor.server.application.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
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
        single { TodoRepositoryImpl(writeConn) }.bind<TodoRepository>()
        singleOf(::TodoApplicationService)
        single { TodoQueryService(readConn) }
    }