package ru.vsu.forum.features.auth.view

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    private val _loginErrorVisibility = MutableLiveData<Int>(View.INVISIBLE)

    val loginErrorVisibility: LiveData<Int> = _loginErrorVisibility
    val email = MutableLiveData<String?>(null)
    val password = MutableLiveData<String?>(null)

    fun showLoginError(){
        _loginErrorVisibility.value = View.VISIBLE
    }
}