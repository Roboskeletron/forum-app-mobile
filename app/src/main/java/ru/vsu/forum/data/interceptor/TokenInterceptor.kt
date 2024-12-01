package ru.vsu.forum.data.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import ru.vsu.forum.utils.Config

class TokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val requestHeaders = request.headers()
            .newBuilder()
            .add("Authorization", "Bearer ${Config.AUTH_TOKEN}")
            .build()

        request = request.newBuilder()
            .headers(requestHeaders)
            .build()

        return chain.proceed(request)
    }
}