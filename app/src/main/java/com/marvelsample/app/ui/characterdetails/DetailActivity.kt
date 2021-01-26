package com.marvelsample.app.ui.characterdetails

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.marvelsample.app.R
import com.marvelsample.app.databinding.DetailActivityBinding
import com.marvelsample.app.ui.base.model.Result
import com.marvelsample.app.ui.utils.imageloader.CoilImageLoader
import com.marvelsample.app.ui.utils.imageloader.loadImageAfterMeasure
import com.marvelsample.app.ui.utils.launchUI
import com.marvelsample.app.ui.utils.textPaletteAsync
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named

class DetailActivity : AppCompatActivity() {
    companion object {
        const val ITEM_ID_ARG: String = "ITEM_ID_ARG"
        const val ITEM_SHARE_THUMB_ARG: String = "ITEM_SHARE_THUMB_ARG"
        const val ITEM_SHARE_TITLE_ARG: String = "ITEM_SHARE_TITLE_ARG"
    }

    private val viewModel: DetailViewModel by viewModel(named("detailViewModel"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DetailActivityBinding.inflate(layoutInflater).apply {
            setContentView(root)

            viewModel.itemObservable.observe(this@DetailActivity, {
                when (it) {
                    is Result.Error -> {
                        bindError(it, this)
                    }
                    is Result.Success -> {
                        bindItem(it.result, this)
                    }
                    Result.Loading -> {
                        detailActivityProgress.visibility = View.VISIBLE
                    }
                }
            })
            postponeEnterTransition()
            detailActivityHeaderImage.transitionName = intent.getStringExtra(ITEM_SHARE_THUMB_ARG)
            detailActivityCharacterName.transitionName = intent.getStringExtra(ITEM_SHARE_TITLE_ARG)
            viewModel.load(intent.getIntExtra(ITEM_ID_ARG, -1))
        }
    }

    private fun bindItem(
        character: CharacterModel,
        binding: DetailActivityBinding
    ) {
        binding.apply {
            detailActivityProgress.visibility = View.GONE
            detailActivityCharacterName.apply {
                visibility = View.VISIBLE
                text = character.name
            }
            detailActivityCharacterDescription.apply {
                visibility = View.VISIBLE
                text = character.description
            }
            character.image?.let {
                detailActivityHeaderImage.loadImageAfterMeasure(
                    CoilImageLoader(this@DetailActivity),
                    character.image,
                    null,
                    { bitmap ->
                        launchUI {
                            startPostponedEnterTransition()
                        }

                        bitmap?.let { image ->
                            launchUI { coroutineScope ->
                                // Grab the palette from the bitmap loaded.
                                val textSwatch = image.textPaletteAsync(coroutineScope).await()

                                // Once the palette is fetched, use its "text palette" for UI elements.
                                val backgroundColor = textSwatch?.background
                                    ?: detailActivityHeaderImage.context.getColor(R.color.backgroundLight)
                                val textColor = textSwatch?.primaryText
                                    ?: detailActivityHeaderImage.context.getColor(R.color.primaryTextColor)

                                detailActivityCharacterName.apply {
                                    setBackgroundColor(backgroundColor)
                                    setTextColor(textColor)
                                }
                            }
                        }
                    })
            }
        }
    }

    private fun bindError(it: Result.Error, binding: DetailActivityBinding) {
        binding.apply {
            detailActivityProgress.visibility = View.GONE
            detailActivityCharacterName.text = it.error.toString()
        }
    }
}