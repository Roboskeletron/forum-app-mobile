package ru.vsu.forum.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.vsu.forum.features.messages.data.CommentRepository
import ru.vsu.forum.features.messages.data.CommentRepositoryImpl
import ru.vsu.forum.features.messages.data.MessageRepository
import ru.vsu.forum.features.messages.data.MessageRepositoryImpl
import ru.vsu.forum.features.profile.data.UserRepository
import ru.vsu.forum.features.profile.data.UserRepositoryImpl
import ru.vsu.forum.features.topics.data.TopicRepository
import ru.vsu.forum.features.topics.data.TopicRepositoryImpl

val dataModule = module {
    singleOf(::TopicRepositoryImpl)
        .bind<TopicRepository>()
    singleOf(::MessageRepositoryImpl)
        .bind<MessageRepository>()
    singleOf(::UserRepositoryImpl)
        .bind<UserRepository>()
    singleOf(::CommentRepositoryImpl)
        .bind<CommentRepository>()
}