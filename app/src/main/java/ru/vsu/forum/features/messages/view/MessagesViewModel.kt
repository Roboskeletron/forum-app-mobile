package ru.vsu.forum.features.messages.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import ru.vsu.forum.features.common.data.ForumService
import ru.vsu.forum.features.messages.data.MessagePagingSource
import ru.vsu.forum.features.messages.data.MessageRepository
import java.util.UUID

class MessagesViewModel(
    private val messageRepository: MessageRepository,
    val topicId: UUID,
    val topicTitle: String
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")

    val searchQuery: StateFlow<String> = _searchQuery

    val messagesFlow = searchQuery.flatMapLatest { query ->
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { MessagePagingSource(messageRepository, topicId, query) }
        ).flow.cachedIn(viewModelScope)
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
}
