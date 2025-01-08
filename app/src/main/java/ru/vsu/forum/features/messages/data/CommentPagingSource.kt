package ru.vsu.forum.features.messages.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.vsu.forum.features.messages.models.Comment
import java.util.UUID

class CommentPagingSource(
    val commentRepository: CommentRepository,
    val messageId: UUID
) : PagingSource<Int, Comment>()  {
    override fun getRefreshKey(state: PagingState<Int, Comment>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Comment> {
        val page = params.key ?: 1
        val comments = commentRepository.getComments(messageId, page, params.loadSize)

        return LoadResult.Page(
            data = comments,
            prevKey = if (page == 1) null else page - 1,
            nextKey = (page + 1).takeIf { comments.size < params.loadSize }
        )
    }
}