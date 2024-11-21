package ru.vsu.forum.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.vsu.forum.model.Message
import ru.vsu.forum.model.PagedList
import ru.vsu.forum.data.model.SendMessageRequest
import ru.vsu.forum.model.Topic
import java.util.UUID

interface ForumApi {
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
        @Body sendMessageRequest: SendMessageRequest,
        @Header("Authorization") token: String
    ): Response<UUID>
}