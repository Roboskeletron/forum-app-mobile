package ru.vsu.forum.features.messages.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.vsu.forum.features.messages.models.Message
import ru.vsu.forum.features.messages.models.MessageSearchParameters
import java.util.UUID

class MessagePagingSource(
    private val messageRepository: MessageRepository,
    private val topicId: UUID,
    private val searchParameters: MessageSearchParameters
) : PagingSource<Int, Message>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Message> {
        val page = params.key ?: 1
        val messages = messageRepository.getMessages(topicId, page, params.loadSize, searchParameters)

        return LoadResult.Page(
            data = messages,
            prevKey = if (page == 1) null else page - 1,
            nextKey = if (messages.size < params.loadSize) null else page + 1
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Message>): Int? {
        return state.anchorPosition
    }
}

