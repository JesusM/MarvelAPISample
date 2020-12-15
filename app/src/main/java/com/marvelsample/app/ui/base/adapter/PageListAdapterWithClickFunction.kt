package com.marvelsample.app.ui.base.adapter

import android.view.View
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class PageListAdapterWithClickFunction<T : Any, V : RecyclerView.ViewHolder>(
    diffUtilCallback: DiffUtil.ItemCallback<T>,
    private var clickFunction: (item: T, view: View) -> Unit = { _: T, _: View -> }
) :
    PagingDataAdapter<T, V>(diffUtilCallback) {

    override fun onBindViewHolder(holder: V, position: Int) {
        getItem(position)?.let {
            bindItem(holder, it)
            bindClickListener(holder, it)
        }
    }

    open fun bindClickListener(holderView: V, item: T) {
        holderView.itemView.setOnClickListener { view ->
            clickFunction.invoke(item, view)
        }
    }

    abstract fun bindItem(holder: V, item: T)
}