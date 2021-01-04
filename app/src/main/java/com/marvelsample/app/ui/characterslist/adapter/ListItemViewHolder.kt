package com.marvelsample.app.ui.characterslist.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.marvelsample.app.R
import com.marvelsample.app.ui.characterslist.ListItem
import com.marvelsample.app.ui.utils.imageloader.ImageLoader
import com.marvelsample.app.ui.utils.imageloader.loadImageAfterMeasure
import com.marvelsample.app.ui.utils.launchUI
import com.marvelsample.app.ui.utils.textPaletteAsync

open class ListItemViewHolder(
    private val rootView: View,
    private val imageLoader: ImageLoader
) : RecyclerView.ViewHolder(rootView) {
    private val title = rootView.findViewById<TextView>(R.id.text)
    private val image = rootView.findViewById<ImageView>(R.id.image)

    open fun bindItem(item: ListItem) {
        rootView.tag = item
        title.apply {
            text = item.name
            setBackgroundColor(rootView.context.getColor(R.color.colorOnPrimary))
            setTextColor(rootView.context.getColor(R.color.primaryTextColor))
        }

        item.image?.let { imageUrl ->
            image.loadImageAfterMeasure(imageLoader, imageUrl, null, { bitmap ->
                bitmap?.let { image ->
                    launchUI {
                        val textSwatch = image.textPaletteSync()
                        val backgroundColor = textSwatch?.background
                            ?: rootView.context.getColor(R.color.backgroundLight)
                        val textColor = textSwatch?.primaryText
                            ?: rootView.context.getColor(R.color.primaryTextColor)

                        title.setBackgroundColor(backgroundColor)
                        title.setTextColor(textColor)
                    }
                }
            })

            image.transitionName = "thumb${item.id}"
        }
        title.transitionName = "name${item.id}"
    }
}