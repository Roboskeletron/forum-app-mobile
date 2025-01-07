package ru.vsu.forum.features.messages.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import org.koin.java.KoinJavaComponent.inject
import ru.vsu.forum.features.auth.domain.UserProvider
import ru.vsu.forum.features.common.data.ForumService
import ru.vsu.forum.features.messages.data.MessagePagingSource
import ru.vsu.forum.features.messages.data.MessageRepository
import ru.vsu.forum.features.messages.models.MessageSearchParameters
import java.util.UUID

class MessagesViewModel(
    private val messageRepository: MessageRepository,
    val topicId: UUID,
    val topicTitle: String
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")

    val searchQuery: StateFlow<String> = _searchQuery
    val message = MutableLiveData<String?>(null)

    val messagesFlow = searchQuery.flatMapLatest { query ->
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { MessagePagingSource(messageRepository, topicId, parseSearchQuery(query)) }
        ).flow.cachedIn(viewModelScope)
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    private fun parseSearchQuery(searchQuery: String?): MessageSearchParameters = searchQuery?.let { query ->
        val searchParams = mutableMapOf<String, String>()

        query.split(";").forEach()
        { part ->
            val keyValue = part.split(":").map { it.trim() }
            if (keyValue.size == 2) {
                val key = keyValue[0].lowercase()
                val value = keyValue[1]

                when (key) {
                    "author" -> searchParams["author"] = value
                    "content" -> searchParams["content"] = value
                }
            }
        }

        if (searchParams.isEmpty()) {
            searchParams["content"] = query
        }

        return MessageSearchParameters(
            searchParams["author"],
            searchParams["content"]
        )
    } ?: MessageSearchParameters(null, null)
}
