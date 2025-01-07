package ru.vsu.forum.features.topics.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.vsu.forum.features.topics.models.Topic
import ru.vsu.forum.features.topics.models.TopicSearchParameters

class TopicPagingSource(
    private val topicRepository: TopicRepository,
    private val searchParameters: TopicSearchParameters
) : PagingSource<Int, Topic>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Topic> {
        val page = params.key ?: 1
        val topics = topicRepository.getTopics(
            page,
            params.loadSize,
            searchParameters
        )
        return LoadResult.Page(
            data = topics,
            prevKey = if (page == 1) null else page - 1,
            nextKey = if (topics.size < params.loadSize) null else page + 1
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Topic>): Int? {
        return state.anchorPosition
    }
}

