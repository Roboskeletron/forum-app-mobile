package ru.vsu.forum

import androidx.databinding.BaseObservable

class MessageSelectionHelper:BaseObservable() {
    private val selectedItems = mutableMapOf<Int, Message>()

    fun handleItem(item: Message) {
        if (selectedItems[item.id] == null) {
            selectedItems[item.id] = item
        } else {
            selectedItems.remove(item.id)
        }

        notifyChange()
    }
    fun isSelected(id: Int): Boolean = selectedItems.containsKey(id)
    fun getSelectedItems(): List<Message> = selectedItems.values.toList()
    fun getSelectedItemsSize(): Int = selectedItems.size
}