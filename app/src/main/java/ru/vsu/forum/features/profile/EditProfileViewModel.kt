package ru.vsu.forum.features.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.vsu.forum.features.profile.data.UserRepository
import ru.vsu.forum.model.User
import java.util.UUID

class EditProfileViewModel(private val userRepository: UserRepository) : ViewModel() {
    private var _userProfile = MutableLiveData<User?>(null)

    val userProfile: LiveData<User?> = _userProfile

    fun getProfile(profileId: UUID? = null){
        viewModelScope.launch{
            _userProfile.value = userRepository.getUserProfile()
        }
    }
}