package com.github.yuk1ty.todoAppKt.application.error

import com.github.yuk1ty.todoAppKt.shared.AppErrors
import java.util.UUID

sealed class ApplicationServiceErrors(why: String?, cause: Throwable?) : AppErrors(why, cause) {
    data class EntityNotFound(val id: UUID) : ApplicationServiceErrors("Entity not found: $id", null)
}