package com.github.yuk1ty.todoAppKt.shared.utilities

import kotlinx.coroutines.supervisorScope
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import kotlin.coroutines.CoroutineContext

/**
 * Wrapper for [newSuspendedTransaction] that fixes error propagation.
 * A workaround for https://github.com/JetBrains/Exposed/issues/1075.
 */
suspend fun <T> newSuspendedTransactionWithExceptionHandling(
    context: CoroutineContext? = null,
    db: Database? = null,
    transactionIsolation: Int? = null,
    readonly: Boolean = false,
    statement: suspend Transaction.() -> T
): T {
    return supervisorScope {
        newSuspendedTransaction(context, db, transactionIsolation, readonly, statement)
    }
}