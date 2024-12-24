package ru.vsu.forum.features.topics.models

import java.util.UUID

data class UpdateTopicModel(
    val topicId: UUID,
    val title: String,
    val description: String?
)
