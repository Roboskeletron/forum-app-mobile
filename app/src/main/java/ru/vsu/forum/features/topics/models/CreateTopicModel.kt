package ru.vsu.forum.features.topics.models

data class CreateTopicModel(
    val title: String,
    val description: String? = null
)
