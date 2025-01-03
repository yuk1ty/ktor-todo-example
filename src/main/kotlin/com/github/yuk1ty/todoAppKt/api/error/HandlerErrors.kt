package com.github.yuk1ty.todoAppKt.api.error

import com.github.yuk1ty.todoAppKt.shared.AppErrors

sealed class HandlerErrors(why: String?, cause: Throwable?) : AppErrors(why, cause) {
    data class InvalidPathParameter(val text: String) : HandlerErrors("Invalid path parameter: $text", null)
}