package ru.vsu.forum.features.messages.models

import java.util.UUID

data class UpdateCommentModel(
    val commentId: UUID,
    val text: String
)
