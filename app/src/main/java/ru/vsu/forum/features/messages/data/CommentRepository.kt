package ru.vsu.forum.features.messages.data

import ru.vsu.forum.features.messages.models.Comment
import java.util.UUID

interface CommentRepository {
    suspend fun getComments(messageId: UUID, pageIndex: Int, pageSize: Int) : List<Comment>
}