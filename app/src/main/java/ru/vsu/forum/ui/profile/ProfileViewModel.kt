package ru.vsu.forum.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.vsu.forum.api.ForumApi
import ru.vsu.forum.model.User
import ru.vsu.forum.ui.topic.TopicViewModel
import java.util.UUID

class ProfileViewModel() : ViewModel() {
    private var _userProfile = MutableLiveData<User?>(null)
    public val userProfile: LiveData<User?> = _userProfile

    fun  setUser(user: User) {
        _userProfile.value = user
    }
}

class ProfileViewModelFactory(
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(TopicViewModel::class.java)) {
            ProfileViewModel() as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}