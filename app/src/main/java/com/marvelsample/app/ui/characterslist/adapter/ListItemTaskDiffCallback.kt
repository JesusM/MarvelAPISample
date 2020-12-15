package com.marvelsample.app.ui.characterslist.adapter

import androidx.recyclerview.widget.DiffUtil
import com.marvelsample.app.ui.characterslist.ListItem

class ListItemTaskDiffCallback : DiffUtil.ItemCallback<ListItem>() {
    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
        oldItem.id == newItem.id
}