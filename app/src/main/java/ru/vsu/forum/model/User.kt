package ru.vsu.forum.model

import java.util.UUID

data class User(
    val id:UUID,
    val name: String,
    val email: String,
    val description: String?,
    val avatarId: UUID,
    val createdAt: String
)
