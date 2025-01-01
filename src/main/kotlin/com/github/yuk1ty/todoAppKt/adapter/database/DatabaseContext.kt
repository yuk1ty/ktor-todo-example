package com.github.yuk1ty.todoAppKt.adapter.database

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
fun <T> DatabaseConn<Permission.ReadOnly>.beginRead(statement: Transaction.() -> T): T =
    transaction(transactionIsolation = Connection.TRANSACTION_READ_COMMITTED, db = this.inner, readOnly = true) {
        statement()
    }

fun <T> DatabaseConn<Permission.Writable>.beginWrite(statement: Transaction.() -> T): T =
    transaction(transactionIsolation = Connection.TRANSACTION_READ_COMMITTED, db = this.inner) {
        statement()
    }