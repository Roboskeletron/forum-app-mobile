package ru.vsu.forum.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import ru.vsu.forum.model.Topic
import java.util.UUID

class HomeViewModel : ViewModel() {
    private val _topics = MutableLiveData<List<Topic>>()
    private val _searchQuery = MutableLiveData("")

    val filteredTopics: LiveData<List<Topic>> = _searchQuery.map { query ->
        _topics.value?.filter { topic ->
            topic.title.contains(query, ignoreCase = true) ||
                    topic.description.contains(query, ignoreCase = true)
        } ?: emptyList()
    }

    init {
        loadTopics()
    }

    private fun loadTopics() {
        val sampleTopics = listOf(
            Topic(UUID.randomUUID(), "Discussion on Android Development", "A place to discuss Android best practices",10),
            Topic(UUID.randomUUID(), "Kotlin vs Java", "Comparison between Kotlin and Java", 20),
            Topic(UUID.randomUUID(), "Jetpack Compose Basics", "Learn the basics of Jetpack Compose", 500),
            Topic(UUID.randomUUID(), "Jetpack Compose Basics", "Learn the basics of Jetpack Compose", 500),
            Topic(UUID.randomUUID(), "Jetpack Compose Basics", "Learn the basics of Jetpack Compose", 500),
            Topic(UUID.randomUUID(), "Jetpack Compose Basics", "Learn the basics of Jetpack Compose", 500),
            Topic(UUID.randomUUID(), "Jetpack Compose Basics", "Learn the basics of Jetpack Compose", 500),
            Topic(UUID.randomUUID(), "Jetpack Compose Basics", "Learn the basics of Jetpack Compose", 500),
            Topic(UUID.randomUUID(), "Jetpack Compose Basics", "Learn the basics of Jetpack Compose", 500),
            Topic(UUID.randomUUID(), "Jetpack Compose Basics", "Learn the basics of Jetpack Compose", 500),
            Topic(UUID.randomUUID(), "Jetpack Compose Basics", "Learn the basics of Jetpack Compose", 500),
            Topic(UUID.randomUUID(), "Jetpack Compose Basics", "Learn the basics of Jetpack Compose", 500)
        )
        _topics.value = sampleTopics
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
}