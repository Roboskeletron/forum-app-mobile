package ru.vsu.forum.data.source

import ru.vsu.forum.api.ForumApi
import ru.vsu.forum.data.model.SendMessageRequest
import ru.vsu.forum.utils.Config
import java.util.UUID

class MessageRepository(private val apiService: ForumApi) {

    suspend fun sendMessage(topicId: UUID, text: String): Result<UUID> {
        return try {
            val response = apiService.sendMessage(
                topicId = topicId,
                sendMessageRequest = SendMessageRequest(topicId = topicId, text = text),
                token = "Bearer ${Config.AUTH_TOKEN}"
            )
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to send message: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
