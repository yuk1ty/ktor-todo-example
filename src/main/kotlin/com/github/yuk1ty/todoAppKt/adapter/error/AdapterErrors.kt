package com.github.yuk1ty.todoAppKt.adapter.error

import com.github.yuk1ty.todoAppKt.shared.AppErrors

sealed class AdapterErrors(message: String?, cause: Throwable?) : AppErrors(message, cause) {
    data class TransactionError(override val cause: Throwable) : AdapterErrors(cause.message, cause)
    data class InvalidInsertionResult(val why: String) : AdapterErrors(why, null)
}