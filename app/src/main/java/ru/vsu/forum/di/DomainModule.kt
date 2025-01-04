package ru.vsu.forum.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.vsu.forum.features.auth.domain.UserProvider
import ru.vsu.forum.features.auth.domain.UserProviderImpl

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ru.vsu.forum.shared.preferences")

val domainModule = module {
    single<UserProvider> {
        UserProviderImpl(androidContext().dataStore, get(), get())
    }
}