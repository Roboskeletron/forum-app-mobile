package ru.vsu.forum.features.auth.data

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import ru.vsu.forum.features.auth.model.TokenData

interface Keycloak {
    @POST("protocol/openid-connect/token")
    @FormUrlEncoded
    suspend fun getTokenData(
        @Field("client_id") clientId: String,
        @Field("grant_type") grantType: String,
        @Field("refresh_token") refreshToken: String? = null,
        @Field("username") username: String? = null,
        @Field("password") password: String? = null,
    ) : Response<TokenData>
}