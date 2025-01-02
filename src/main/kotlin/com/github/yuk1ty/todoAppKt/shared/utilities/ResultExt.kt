package com.github.yuk1ty.todoAppKt.shared.utilities

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.partition

fun <E : Throwable> errorIdentity(x: E): Result<Nothing, E> = Err(x)

/**
 * Referring credit: https://speakerdeck.com/yuitosato/railway-oriented-programming-in-onion-architecture-by-kotlin-result?slide=26
 */
fun <T, E : Throwable> List<Result<T, E>>.combineOrAccumulate(): Result<List<T>, List<E>> {
    val (values, errors) = this.partition()
    return if (errors.isEmpty()) {
        Ok(values)
    } else {
        Err(errors)
    }
}