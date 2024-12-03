package ru.vsu.forum.features.topics.data

import android.util.Log
import ru.vsu.forum.features.common.data.ForumService
import ru.vsu.forum.features.topics.models.Topic

interface TopicRepository {
    suspend fun getTopics(pageIndex: Int, pageSize: Int, searchQuery: String?) : List<Topic>
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
}