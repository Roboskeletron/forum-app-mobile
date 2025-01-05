package ru.vsu.forum.features.messages.models

import java.util.UUID

data class SendMessageModel(val topicId: UUID, val text: String)
