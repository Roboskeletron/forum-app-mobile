package ru.vsu.forum.model

import java.time.LocalDateTime
import java.util.UUID

data class Message(
    val id: UUID,
    val author: Author,
    val text: String,
    val createdAt: String,
    val updatedAt: String?,
    val likeCount: Int
)

