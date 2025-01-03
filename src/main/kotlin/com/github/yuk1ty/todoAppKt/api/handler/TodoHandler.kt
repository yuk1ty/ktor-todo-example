package com.github.yuk1ty.todoAppKt.api.handler

import com.github.michaelbull.result.*
import com.github.michaelbull.result.coroutines.coroutineBinding
import com.github.yuk1ty.todoAppKt.api.error.HandlerErrors
import com.github.yuk1ty.todoAppKt.api.model.CreateTodoRequest
import com.github.yuk1ty.todoAppKt.api.model.TodoResponse
import com.github.yuk1ty.todoAppKt.api.model.UpdateTodoRequest
import com.github.yuk1ty.todoAppKt.api.routing.API_V1
import com.github.yuk1ty.todoAppKt.application.command.TodoCommands
import com.github.yuk1ty.todoAppKt.application.service.TodoApplicationService
import com.github.yuk1ty.todoAppKt.queryService.TodoQueryService
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Contextual
import org.koin.ktor.ext.inject
import java.util.UUID

private sealed interface Todos {
    @Resource("/todos")
    data object GetAll : Todos

    @Resource("/todos/{todoId}")
    data class GetOneById(@Contextual val todoId: UUID) : Todos

    @Resource("/todos")
    data object RegisterTask : Todos

    @Resource("/todos/{todoId}")
    data object EditTask : Todos

    @Resource("/todos/{todoId}")
    data object DeleteTask : Todos
}

private fun Route.todoHandler() {
    val todoQueryService by inject<TodoQueryService>()
    val todoApplicationService by inject<TodoApplicationService>()

    get<Todos.GetAll> {
        coroutineBinding {
            val allTodos = todoQueryService.getTodos().bind()
            val res = allTodos.map { TodoResponse.fromTodo(it) }

            call.respond(HttpStatusCode.OK, res)
        }.getOrThrow()
    }

    post<Todos.RegisterTask> {
        coroutineBinding {
            val req = runCatching { call.receive<CreateTodoRequest>() }.bind()
            todoApplicationService.createTodo(req.intoCommand()).bind()

            call.respond(HttpStatusCode.Created)
        }.getOrThrow()
    }

    put<Todos.EditTask> {
        coroutineBinding {
            val req = runCatching { call.receive<UpdateTodoRequest>() }.bind()
            val id =
                runCatching { call.parameters["todoId"]?.let { UUID.fromString(it) } }.toErrorIfNull {
                    HandlerErrors.InvalidPathParameter("$it")
                }.bind()
            todoApplicationService.updateTodo(req.intoCommand(id)).bind()

            call.respond(HttpStatusCode.OK)
        }.getOrThrow()
    }

    delete<Todos.DeleteTask> {
        coroutineBinding {
            val id = runCatching { call.parameters["todoId"]?.let { UUID.fromString(it) } }.toErrorIfNull {
                HandlerErrors.InvalidPathParameter("$it")
            }.bind()
            todoApplicationService.deleteTodo(TodoCommands.Delete(id)).bind()

            call.respond(HttpStatusCode.OK)
        }.getOrThrow()
    }
}

internal fun Route.registerTodoHandler() {
    route(API_V1) {
        todoHandler()
    }
}

