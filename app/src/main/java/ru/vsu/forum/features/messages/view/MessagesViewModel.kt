package ru.vsu.forum.features.messages.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import ru.vsu.forum.features.common.data.ForumService
import ru.vsu.forum.data.source.MessagePagingSource
import java.lang.IllegalArgumentException
import java.util.UUID
import kotlin.jvm.java

class TopicViewModel(
    val forumService: ForumService,
    val topicId: UUID
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val messagesFlow = searchQuery.flatMapLatest { query ->
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { MessagePagingSource(forumService, topicId, query) }
        ).flow.cachedIn(viewModelScope)
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
}

class TopicViewModelFactory(
    private val forumService: ForumService,
    private val topicId: UUID
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(TopicViewModel::class.java)) {
            TopicViewModel(forumService, topicId) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
