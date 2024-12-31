package com.github.yuk1ty.todoAppKt.domain.error

sealed class AppErrors(why: String) : Throwable(why)

sealed class DomainErrors(why: String) : AppErrors(why) {
    data class ValidationError(val why: String) : DomainErrors(why)
    data class ValidationErrors(val errors: List<ValidationError>) : DomainErrors(errors.joinToString(", "))
}