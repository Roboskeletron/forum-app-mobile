package ru.vsu.forum.features.auth.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.vsu.forum.features.profile.data.UserRepository
import ru.vsu.forum.model.User

interface UserProvider {
    val user: LiveData<User?>

    suspend fun tryLogin(username: String, password: String): Boolean
    suspend fun logout()
    suspend fun tryLogin(): Boolean
}

class UserProviderImpl(
    val authManager: AuthManager,
    val userRepository: UserRepository
) : UserProvider {
    private val _user = MutableLiveData<User?>(null)

    override val user: LiveData<User?>
        get() = _user

    override suspend fun tryLogin(
        username: String,
        password: String
    ): Boolean = authManager.tryLogin(username, password).also {
        if (it){
            _user.value = userRepository.getUserProfile()
        }
    }

    override suspend fun logout() = authManager.logout().also {
        _user.value = null
    }

    override suspend fun tryLogin(): Boolean = authManager.refreshTokens()?.let {
        _user.value = userRepository.getUserProfile()
        true
    } == true
}
