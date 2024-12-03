package ru.vsu.forum.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.vsu.forum.features.topics.view.TopicsViewModel
import ru.vsu.forum.features.profile.ProfileViewModel
import ru.vsu.forum.features.messages.view.TopicViewModel

val appModule = module {
    viewModelOf(::TopicsViewModel)
    viewModelOf(::TopicViewModel)
    viewModelOf(::ProfileViewModel)
}