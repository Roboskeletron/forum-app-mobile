package ru.vsu.forum

import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter

object BindingAdapter {

    @BindingAdapter("selection_helper", "item_id", requireAll = true)
    @JvmStatic
    fun <Message> handleSelection(
        view: View,
        selectionHelper: MessageSelectionHelper,
        itemId: Int
    ) {
        val isSelected = selectionHelper.isSelected(itemId)

        val color = if (isSelected) {
            R.color.blue
        } else {
            android.R.color.transparent
        }
        view.setBackgroundColor(ContextCompat.getColor(view.context, color))
    }
}