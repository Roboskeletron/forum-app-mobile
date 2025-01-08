package ru.vsu.forum.features.messages.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import ru.vsu.forum.features.messages.data.CommentPagingSource
import ru.vsu.forum.features.messages.data.CommentRepository
import java.util.UUID

class CommentListDialogViewModel(
    val commentRepository: CommentRepository,
    val messageId: UUID
) : ViewModel() {
    val comment = MutableLiveData<String?>(null)

    val commentsFlow = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { CommentPagingSource(commentRepository, messageId) }
    ).flow.cachedIn(viewModelScope)
}