package ru.vsu.forum.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.vsu.forum.model.Topic
import java.util.UUID

class HomeViewModel : ViewModel() {
    // Модель данных для топиков
    private val _topics = MutableLiveData<List<Topic>>()
    val topics: LiveData<List<Topic>> get() = _topics

    init {
        // Инициализация списка топиков
        loadTopics()
    }

    private fun loadTopics() {
        // Пример данных
        val sampleTopics = listOf(
            Topic(UUID.randomUUID(), "Discussion on Android Development", "A place to discuss Android best practices",10),
            Topic(UUID.randomUUID(), "Kotlin vs Java", "Comparison between Kotlin and Java", 20),
            Topic(UUID.randomUUID(), "Jetpack Compose Basics", "Learn the basics of Jetpack Compose", 500)
        )
        _topics.value = sampleTopics
    }
}