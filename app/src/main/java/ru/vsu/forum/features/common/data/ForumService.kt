package ru.vsu.forum.features.common.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import ru.vsu.forum.features.messages.models.Message
import ru.vsu.forum.features.common.models.PagedList
import ru.vsu.forum.features.profile.models.UpdateProfileModel
import ru.vsu.forum.features.topics.models.UpdateTopicModel
import ru.vsu.forum.features.messages.models.SendMessageRequest
import ru.vsu.forum.features.topics.models.CreateTopicModel
import ru.vsu.forum.features.topics.models.Topic
import ru.vsu.forum.model.RegisterUserModel
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

    @GET("Topics/exists-by-title")
    suspend fun topicExistsByTitle(@Query("title") title: String) : Response<Boolean>

    @PUT("Users/profile")
    suspend fun updateProfile(@Body updateProfileModel: UpdateProfileModel) : Response<Unit>

    @GET("Users/profile/{id}")
    suspend fun getUserById(@Path("id") id: UUID) : Response<User?>

    @GET("Topics/{id}")
    suspend fun getTopicById(@Path("id") id: UUID) : Response<Topic?>

    @PUT("Topics/{id}")
    suspend fun updateTopic(@Path("id") id: UUID, @Body updateTopicModel: UpdateTopicModel) : Response<Unit>

    @GET("Users/exists-by-email")
    suspend fun userExistsByEmail(@Query("email") email: String) : Response<Boolean>

    @GET("Users/exists-by-username")
    suspend fun userExistsByUsername(@Query("username") username: String) : Response<Boolean>

    @POST("Users")
    suspend fun registerUser(@Body registerUserModel: RegisterUserModel) : Response<UInt>
}