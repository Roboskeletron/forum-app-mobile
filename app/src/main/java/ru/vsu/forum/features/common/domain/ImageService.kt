package ru.vsu.forum.features.common.domain

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.vsu.forum.features.profile.data.UserRepository
import java.util.UUID

class ImageService(
    val userRepository: UserRepository
) {
    private val _avatar = MutableLiveData<Bitmap?>(null)

    val avatar: LiveData<Bitmap?> = _avatar

    suspend fun fetchAvatar(userId: UUID) {
        userRepository.getUserAvatar(userId)?.also {
            _avatar.value = BitmapFactory.decodeStream(it)
        }
    }
}