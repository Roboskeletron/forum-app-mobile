package ru.vsu.forum.features.topics.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddTopicViewModel : ViewModel() {
    private var _error = MutableLiveData<String?>(null)

    var title: String? = null
    var description: String? = null
    val error: LiveData<String?> = _error

    fun setError(errorText: String?){
        _error.value = errorText
    }
}