package com.marvelsample.app.ui.base.adapter

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T : RecyclerView.ViewHolder, C : Any>(
        clickFunction: (item: C, view: View) -> Unit = { _: C, _: View -> },
        taskDiffCallback: DiffUtil.ItemCallback<C>
) : PageListAdapterWithClickFunction<C, T>(taskDiffCallback, clickFunction) {
}