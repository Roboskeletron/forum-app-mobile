package ru.vsu.forum.ui.topic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import ru.vsu.forum.api.ForumApi
import ru.vsu.forum.data.source.MessagePagingSource
import java.util.UUID

class TopicViewModel(
    val forumApi: ForumApi,
    val topicId: UUID
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val messagesFlow = searchQuery.flatMapLatest { query ->
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { MessagePagingSource(forumApi, topicId, query) }
        ).flow.cachedIn(viewModelScope)
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
}

class TopicViewModelFactory(
    private val forumApi: ForumApi,
    private val topicId: UUID
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(TopicViewModel::class.java)) {
            TopicViewModel(forumApi, topicId) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
