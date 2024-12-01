package ru.vsu.forum.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.vsu.forum.ui.home.HomeViewModel
import ru.vsu.forum.ui.profile.ProfileViewModel
import ru.vsu.forum.ui.topic.TopicViewModel

val appModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::TopicViewModel)
    viewModelOf(::ProfileViewModel)
}