package ru.vsu.forum.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.vsu.forum.api.ForumApi
import ru.vsu.forum.model.PagedList
import ru.vsu.forum.model.Topic

class TopicPagingSource(
    private val forumApi: ForumApi,
    private val searchQuery: String
) : PagingSource<Int, Topic>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Topic> {
        val page = params.key ?: 1
        return try {
            val response = forumApi.getTopics(page, params.loadSize, searchQuery)
            if (response.isSuccessful) {
                val topics = response.body() ?: PagedList(listOf(), 0)
                LoadResult.Page(
                    data = topics.items,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (topics.count < params.loadSize) null else page + 1
                )
            } else {
                LoadResult.Error(Exception("Failed to load topics"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Topic>): Int? {
        return state.anchorPosition
    }
}

