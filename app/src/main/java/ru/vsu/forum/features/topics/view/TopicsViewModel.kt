package ru.vsu.forum.features.topics.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import ru.vsu.forum.features.common.data.ForumService
import ru.vsu.forum.features.topics.data.TopicPagingSource
import ru.vsu.forum.features.topics.data.TopicRepository

class TopicsViewModel(private val topicRepository: TopicRepository) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val topicsFlow = searchQuery.flatMapLatest { query ->
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { TopicPagingSource(topicRepository, query) }
        ).flow.cachedIn(viewModelScope)
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
}