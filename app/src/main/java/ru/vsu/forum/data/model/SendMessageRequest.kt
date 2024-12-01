package ru.vsu.forum.data.model

import java.util.UUID

data class SendMessageRequest(val topicId: UUID, val text: String)
