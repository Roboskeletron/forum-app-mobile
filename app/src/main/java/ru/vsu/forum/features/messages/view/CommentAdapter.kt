package ru.vsu.forum.features.messages.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import ru.vsu.forum.databinding.CommentItemBinding
import ru.vsu.forum.features.auth.domain.UserProvider
import ru.vsu.forum.features.common.domain.ImageService
import ru.vsu.forum.features.messages.models.Author
import ru.vsu.forum.features.messages.models.Comment
import ru.vsu.forum.features.profile.data.UserRepository
import java.util.UUID

class CommentAdapter(
    val fragment: CommentListDialogFragment,
    val userProvider: UserProvider,
    val userRepository: UserRepository
) : PagingDataAdapter<Comment, CommentAdapter.CommentViewHolder>(CommentDiffCallback()) {
    private var selectedPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommentViewHolder {
        val binding = CommentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: CommentViewHolder,
        position: Int
    ) {
        val comment = getItem(position)
        if (comment != null) {
            holder.bind(comment)
        }

        holder.itemView.setOnLongClickListener {
            selectedPosition = holder.bindingAdapterPosition
            false
        }
    }

    fun getSelectedComment() : Comment? {
        return getItem(selectedPosition)
    }

    inner class CommentViewHolder(
        private val binding: CommentItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private val imageService = ImageService(userRepository)

        fun bind(comment: Comment) {
            binding.comment = comment

            userProvider.user.observe(fragment.viewLifecycleOwner) {
                if (it?.id == comment.author.id) {
                    fragment.registerForContextMenu(binding.root)
                } else {
                    fragment.unregisterForContextMenu(binding.root)
                }
            }

            binding.executePendingBindings()

            binding.avatarImageView.setOnClickListener {
                val direction = getViewProfileNavDirection(comment.author.id)

                fragment.findNavController().navigate(direction)
            }

            binding.authorName.setOnClickListener {
                val direction = getViewProfileNavDirection(comment.author.id)

                fragment.findNavController().navigate(direction)
            }

            fetchAuthorAvatar(comment.author)
        }

        private fun fetchAuthorAvatar(author: Author) {
            imageService.avatar.observe(fragment.viewLifecycleOwner) {
                if (it != null) {
                    binding.avatarImageView.setImageBitmap(it)
                }
            }

            fragment.lifecycleScope.launch {
                imageService.fetchAvatar(author.id)
            }
        }

        private fun getViewProfileNavDirection(userId: UUID) : NavDirections =
            if (userId == userProvider.user.value?.id)
                CommentListDialogFragmentDirections.actionCommentListDialogFragmentToNavigationProfile()
            else CommentListDialogFragmentDirections.actionCommentListDialogFragmentToViewProfileFragment(userId.toString())
    }

    class CommentDiffCallback : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(
            oldItem: Comment,
            newItem: Comment
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Comment,
            newItem: Comment
        ): Boolean {
            return oldItem == newItem
        }

    }
}