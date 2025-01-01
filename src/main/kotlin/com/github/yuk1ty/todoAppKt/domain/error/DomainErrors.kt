package com.github.yuk1ty.todoAppKt.domain.error

import com.github.yuk1ty.todoAppKt.shared.AppErrors

sealed class DomainErrors(why: String) : AppErrors(message = why, cause = null) {
    data class ValidationError(val why: String) : DomainErrors(why)
    data class ValidationErrors(val errors: List<ValidationError>) : DomainErrors(errors.joinToString(", "))
}