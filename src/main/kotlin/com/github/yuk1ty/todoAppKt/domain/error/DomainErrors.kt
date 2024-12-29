package com.github.yuk1ty.todoAppKt.domain.error

sealed class DomainErrors(why: String) : Throwable(why) {
    data class ValidationError(val why: String) : DomainErrors(why)
}