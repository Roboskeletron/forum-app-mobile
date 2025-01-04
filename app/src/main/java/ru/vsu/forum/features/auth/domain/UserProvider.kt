package ru.vsu.forum.features.auth.domain

import ru.vsu.forum.features.profile.data.UserRepository
import ru.vsu.forum.model.User

interface UserProvider {
    val user: User?

    suspend fun tryLogin(username: String, password: String): Boolean
    suspend fun logout()
}

class UserProviderImpl(
    val authManager: AuthManager,
    val userRepository: UserRepository
) : UserProvider {
    private var _user: User? = null

    override val user: User? = _user

    override suspend fun tryLogin(
        username: String,
        password: String
    ): Boolean = authManager.tryLogin(username, password).also {
        if (it){
            _user = userRepository.getUserProfile()
        }
    }

    override suspend fun logout() = authManager.logout().also {
        _user = null
    }
}
