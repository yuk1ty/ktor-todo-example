package com.github.yuk1ty.todoAppKt.adapter.database

import com.github.michaelbull.result.*
import com.github.yuk1ty.todoAppKt.adapter.error.AdapterErrors
import com.github.yuk1ty.todoAppKt.shared.AppErrors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

suspend fun <T> DatabaseConn<Permission.ReadOnly>.beginReadTransaction(statement: Transaction.() -> T): Result<T, AppErrors> {
    val conn = this.inner
    return withContext(Dispatchers.IO) {
        runCatching {
            transaction(
                db = conn,
            ) {
                statement()
            }
        }.mapError { AdapterErrors.DatabaseError(it) }
    }
}

suspend fun <T> DatabaseConn<Permission.Writable>.tryBeginWriteTransaction(statement: Transaction.() -> Result<T, AppErrors>): Result<T, AppErrors> {
    val conn = this.inner
    return withContext(Dispatchers.IO) {
        runCatching {
            transaction(
                transactionIsolation = Connection.TRANSACTION_READ_COMMITTED,
                db = conn,
            ) {
                statement().getOrThrow()
            }
        }.mapError { AdapterErrors.DatabaseError(it) }
    }
}