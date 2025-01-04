package ru.vsu.forum.features.auth.model

data class TokenData(
    val accessToken: String,
    val refreshToken: String
)
