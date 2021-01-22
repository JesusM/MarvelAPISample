package com.marvelsample.app.ui.characterdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.transition.TransitionInflater
import com.marvelsample.app.R
import com.marvelsample.app.core.model.base.error.ResourceError
import com.marvelsample.app.databinding.DetailScreenBinding
import com.marvelsample.app.ui.base.model.Result
import com.marvelsample.app.ui.utils.imageloader.CoilImageLoader
import com.marvelsample.app.ui.utils.imageloader.loadImageAfterMeasure
import com.marvelsample.app.ui.utils.launchUI
import com.marvelsample.app.ui.utils.textPaletteAsync
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named

class DetailFragment : Fragment() {

    companion object {
        const val ITEM_ID_ARG: String = "ITEM_ID_ARG"
    }

    private val viewModel: DetailViewModel by viewModel(named("detailViewModel"))

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return DetailScreenBinding.inflate(inflater, container, false).root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        DetailScreenBinding.bind(view).apply {
            val itemId = arguments?.getInt(ITEM_ID_ARG) ?: -1
            detailScreenHeaderImage.transitionName = "thumb$itemId"
            detailScreenCharacterName.transitionName = "title$itemId"
            viewModel.itemObservable.observe(viewLifecycleOwner, {
                when (it) {
                    is Result.Error -> {
                        bindError(it, this)
                    }
                    is Result.Success -> {
                        bindItem(it.result, this)
                    }
                    Result.Loading -> {
                        detailScreenProgress.visibility = View.VISIBLE
                    }
                }
            })
            arguments?.let {
                postponeEnterTransition()
                viewModel.load(itemId)
            }
        }
    }

    private fun bindItem(
        character: CharacterModel,
        binding: DetailScreenBinding
    ) {
        binding.apply {
            detailScreenProgress.isVisible = false
            detailScreenCharacterDescription.isVisible = true
            detailScreenCharacterName.isVisible = true
            detailScreenHeaderImage.isVisible = true

            detailScreenCharacterName.apply {
                isVisible = true
                text = character.name
            }
            detailScreenCharacterDescription.apply {
                isVisible = true
                text = character.description
            }
            character.image?.let {
                detailScreenHeaderImage.loadImageAfterMeasure(
                    CoilImageLoader(requireContext()),
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
                                    ?: detailScreenHeaderImage.context.getColor(R.color.backgroundLight)
                                val textColor = textSwatch?.primaryText
                                    ?: detailScreenHeaderImage.context.getColor(R.color.primaryTextColor)

                                detailScreenCharacterName.apply {
                                    setBackgroundColor(backgroundColor)
                                    setTextColor(textColor)
                                }
                            }
                        }
                    })
            }
        }
    }

    private fun bindError(it: Result.Error, binding: DetailScreenBinding) {
        binding.apply {
            detailScreenProgress.isVisible = false
            detailScreenCharacterName.text = when (it.error) {
                ResourceError.EmptyContent -> "No content"
                is ResourceError.RequestFailError -> it.error.errorMessage
                ResourceError.NoNetworkError -> "No network"
            }
            detailScreenHeaderImage.visibility = View.INVISIBLE
            detailScreenCharacterDescription.visibility = View.INVISIBLE
        }
    }
}