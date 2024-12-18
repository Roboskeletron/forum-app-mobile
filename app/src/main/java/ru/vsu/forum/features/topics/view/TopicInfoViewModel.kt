package ru.vsu.forum.features.topics.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.vsu.forum.features.topics.models.Topic
import ru.vsu.forum.model.User

class TopicInfoViewModel : ViewModel() {
    val topic = MutableLiveData<Topic?>()
    val author = MutableLiveData<User?>()
}