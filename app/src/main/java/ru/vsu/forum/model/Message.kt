package ru.vsu.forum.model

import java.time.LocalDateTime
import java.util.UUID

data class Message(
    val id: UUID,
    val userId: UUID,
    val userName: String,
    val userAvatar: String?,
    val content: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
    val likes: Int
)

