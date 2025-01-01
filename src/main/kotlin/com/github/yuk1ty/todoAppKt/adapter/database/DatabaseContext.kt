package com.github.yuk1ty.todoAppKt.adapter.database

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.runCatching
import com.github.yuk1ty.todoAppKt.adapter.error.AdapterErrors
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

sealed interface Permission {
    data object ReadOnly : Permission
    data object Writable : Permission
}

@JvmInline
value class DatabaseConn<K : Permission>(val inner: Database) {
    companion object {
        fun establishRead(conn: Database): DatabaseConn<Permission.ReadOnly> = DatabaseConn(conn)
        fun establishWrite(conn: Database): DatabaseConn<Permission.Writable> = DatabaseConn(conn)
    }
}

// TODO: Needs to check whether rollback will happen correctly when passed Result type and it came to be an error.
fun <T> DatabaseConn<Permission.ReadOnly>.beginReadTransaction(statement: Transaction.() -> T): Result<T, AdapterErrors> =
    runCatching {
        transaction(transactionIsolation = Connection.TRANSACTION_READ_COMMITTED, db = this.inner, readOnly = true) {
            statement()
        }
    }.mapError { AdapterErrors.TransactionError(it) }

fun <T> DatabaseConn<Permission.Writable>.beginWriteTransaction(statement: Transaction.() -> T): Result<T, AdapterErrors> =
    runCatching {
        transaction(transactionIsolation = Connection.TRANSACTION_READ_COMMITTED, db = this.inner, readOnly = true) {
            statement()
        }
    }.mapError { AdapterErrors.TransactionError(it) }
