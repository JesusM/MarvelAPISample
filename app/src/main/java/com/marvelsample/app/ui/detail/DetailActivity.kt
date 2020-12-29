package com.marvelsample.app.ui.detail

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.marvelsample.app.R
import com.marvelsample.app.core.repository.model.characters.Character
import com.marvelsample.app.core.repository.model.fullPath
import com.marvelsample.app.databinding.DetailActivityBinding
import com.marvelsample.app.ui.base.model.Result
import com.marvelsample.app.ui.utils.imageloader.CoilImageLoader
import com.marvelsample.app.ui.utils.launchUI
import com.marvelsample.app.ui.utils.loadImageAfterMeasure
import com.marvelsample.app.ui.utils.textPaletteAsync
import org.kodein.di.*
import org.kodein.di.android.closestDI
import org.kodein.di.android.retainedDI

class DetailActivity : AppCompatActivity(), DIAware {

    private val activityModule = DI.Module("itemDetail") {
        bind("itemDetailViewModelFactory") from factory { activity: DetailActivity ->
            val viewModelFactory = object : AbstractSavedStateViewModelFactory(activity, null) {
                override fun <T : ViewModel?> create(
                    key: String,
                    modelClass: Class<T>,
                    handle: SavedStateHandle
                ): T {
                    return DetailViewModel(useCase = instance()) as T
                }

            }
            viewModelFactory
        }
    }

    private val _parentKodein by closestDI()
    override val di by retainedDI {
        extend(_parentKodein)
        import(activityModule)
    }

    companion object {
        const val ITEM_ID_ARG: String = "ITEM_ID_ARG"
        const val ITEM_SHARE_THUMB_ARG: String = "ITEM_SHARE_THUMB_ARG"
        const val ITEM_SHARE_TITLE_ARG: String = "ITEM_SHARE_TITLE_ARG"
    }

    private val viewModelFactory: AbstractSavedStateViewModelFactory by instance(
        "itemDetailViewModelFactory",
        arg = this
    )
    private val viewModel: DetailViewModel by viewModels {
        viewModelFactory
    }

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
        character: Character,
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
            detailActivityHeaderImage.loadImageAfterMeasure(
                CoilImageLoader(this@DetailActivity),
                character.thumbnail.fullPath(),
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

    private fun bindError(it: Result.Error, binding: DetailActivityBinding) {
        binding.apply {
            detailActivityProgress.visibility = View.GONE
            detailActivityCharacterName.text = it.error.toString()
        }
    }
}