package ru.vsu.forum.features.profile.data

import android.util.Log
import ru.vsu.forum.features.common.data.ForumService
import ru.vsu.forum.features.profile.models.UpdateProfileModel
import ru.vsu.forum.model.User
import java.util.UUID

interface UserRepository {
    suspend fun getUserProfile(): User?
    suspend fun updateProfile(name: String, description: String? = null)
    suspend fun getById(id: UUID) : User?
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
            Log.e(UserRepositoryImpl::class.qualifiedName, "Unable to update profile", e)
        }
    }

    override suspend fun getById(id: UUID): User? {
        try {
            val response = forumService.getUserById(id)
            return response.body()
        }
        catch (e: Exception){
            Log.e(UserRepositoryImpl::class.qualifiedName, "Unable to get user by id", e)
            return null
        }
    }
}