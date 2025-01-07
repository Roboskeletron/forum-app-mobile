package ru.vsu.forum.features.topics.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import ru.vsu.forum.databinding.TopicItemBinding
import ru.vsu.forum.features.auth.domain.UserProvider
import ru.vsu.forum.features.topics.data.TopicRepository
import ru.vsu.forum.features.topics.models.Topic

class TopicAdapter(
    val fragment: TopicsFragment,
    val userProvider: UserProvider,
    val topicRepository: TopicRepository
) : PagingDataAdapter<Topic, TopicAdapter.TopicViewHolder>(TopicDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val binding = TopicItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TopicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        val topic = getItem(position)
        if (topic != null) {
            holder.bind(topic)
        }
    }

    inner class TopicViewHolder(private val binding: TopicItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(topic: Topic) {
            binding.topic = topic

            userProvider.user.observe(fragment.viewLifecycleOwner) {
//                if (it?.id == message.author.id) {
//                    fragment.registerForContextMenu(binding.root)
//                } else {
//                    fragment.unregisterForContextMenu(binding.root)
//                }

                binding.likeButton.isEnabled = it != null
            }

            binding.executePendingBindings()

            binding.root.setOnClickListener{
                val action = TopicsFragmentDirections
                    .actionNavigationHomeToNavigationTopic(topic.id.toString(), topic.title)
                fragment.findNavController().navigate(action)
            }

            binding.likeButton.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
                fragment.lifecycleScope.launch {
                    val newTopic = if (isChecked)
                        topicRepository.likeTopic(topic.id)
                        else topicRepository.dislikeTopic(topic.id)
                    binding.topic = newTopic
                }
                binding.executePendingBindings()
            }
        }
    }

    class TopicDiffCallback : DiffUtil.ItemCallback<Topic>() {
        override fun areItemsTheSame(oldItem: Topic, newItem: Topic): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Topic, newItem: Topic): Boolean {
            return oldItem == newItem
        }
    }
}

