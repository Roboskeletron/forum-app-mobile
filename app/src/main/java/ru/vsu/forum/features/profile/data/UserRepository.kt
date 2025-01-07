package ru.vsu.forum.features.profile.data

import android.util.Log
import ru.vsu.forum.features.common.data.ForumService
import ru.vsu.forum.features.profile.models.UpdateProfileModel
import ru.vsu.forum.model.RegisterUserModel
import ru.vsu.forum.model.User
import java.io.InputStream
import java.util.UUID

interface UserRepository {
    suspend fun getUserProfile(): User?
    suspend fun updateProfile(name: String, description: String? = null)
    suspend fun getById(id: UUID): User?
    suspend fun existsByEmail(email: String): Boolean
    suspend fun existsByUsername(username: String): Boolean
    suspend fun register(username: String, email: String, password: String)
    suspend fun getUserAvatar(id: UUID) : InputStream?
}

class UserRepositoryImpl(private val forumService: ForumService) : UserRepository {
    override suspend fun getUserProfile(): User? {
        try {
            val profile = forumService.getProfile()
            return profile.body()
        } catch (e: Exception) {
            Log.e(UserRepositoryImpl::class.qualifiedName, "Unable to get user profile", e)
            return null!!
        }
    }

    override suspend fun updateProfile(name: String, description: String?) {
        try {
            forumService.updateProfile(UpdateProfileModel(name, description))
        } catch (e: Exception) {
            Log.e(UserRepositoryImpl::class.qualifiedName, "Unable to update profile", e)
        }
    }

    override suspend fun getById(id: UUID): User? {
        try {
            val response = forumService.getUserById(id)
            return response.body()
        } catch (e: Exception) {
            Log.e(UserRepositoryImpl::class.qualifiedName, "Unable to get user by id", e)
            return null
        }
    }

    override suspend fun existsByEmail(email: String): Boolean {
        try {
            return forumService.userExistsByEmail(email).body() == true
        } catch (e: Exception) {
            Log.e(UserRepositoryImpl::class.qualifiedName, "Unable to check user existence by email", e)
        }

        return false
    }

    override suspend fun existsByUsername(username: String): Boolean {
        try {
            return forumService.userExistsByUsername(username).body() == true
        } catch (e: Exception) {
            Log.e(UserRepositoryImpl::class.qualifiedName, "Unable to check user existence by username", e)
        }

        return false
    }

    override suspend fun register(
        username: String,
        email: String,
        password: String
    ) {
        try {
            forumService.registerUser(RegisterUserModel(username, email, password))
        } catch (e: Exception) {
            Log.e(UserRepositoryImpl::class.qualifiedName, "Unable to register new user", e)
        }
    }

    override suspend fun getUserAvatar(id: UUID): InputStream? {
        try {
            val response = forumService.getUserAvatar(id)

            if (!response.isSuccessful) {
                return null
            }

            return response.body()?.byteStream()
        } catch (e: Exception) {
            Log.e(UserRepositoryImpl::class.qualifiedName, "Unable to register new user", e)
        }
        return null
    }
}