package ru.vsu.forum.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.vsu.forum.databinding.TopicItemBinding
import ru.vsu.forum.model.Topic
import java.util.UUID

class TopicAdapter(
    private val onTopicClick: (UUID) -> Unit
) : ListAdapter<Topic, TopicAdapter.TopicViewHolder>(TopicDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val binding = TopicItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TopicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        val topic = getItem(position)
        holder.bind(topic)
    }

    inner class TopicViewHolder(private val binding: TopicItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(topic: Topic) {
            binding.topic = topic
            binding.root.setOnClickListener {
                onTopicClick(topic.id)
            }
        }
    }
}

class TopicDiffCallback : DiffUtil.ItemCallback<Topic>() {
    override fun areItemsTheSame(oldItem: Topic, newItem: Topic): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Topic, newItem: Topic): Boolean = oldItem == newItem
}
