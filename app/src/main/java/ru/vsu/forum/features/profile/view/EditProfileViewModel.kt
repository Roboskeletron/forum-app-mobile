package ru.vsu.forum.features.profile.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.vsu.forum.model.User
import java.util.UUID

class EditProfileViewModel : ViewModel() {
    private val _email = MutableLiveData<String?>(null)
    private var _id: UUID? = null

    val id = _id
    val name = MutableLiveData<String?>(null)
    val email: LiveData<String?> = _email
    val description = MutableLiveData<String?>(null)

    fun setProfile(profile: User?){
        _id = profile?.id
        name.value = profile?.name
        _email.value = profile?.email
        description.value = profile?.description
    }
}