package ru.vsu.forum.ui.topic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.vsu.forum.model.Message
import java.time.LocalDateTime
import java.util.UUID

class TopicViewModel : ViewModel() {

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    fun loadMessages(topicId: UUID) {
        _messages.value = listOf(
            Message(UUID.randomUUID(), UUID.randomUUID(), "user1", null, "This is a message", LocalDateTime.now(), null, 10),
            Message(UUID.randomUUID(), UUID.randomUUID(), "user2", null, "Another message", LocalDateTime.now().minusDays(1), LocalDateTime.now(), 5)
        )
    }
}
