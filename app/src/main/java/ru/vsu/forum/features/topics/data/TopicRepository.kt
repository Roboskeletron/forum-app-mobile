package ru.vsu.forum.features.topics.data

import android.util.Log
import ru.vsu.forum.features.common.data.ForumService
import ru.vsu.forum.features.topics.models.CreateTopicModel
import ru.vsu.forum.features.topics.models.Topic
import java.util.UUID

interface TopicRepository {
    suspend fun getTopics(pageIndex: Int, pageSize: Int, searchQuery: String?) : List<Topic>
    suspend fun createTopic(title: String, description: String? = null) : UUID?
}

class TopicRepositoryImpl(private val forumService: ForumService) : TopicRepository{
    override suspend fun getTopics(
        pageIndex: Int,
        pageSize: Int,
        searchQuery: String?
    ): List<Topic> {
        try {
            val response = forumService.getTopics(pageIndex, pageSize, searchQuery)
            return response.body()?.items ?: listOf()
        }
        catch (e: Exception){
            Log.e(TopicRepositoryImpl::class.qualifiedName, "Unable to get topics", e)
            return listOf()
        }
    }

    override suspend fun createTopic(title: String, description: String?) : UUID? {
        try {
            val response = forumService.createTopic(CreateTopicModel(title, description))
            return response.body()
        }
        catch (e: Exception){
            Log.e(TopicRepositoryImpl::class.qualifiedName, "Unable to create topic", e)
            return null
        }
    }
}