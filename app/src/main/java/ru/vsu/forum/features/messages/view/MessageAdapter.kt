package ru.vsu.forum.features.messages.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.vsu.forum.databinding.MessageItemBinding
import ru.vsu.forum.features.auth.domain.UserProvider
import ru.vsu.forum.features.messages.models.Message

class MessageAdapter(
    val fragment: Fragment,
    val userProvider: UserProvider
) : PagingDataAdapter<Message, MessageAdapter.MessageViewHolder>(MessageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = MessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = getItem(position)
        if (message != null) {
            holder.bind(message)
        }
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
            }

            binding.root.setOnClickListener {
                binding.root.showContextMenu()
                true
            }

            binding.executePendingBindings()
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

