package ru.vsu.forum.features.common.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.vsu.forum.features.messages.models.Message
import ru.vsu.forum.features.common.models.PagedList
import ru.vsu.forum.features.messages.models.SendMessageRequest
import ru.vsu.forum.features.topics.models.CreateTopicModel
import ru.vsu.forum.features.topics.models.Topic
import ru.vsu.forum.model.User
import java.util.UUID

interface ForumService {
    @GET("Topics")
    suspend fun getTopics(
        @Query("PageNumber") page: Int,
        @Query("PageSize") pageSize: Int,
        @Query("search") search: String? = null
    ): Response<PagedList<Topic>>

    @GET("Topics/{id}/messages")
    suspend fun getMessages(
        @Path("id") topicId: UUID,
        @Query("PageNumber") page: Int,
        @Query("PageSize") pageSize: Int,
        @Query("search") search: String? = null
    ): Response<PagedList<Message>>

    @POST("Topics/{topicId}/messages")
    suspend fun sendMessage(
        @Path("topicId") topicId: UUID,
        @Body sendMessageRequest: SendMessageRequest
    ): Response<UUID>

    @GET("Users/profile")
    suspend fun getProfile() : Response<User>

    @POST("Topics")
    suspend fun createTopic(@Body createTopicModel: CreateTopicModel) : Response<UUID?>
}