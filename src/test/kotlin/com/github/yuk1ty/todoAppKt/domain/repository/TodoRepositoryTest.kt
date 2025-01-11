package com.github.yuk1ty.todoAppKt.domain.repository

import com.github.yuk1ty.todoAppKt.adapter.database.DatabaseConn
import com.github.yuk1ty.todoAppKt.adapter.database.Permission
import com.github.yuk1ty.todoAppKt.test.util.TestDatabaseConfig
import io.kotest.core.spec.style.DescribeSpec

class TodoRepositoryTest : DescribeSpec({

    var conn: DatabaseConn<Permission.Writable>

    beforeContainer {
        val (_, write) = TestDatabaseConfig.establishTestConnection()
        conn = write
    }

    describe("#create") {
        describe("when todo is valid") {
            it("should return success") {

            }
        }
    }
})