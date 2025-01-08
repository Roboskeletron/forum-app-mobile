package ru.vsu.forum.features.messages.view

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.vsu.forum.R
import ru.vsu.forum.databinding.FragmentCommentListDialogBinding
import ru.vsu.forum.features.auth.domain.UserProvider
import ru.vsu.forum.features.messages.data.CommentRepository
import ru.vsu.forum.features.messages.models.Comment
import ru.vsu.forum.features.profile.data.UserRepository
import java.util.UUID

class CommentListDialogFragment(
    val messageId: UUID,
    val userProvider: UserProvider,
    val userRepository: UserRepository,
    val commentRepository: CommentRepository,
    val messagesFragment: MessagesFragment
) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCommentListDialogBinding
    private lateinit var commentAdapter: CommentAdapter
    private val viewModel = CommentListDialogViewModel(commentRepository, messageId)

    private var sendCommentStrategy: suspend () -> Unit = {
        commentRepository.sendComment(viewModel.messageId, viewModel.comment.value!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommentListDialogBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        commentAdapter = CommentAdapter(
            this,
            userProvider,
            userRepository)

        binding.commentsRecyclerView.adapter = commentAdapter

        lifecycleScope.launch {
            viewModel.commentsFlow.collectLatest {
                commentAdapter.submitData(it)
            }
        }

        binding.commentsTextInputLayout.setEndIconOnClickListener {
            val commentText = viewModel.comment.value

            if (commentText.isNullOrBlank()) return@setEndIconOnClickListener

            lifecycleScope.launch {
                sendCommentStrategy()
                binding.messagesTextInput.text?.clear()
                viewModel.refreshComments()
                viewModel.commentsFlow.collectLatest {
                    commentAdapter.submitData(it)
                }
            }

            sendCommentStrategy = {
                commentRepository.sendComment(viewModel.messageId, viewModel.comment.value!!)
            }
        }

        return binding.root
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater: MenuInflater = MenuInflater(v.context)
        inflater.inflate(R.menu.context_menu, menu)

        menu.findItem(R.id.action_edit)?.setOnMenuItemClickListener {
            onContextItemSelected(it)
        }
        menu.findItem(R.id.action_delete)?.setOnMenuItemClickListener {
            onContextItemSelected(it)
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val selectedComment = commentAdapter.getSelectedComment() ?: return super.onContextItemSelected(item)

        return when (item.itemId) {
            R.id.action_edit -> {
                editComment(selectedComment)
                true
            }

            R.id.action_delete -> {
                deleteComment(selectedComment)
                true
            }

            else -> super.onContextItemSelected(item)
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        messagesFragment.lifecycleScope.launch {
            messagesFragment.refreshMessages()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        messagesFragment.lifecycleScope.launch {
            messagesFragment.refreshMessages()
        }
    }

    private fun editComment(comment: Comment) {
        binding.messagesTextInput.setText(comment.text)

        sendCommentStrategy = {
            commentRepository.updateComment(comment.id, viewModel.comment.value!!)
        }
    }

    private fun deleteComment(comment: Comment) {
        lifecycleScope.launch {
            commentRepository.deleteComment(comment.id)
            viewModel.refreshComments()
            viewModel.commentsFlow.collectLatest {
                commentAdapter.submitData(it)
            }
        }
    }
}