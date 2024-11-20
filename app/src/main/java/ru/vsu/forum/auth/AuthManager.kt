//package ru.vsu.forum.auth
//
//import android.app.Application
//import android.content.Context
//import android.net.Uri
//import android.text.TextUtils
//import android.util.Base64
//import androidx.activity.result.ActivityResultLauncher
//import androidx.activity.result.ActivityResultRegistry
//import androidx.lifecycle.DefaultLifecycleObserver
//import com.auth0.android.jwt.JWT
//import net.openid.appauth.AppAuthConfiguration
//import net.openid.appauth.AuthState
//import net.openid.appauth.AuthorizationRequest
//import net.openid.appauth.AuthorizationService
//import net.openid.appauth.AuthorizationServiceConfiguration
//import net.openid.appauth.ResponseTypeValues
//import net.openid.appauth.browser.BrowserAllowList
//import net.openid.appauth.browser.VersionedBrowserMatcher
//import org.json.JSONException
//import ru.vsu.forum.utils.Config
//import java.security.MessageDigest
//import java.security.SecureRandom
//
//class AuthManager(private val registry : ActivityResultRegistry) : DefaultLifecycleObserver {
//    lateinit var getContent : ActivityResultLauncher<String>
//
//    private var authState: AuthState = AuthState()
//    private var jwt : JWT? = null
//    private lateinit var authorizationService : AuthorizationService
//    lateinit var authServiceConfig : AuthorizationServiceConfiguration
//
//    fun restoreState(application: Application) {
//        val jsonString = application
//            .getSharedPreferences(Config.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
//            .getString(Config.AUTH_STATE, null)
//
//        if( jsonString != null && !TextUtils.isEmpty(jsonString) ) {
//            try {
//                authState = AuthState.jsonDeserialize(jsonString)
//
//                if( !TextUtils.isEmpty(authState.idToken) ) {
//                    jwt = JWT(authState.idToken!!)
//                }
//
//            } catch(jsonException: JSONException) { }
//        }
//    }
//
//    fun persistState(application: Application) {
//        application.getSharedPreferences(Config.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
//            .edit()
//            .putString(Config.AUTH_STATE, authState.jsonSerializeString())
//            .apply()
//    }
//
//    private fun initAuthServiceConfig() {
//        authServiceConfig = AuthorizationServiceConfiguration(
//            Uri.parse(Config.URL_AUTHORIZATION),
//            Uri.parse(Config.URL_TOKEN_EXCHANGE),
//            null,
//            Uri.parse(Config.URL_LOGOUT))
//    }
//
//    private fun initAuthService(application: Application) {
//        val appAuthConfiguration = AppAuthConfiguration.Builder()
//            .setBrowserMatcher(
//                BrowserAllowList(
//                    VersionedBrowserMatcher.CHROME_CUSTOM_TAB,
//                    VersionedBrowserMatcher.SAMSUNG_CUSTOM_TAB
//                )
//            ).build()
//
//        authorizationService = AuthorizationService(
//            application,
//            appAuthConfiguration)
//    }
//
//    fun attemptAuthorization() {
//        val secureRandom = SecureRandom()
//        val bytes = ByteArray(64)
//        secureRandom.nextBytes(bytes)
//
//        val encoding = Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP
//        val codeVerifier = Base64.encodeToString(bytes, encoding)
//
//        val digest = MessageDigest.getInstance(Config.MESSAGE_DIGEST_ALGORITHM)
//        val hash = digest.digest(codeVerifier.toByteArray())
//        val codeChallenge = Base64.encodeToString(hash, encoding)
//
//        val builder = AuthorizationRequest.Builder(
//            authServiceConfig,
//            Config.CLIENT_ID,
//            ResponseTypeValues.CODE,
//            Uri.parse(Config.URL_AUTH_REDIRECT))
//            .setCodeVerifier(codeVerifier,
//                codeChallenge,
//                Config.CODE_VERIFIER_CHALLENGE_METHOD)
//
//        builder.setScopes(
//            Config.SCOPE_PROFILE,
//            Config.SCOPE_EMAIL,
//            Config.SCOPE_OPENID)
//
//        val request = builder.build()
//
//        val authIntent = authorizationService.getAuthorizationRequestIntent(request)
//    }
//}