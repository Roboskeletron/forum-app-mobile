package ru.vsu.forum.features.messages.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import ru.vsu.forum.databinding.MessageItemBinding
import ru.vsu.forum.features.auth.domain.UserProvider
import ru.vsu.forum.features.common.domain.decodeBase64ToBitmap
import ru.vsu.forum.features.messages.data.MessageRepository
import ru.vsu.forum.features.messages.models.Message

class MessageAdapter(
    val fragment: Fragment,
    val userProvider: UserProvider,
    val messageRepository: MessageRepository
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
        fun bind(message: Message) {
            binding.message = message

            userProvider.user.observe(fragment.viewLifecycleOwner) {
                if (it?.id == message.author.id) {
                    fragment.registerForContextMenu(binding.root)
                }
                else {
                    fragment.unregisterForContextMenu(binding.root)
                }

                binding.likeButton.isEnabled = it != null
            }

            if (message.author.avatar != null) {
                decodeBase64ToBitmap(message.author.avatar)?.also {
                    binding.avatarImageView.setImageBitmap(it)
                }
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
        }
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

