package ru.vsu.forum.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.vsu.forum.features.topics.data.TopicRepository
import ru.vsu.forum.features.topics.data.TopicRepositoryImpl

val dataModule = module {
    singleOf(::TopicRepositoryImpl)
        .bind<TopicRepository>()
}