package com.marvelsample.app.ui.characterdetails.comics.adapter

import androidx.recyclerview.widget.DiffUtil
import com.marvelsample.app.ui.characterdetails.comics.ComicListItem

class ComicListItemTaskDiffCallback : DiffUtil.ItemCallback<ComicListItem>() {
    override fun areItemsTheSame(oldItem: ComicListItem, newItem: ComicListItem): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ComicListItem, newItem: ComicListItem): Boolean =
        oldItem.id == newItem.id
}