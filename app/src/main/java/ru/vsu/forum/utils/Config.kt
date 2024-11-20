package ru.vsu.forum.utils

object Config {
    const val SHARED_PREFERENCES_NAME = "AUTH_STATE_PREFERENCE"
    const val AUTH_STATE = "AUTH_STATE"
    const val URL_AUTHORIZATION = "https://accounts.google.com/o/oauth2/v2/auth"
    const val URL_TOKEN_EXCHANGE = "https://www.googleapis.com/oauth2/v4/token"
    const val URL_AUTH_REDIRECT = "com.ptruiz.authtest:/oauth2redirect"
    const val URL_LOGOUT = "https://accounts.google.com/o/oauth2/revoke?token="
    const val URL_LOGOUT_REDIRECT = "com.ptruiz.authtest:/logout"
    const val SCOPE_PROFILE = "profile"
    const val SCOPE_EMAIL = "email"
    const val SCOPE_OPENID = "openid"

    const val CLIENT_ID =
        "1080220280079-d0c0ba1076kdsr3d0pbuvd7e3npj1ket.apps.googleusercontent.com"
    const val CODE_VERIFIER_CHALLENGE_METHOD = "S256"
    const val MESSAGE_DIGEST_ALGORITHM = "SHA-256"

    const val BASE_URL = "http:10.0.2.2:5218/api/"
}