package ru.vsu.forum.di

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.vsu.forum.features.auth.data.AuthInterceptor
import ru.vsu.forum.features.auth.data.Keycloak
import ru.vsu.forum.features.auth.domain.AuthManager
import ru.vsu.forum.features.common.data.ForumService
import ru.vsu.forum.utils.Config
import ru.vsu.forum.utils.Config.BASE_URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.concurrent.TimeUnit

fun provideHttpClient(authManager: AuthManager): OkHttpClient = OkHttpClient
    .Builder()
    .readTimeout(60, TimeUnit.SECONDS)
    .connectTimeout(60, TimeUnit.SECONDS)
    .addNetworkInterceptor(AuthInterceptor(authManager))
    .authenticator(AuthInterceptor(authManager))
    .build()

fun provideConverterFactory(): GsonConverterFactory = GsonConverterFactory.create(
    GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, JsonDeserializer { json, _, _ ->
            try {
                LocalDateTime.parse(json.asString, DateTimeFormatter.ISO_ZONED_DATE_TIME)
            }
            catch (e: DateTimeParseException){
                null
            }
        })
        .create()
)

fun provideRetrofit(
    okHttpClient: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory
): Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .addConverterFactory(gsonConverterFactory)
    .build()

fun provideService(retrofit: Retrofit): ForumService =
    retrofit.create(ForumService::class.java)

fun provideKeycloakHttpClient(): OkHttpClient = OkHttpClient
    .Builder()
    .readTimeout(60, TimeUnit.SECONDS)
    .connectTimeout(60, TimeUnit.SECONDS)
    .build()

fun provideKeycloakConverterFactory(): GsonConverterFactory = GsonConverterFactory.create(
    GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()
)

fun provideKeycloakRetrofit(): Retrofit = Retrofit.Builder()
    .baseUrl(Config.KEYCLOAK_BASE_URL)
    .client(provideKeycloakHttpClient())
    .addConverterFactory(provideKeycloakConverterFactory())
    .build()

fun provideKeycloakService() : Keycloak =
    provideKeycloakRetrofit().create(Keycloak::class.java)

val networkModule = module {
    single { provideHttpClient(get()) }
    single { provideConverterFactory() }
    single { provideRetrofit(get(), get()) }
    single { provideService(get()) }
    single { provideKeycloakService() }
}