package ru.vsu.forum.features.profile.view

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.vsu.forum.model.User

class ViewProfileViewModel : ViewModel() {
    private val _name = MutableLiveData<String>()
    private val _bio = MutableLiveData<String?>(null)
    private val _email = MutableLiveData<String>()

    val avatar = MutableLiveData<Bitmap?>(null)
    val name: LiveData<String> = _name
    val bio: LiveData<String?> = _bio
    val email: LiveData<String> = _email

    fun setProfile(profile: User) {
        _name.value = profile.name
        _bio.value = profile.description
        _email.value = profile.email
    }
}