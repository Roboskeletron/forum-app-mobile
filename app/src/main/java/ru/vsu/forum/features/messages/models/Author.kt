package ru.vsu.forum.features.messages.models

import java.util.UUID

data class Author(
    val id: UUID,
    val name : String,
    val avatar: String?
)
