package com.marvelsample.app.ui.characterdetails.comics.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.marvelsample.app.R
import com.marvelsample.app.ui.base.adapter.BaseAdapter
import com.marvelsample.app.ui.characterdetails.comics.ComicListItem
import com.marvelsample.app.ui.utils.imageloader.ImageLoader
import com.marvelsample.app.ui.utils.inflateView

open class ComicsListAdapter(
    private val imageLoader: ImageLoader,
    difUtilCallback: DiffUtil.ItemCallback<ComicListItem>,
    private val layoutRes: Int = R.layout.detail_screen_comic_list_item,
    clickFunction: (item: ComicListItem, view: View) -> Unit
) : BaseAdapter<ComicListItemViewHolder, ComicListItem>(clickFunction, difUtilCallback) {

    override fun bindItem(holder: ComicListItemViewHolder, item: ComicListItem) {
        holder.bindItem(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicListItemViewHolder {
        return ComicListItemViewHolder(parent.context.inflateView(layoutRes, parent), imageLoader)
    }
}