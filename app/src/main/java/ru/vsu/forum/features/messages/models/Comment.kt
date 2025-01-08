package ru.vsu.forum.features.messages.models

import java.time.LocalDateTime
import java.util.UUID

data class Comment(
    val id: UUID,
    val author: Author,
    val text: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
)