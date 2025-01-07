package ru.vsu.forum.features.topics.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import ru.vsu.forum.features.topics.data.TopicPagingSource
import ru.vsu.forum.features.topics.data.TopicRepository
import ru.vsu.forum.features.topics.models.TopicSearchParameters

class TopicsViewModel(private val topicRepository: TopicRepository) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val topicsFlow = searchQuery.flatMapLatest { query ->

        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { TopicPagingSource(topicRepository, parseSearchQuery(query)) }
        ).flow.cachedIn(viewModelScope)
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    private fun parseSearchQuery(searchQuery: String?): TopicSearchParameters = searchQuery?.let { query ->
        val searchParams = mutableMapOf<String, String>()

        query.split(";").forEach()
        { part ->
            val keyValue = part.split(":").map { it.trim() }
            if (keyValue.size == 2) {
                val key = keyValue[0].lowercase()
                val value = keyValue[1]

                when (key) {
                    "author" -> searchParams["author"] = value
                    "title" -> searchParams["title"] = value
                    "content" -> searchParams["content"] = value
                }
            }
        }

        if (searchParams.isEmpty()) {
            searchParams["content"] = query
        }

        return TopicSearchParameters(
            searchParams["author"],
            searchParams["title"],
            searchParams["content"]
        )
    } ?: TopicSearchParameters(null, null, null)
}