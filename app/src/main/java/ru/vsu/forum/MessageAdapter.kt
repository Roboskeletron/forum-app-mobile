package ru.vsu.forum

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import ru.vsu.forum.databinding.MessageItemBinding

class MessageAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val mSelectionHelper: MessageSelectionHelper? = null,
    private val onClick: ((Message) -> Unit)? = null
): RecyclerView.Adapter<MessageAdapter.ViewHolder>() {
    private val items = mutableListOf<Message>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<MessageItemBinding>(inflater, R.layout.message_item, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.onBind(item)
    }

    inner class ViewHolder(
        private val binding: MessageItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: Message) {
            binding.apply {
                setVariable(BR.item, item)
                setVariable(BR.selectionHelper, mSelectionHelper)

                root.setOnClickListener {
                    mSelectionHelper?.handleItem(item)
                    onClick?.invoke(item)
                }
                lifecycleOwner = this@MessageAdapter.lifecycleOwner
            }
            binding.executePendingBindings()
        }
    }
}