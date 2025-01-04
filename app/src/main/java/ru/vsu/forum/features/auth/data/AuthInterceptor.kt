package ru.vsu.forum.features.auth.data

import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import ru.vsu.forum.features.auth.domain.AuthManager

class AuthInterceptor(
    val authManager: AuthManager
) : Interceptor, Authenticator {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        runBlocking {
            authManager.getTokenData()
        }?.also {
            val requestHeaders = request.headers()
                .newBuilder()
                .add("Authorization", "Bearer ${it.accessToken}")
                .build()

            request = request.newBuilder()
                .headers(requestHeaders)
                .build()
        }

        return chain.proceed(request)
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            authManager.refreshTokens()
        }.let {
            if (it == null) {
                return null
            }

            response.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer ${it.accessToken}")
                .build()
        }
    }
}