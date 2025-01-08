package ru.vsu.forum.features.messages.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import ru.vsu.forum.databinding.MessageItemBinding
import ru.vsu.forum.features.auth.domain.UserProvider
import ru.vsu.forum.features.common.domain.ImageService
import ru.vsu.forum.features.messages.data.CommentRepository
import ru.vsu.forum.features.messages.data.MessageRepository
import ru.vsu.forum.features.messages.models.Author
import ru.vsu.forum.features.messages.models.Message
import ru.vsu.forum.features.profile.data.UserRepository
import java.util.UUID

class MessageAdapter(
    val fragment: Fragment,
    val userProvider: UserProvider,
    val messageRepository: MessageRepository,
    val userRepository: UserRepository,
    val commentRepository: CommentRepository
) : PagingDataAdapter<Message, MessageAdapter.MessageViewHolder>(MessageDiffCallback()) {
    private var selectedPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = MessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = getItem(position)
        if (message != null) {
            holder.bind(message)
        }

        holder.itemView.setOnLongClickListener {
            selectedPosition = holder.bindingAdapterPosition
            false
        }
    }

    fun getSelectedMessage(): Message? {
        return getItem(selectedPosition)
    }

    inner class MessageViewHolder(private val binding: MessageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val imageService = ImageService(userRepository)

        fun bind(message: Message) {
            binding.message = message

            userProvider.user.observe(fragment.viewLifecycleOwner) {
                if (it?.id == message.author.id) {
                    fragment.registerForContextMenu(binding.root)
                } else {
                    fragment.unregisterForContextMenu(binding.root)
                }

                binding.likeButton.isEnabled = it != null
            }

            binding.executePendingBindings()

            binding.likeButton.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
                fragment.lifecycleScope.launch {
                    val newMessage = if (isChecked)
                        messageRepository.likeMessage(message.id)
                    else messageRepository.dislikeMessage(message.id)

                    binding.message = newMessage
                }
                binding.executePendingBindings()
            }

            binding.avatarImageView.setOnClickListener {
                val direction = getViewProfileNavDirection(message.author.id)

                fragment.findNavController().navigate(direction)
            }

            binding.authorName.setOnClickListener {
                val direction = getViewProfileNavDirection(message.author.id)

                fragment.findNavController().navigate(direction)
            }

            binding.commentButton.setOnClickListener {
                val commentFragment = CommentListDialogFragment(
                    message.id,
                    userProvider,
                    userRepository,
                    commentRepository,
                    fragment as MessagesFragment
                )
                commentFragment.show(fragment.requireActivity().supportFragmentManager, "Comments")
            }

            fetchAuthorAvatar(message.author)
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
                MessagesFragmentDirections.actionNavigationTopicToNavigationProfile()
            else MessagesFragmentDirections.actionNavigationTopicToViewProfileFragment(userId.toString())
    }

    class MessageDiffCallback : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }
    }
}

