package ru.vsu.forum.features.messages.models

import java.util.UUID

data class SendCommentModel(
    val messageId: UUID,
    val comment: String
)
