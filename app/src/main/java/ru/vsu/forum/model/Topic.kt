package ru.vsu.forum.model

import java.util.UUID

data class Topic(
    val id: UUID,
    val title: String,
    val description: String,
    val userCount: Int
)
