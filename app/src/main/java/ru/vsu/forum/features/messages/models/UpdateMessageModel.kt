package ru.vsu.forum.features.messages.models

import java.util.UUID

data class UpdateMessageModel(
    val messageId: UUID,
    val text: String
)