package ru.vsu.forum.features.auth.domain

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import ru.vsu.forum.features.auth.data.Keycloak
import ru.vsu.forum.features.auth.model.TokenData
import ru.vsu.forum.utils.Config

interface AuthManager {
    suspend fun tryLogin(username: String, password: String): Boolean
    suspend fun logout()
    suspend fun getTokenData(): TokenData?
    suspend fun refreshTokens(): TokenData?
}

class AuthManagerImpl(
    val keycloak: Keycloak,
    val dataStore: DataStore<Preferences>
) : AuthManager {
    private val accessTokenKey = stringPreferencesKey("access_token")
    private val refreshTokenKey = stringPreferencesKey("refresh_token")

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

            return true
        } catch (e: Exception) {
            Log.e(AuthManagerImpl::class.qualifiedName, "Login failed", e)
        }

        return false
    }

    override suspend fun logout() {
        dataStore.edit {
            it[accessTokenKey] = ""
            it[refreshTokenKey] = ""
        }
    }

    override suspend fun getTokenData(): TokenData? {
        val preferences = dataStore.data.first()

        val accessToken = preferences[accessTokenKey]
        val refreshToken = preferences[refreshTokenKey]

        return if (accessToken == null)
            null
        else TokenData(accessToken, refreshToken!!)
    }

    override suspend fun refreshTokens(): TokenData? =
        getTokenData()?.let {
            try {
                val response = keycloak.getTokenData(
                    Config.KEYCLOAK_CLIENT_ID,
                    "refresh_token",
                    refreshToken = it.refreshToken
                )

                if (!response.isSuccessful) {
                    return null
                }

                return response.body()!!.also { tokenData ->
                    dataStore.edit {
                        it[accessTokenKey] = tokenData.accessToken
                        it[refreshTokenKey] = tokenData.refreshToken
                    }
                }
            } catch (e: Exception) {
                Log.e(AuthManagerImpl::class.qualifiedName, "Refresh tokens failed", e)
            }

            return null
        }
}
