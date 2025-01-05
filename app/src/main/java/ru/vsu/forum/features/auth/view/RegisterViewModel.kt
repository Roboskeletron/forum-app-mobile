package ru.vsu.forum.features.auth.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {
    val username = MutableLiveData<String?>(null)
    val email = MutableLiveData<String?>(null)
    val password = MutableLiveData<String?>(null)
}