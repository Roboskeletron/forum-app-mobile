package ru.vsu.forum.features.topics.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EditTopicViewModel : ViewModel() {
    private var _error = MutableLiveData<String?>(null)

    var title = MutableLiveData<String?>(null)
    var description = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    fun setError(errorText: String?){
        _error.value = errorText
    }
}