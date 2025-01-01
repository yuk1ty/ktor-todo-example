package com.github.yuk1ty.todoAppKt.adapter.models

import java.time.LocalDateTime
import java.util.UUID

data class TodoRow(
    val id: UUID,
    val title: String,
    val description: String?,
    val due: LocalDateTime,
    val status: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)