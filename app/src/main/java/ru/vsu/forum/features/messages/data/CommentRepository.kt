package ru.vsu.forum.features.messages.data

import android.util.Log
import ru.vsu.forum.features.common.data.ForumService
import ru.vsu.forum.features.messages.models.Comment
import ru.vsu.forum.features.messages.models.SendCommentModel
import ru.vsu.forum.features.messages.models.UpdateCommentModel
import java.util.UUID

interface CommentRepository {
    suspend fun getComments(messageId: UUID, pageIndex: Int, pageSize: Int): List<Comment>
    suspend fun sendComment(messageId: UUID, comment: String) : UUID
    suspend fun deleteComment(id: UUID)
    suspend fun updateComment(id: UUID, comment: String)
}

class CommentRepositoryImpl(val forumService: ForumService) : CommentRepository {
    override suspend fun getComments(
        messageId: UUID,
        pageIndex: Int,
        pageSize: Int
    ): List<Comment> {
        try {
            val comments = forumService.getMessageComments(messageId, pageIndex, pageSize)
            return comments.body()?.items ?: listOf()
        }
        catch (e: Exception) {
            Log.e(CommentRepositoryImpl::class.qualifiedName, "Unable to get comments")
            return listOf()
        }
    }

    override suspend fun sendComment(messageId: UUID, comment: String) : UUID {
        try {
            val response = forumService.sendComment(
                messageId,
                SendCommentModel(messageId, comment)
            )

            return response.body()!!
        } catch (e: Exception) {
            Log.e(CommentRepositoryImpl::class.qualifiedName, "Unable to send comment", e)
            throw e
        }
    }

    override suspend fun deleteComment(id: UUID) {
        try {
            forumService.deleteComment(id)
        } catch (e: Exception) {
            Log.e(CommentRepositoryImpl::class.qualifiedName, "Unable to delete comment", e)
        }
    }

    override suspend fun updateComment(id: UUID, comment: String) {
        try {
            forumService.updateComment(id, UpdateCommentModel(id, comment))
        } catch (e: Exception) {
            Log.e(CommentRepositoryImpl::class.qualifiedName, "Unable to update comment", e)
        }
    }
}