package com.github.yuk1ty.todoAppKt.domain.model.common

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.yuk1ty.todoAppKt.domain.error.DomainErrors

object Strings {
    @JvmInline
    value class String1024 private constructor(val value: String) {
        companion object {
            operator fun invoke(value: String): Result<String1024, DomainErrors.ValidationError> {
                return if (value.length <= 1024) {
                    Ok(String1024(value))
                } else {
                    Err(DomainErrors.ValidationError("String length must be less than or equal to 1024"))
                }
            }
        }
    }

    @JvmInline
    value class String2048 private constructor(val value: String) {
        companion object {
            operator fun invoke(value: String): Result<String2048, DomainErrors.ValidationError> {
                return if (value.length <= 2048) {
                    Ok(String2048(value))
                } else {
                    Err(DomainErrors.ValidationError("String length must be less than or equal to 2048"))
                }
            }
        }
    }
}
