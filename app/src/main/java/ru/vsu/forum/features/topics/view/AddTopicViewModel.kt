package ru.vsu.forum.features.topics.view

import androidx.databinding.BaseObservable
import androidx.databinding.Observable
import androidx.lifecycle.ViewModel

class AddTopicViewModel : ViewModel() {
    var title: String? = null
    var description: String? = null
}