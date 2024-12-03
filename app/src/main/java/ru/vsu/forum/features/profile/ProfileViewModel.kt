package ru.vsu.forum.features.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.vsu.forum.model.User

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
        return if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            ProfileViewModel() as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}