package com.marvelsample.app.ui.characterslist.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.marvelsample.app.R
import com.marvelsample.app.ui.base.adapter.BaseAdapter
import com.marvelsample.app.ui.characterslist.ListItem
import com.marvelsample.app.ui.utils.imageloader.ImageLoader
import com.marvelsample.app.ui.utils.inflateView

open class CharactersListAdapter(
    private val imageLoader: ImageLoader,
    difUtilCallback: DiffUtil.ItemCallback<ListItem>,
    private val layoutRes: Int = R.layout.grid_item,
    clickFunction: (item: ListItem, view: View) -> Unit
) :
    BaseAdapter<ListItemViewHolder, ListItem>(clickFunction, difUtilCallback) {

    override fun bindItem(holder: ListItemViewHolder, item: ListItem) {
        holder.bindItem(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        return ListItemViewHolder(parent.context.inflateView(layoutRes, parent), imageLoader)
    }
}