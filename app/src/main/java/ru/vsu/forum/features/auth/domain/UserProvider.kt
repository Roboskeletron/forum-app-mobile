package ru.vsu.forum.features.auth.domain

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import ru.vsu.forum.features.auth.data.Keycloak
import ru.vsu.forum.features.profile.data.UserRepository
import ru.vsu.forum.features.profile.data.UserRepositoryImpl
import ru.vsu.forum.model.User
import ru.vsu.forum.utils.Config

interface UserProvider {
    val user: User?

    suspend fun tryLogin(username: String, password: String) : Boolean

    suspend fun logout()
}

class UserProviderImpl(
    val dataStore: DataStore<Preferences>,
    val keycloak: Keycloak,
    val userRepository: UserRepository
) : UserProvider {
    private var _user: User? = null
    private val accessTokenKey = stringPreferencesKey("access_token")
    private val refreshTokenKey = stringPreferencesKey("refresh_token")

    override val user: User? = _user

    override suspend fun tryLogin(
        username: String,
        password: String
    ): Boolean {
        try {
            val response = keycloak.getTokenData(
                Config.KEYCLOAK_CLIENT_ID,
                "password",
                username = username,
                password = password
            )

            if (!response.isSuccessful) {
                return false
            }

            val tokens = response.body()!!

            dataStore.edit {
                it[accessTokenKey] = tokens.accessToken
                it[refreshTokenKey] = tokens.refreshToken
            }

            _user = userRepository.getUserProfile()

            return  true
        }
        catch (e: Exception) {
            Log.e(UserRepositoryImpl::class.qualifiedName, "Login failed", e)
        }

        return false
    }

    override suspend fun logout() {
        dataStore.edit {

            _user = null
            it[accessTokenKey] = ""
            it[refreshTokenKey] = ""
        }
    }
}
