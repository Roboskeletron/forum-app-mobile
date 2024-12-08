package ru.vsu.forum.features.profile.data

import android.util.Log
import ru.vsu.forum.features.common.data.ForumService
import ru.vsu.forum.model.User

interface UserRepository {
    suspend fun getUserProfile(): User?
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
}