package ru.vsu.forum

class Constants {
    companion object{
        val SHARED_PREFERENCES_NAME = "AUTH_STATE_PREFERENCE"
        val AUTH_STATE = "AUTH_STATE"
        val URL_AUTHORIZATION = "https://accounts.google.com/o/oauth2/v2/auth"
        val URL_TOKEN_EXCHANGE = "https://www.googleapis.com/oauth2/v4/token"
        val URL_AUTH_REDIRECT = "com.ptruiz.authtest:/oauth2redirect"
        val URL_LOGOUT = "https://accounts.google.com/o/oauth2/revoke?token="
        val URL_LOGOUT_REDIRECT = "com.ptruiz.authtest:/logout"
        val SCOPE_PROFILE = "profile"
        val SCOPE_EMAIL = "email"
        val SCOPE_OPENID = "openid"

        val CLIENT_ID = "1080220280079-d0c0ba1076kdsr3d0pbuvd7e3npj1ket.apps.googleusercontent.com"
        val CODE_VERIFIER_CHALLENGE_METHOD = "S256"
        val MESSAGE_DIGEST_ALGORITHM = "SHA-256"
    }
}