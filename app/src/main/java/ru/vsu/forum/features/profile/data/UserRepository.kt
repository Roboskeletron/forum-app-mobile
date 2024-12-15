package ru.vsu.forum.features.profile.data

import android.util.Log
import ru.vsu.forum.features.common.data.ForumService
import ru.vsu.forum.features.common.models.UpdateProfileModel
import ru.vsu.forum.model.User

interface UserRepository {
    suspend fun getUserProfile(): User?
    suspend fun updateProfile(name: String, description: String? = null)
}

class UserRepositoryImpl(private val forumService: ForumService) : UserRepository {
    override suspend fun getUserProfile(): User? {
        try {
            val profile = forumService.getProfile()
            return  profile.body()
        }
        catch (e: Exception){
            Log.e(UserRepositoryImpl::class.qualifiedName, "Unable to get user profile", e)
            return null!!
        }
    }

    override suspend fun updateProfile(name: String, description: String?) {
        try {
            forumService.updateProfile(UpdateProfileModel(name, description))
        }
        catch (e: Exception) {
            Log.e(UserRepositoryImpl::class.qualifiedName, "Unable to update profile")
        }
    }
}