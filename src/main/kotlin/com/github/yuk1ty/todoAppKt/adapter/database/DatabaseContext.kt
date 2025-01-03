package com.github.yuk1ty.todoAppKt.adapter.database

import com.github.michaelbull.result.*
import com.github.yuk1ty.todoAppKt.adapter.error.AdapterErrors
import com.github.yuk1ty.todoAppKt.shared.utilities.newSuspendedTransactionWithExceptionHandling
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
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

suspend fun <T> DatabaseConn<Permission.ReadOnly>.beginReadTransaction(statement: Transaction.() -> T): Result<T, AdapterErrors> {
    val conn = this.inner
    return runCatching {
        newSuspendedTransactionWithExceptionHandling(
            transactionIsolation = Connection.TRANSACTION_READ_COMMITTED,
            db = conn,
            readonly = true
        ) {
            statement()
        }
    }.mapError { AdapterErrors.DatabaseError(it) }
}

suspend fun <T> DatabaseConn<Permission.ReadOnly>.tryBeginReadTransaction(statement: Transaction.() -> Result<T, AdapterErrors>): Result<T, AdapterErrors> {
    val conn = this.inner
    return runCatching {
        newSuspendedTransactionWithExceptionHandling(
            transactionIsolation = Connection.TRANSACTION_READ_COMMITTED,
            db = conn,
            readonly = true
        ) {
            statement().getOrThrow()
        }
    }.mapError { AdapterErrors.DatabaseError(it) }
}

// This isn't used in our implementation, but it's here for reference.
//suspend fun <T> DatabaseConn<Permission.Writable>.beginWriteTransaction(statement: suspend Transaction.() -> T): Result<T, AdapterErrors> {
//    val conn = this.inner
//    return runCatching {
//        newSuspendedTransactionWithExceptionHandling(
//            transactionIsolation = Connection.TRANSACTION_READ_COMMITTED,
//            db = conn,
//        ) {
//            statement()
//        }
//    }.mapError { AdapterErrors.DatabaseError(it) }
//}

suspend fun <T> DatabaseConn<Permission.Writable>.tryBeginWriteTransaction(statement: suspend Transaction.() -> Result<T, AdapterErrors>): Result<T, AdapterErrors> {
    val conn = this.inner
    return runCatching {
        newSuspendedTransactionWithExceptionHandling(
            transactionIsolation = Connection.TRANSACTION_READ_COMMITTED,
            db = conn,
        ) {
            statement().getOrThrow()
        }
    }.mapError { AdapterErrors.DatabaseError(it) }
}