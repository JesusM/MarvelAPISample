package com.marvelsample.app.ui.characterdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.paging.LoadState
import androidx.transition.TransitionInflater
import com.google.android.material.snackbar.Snackbar
import com.marvelsample.app.R
import com.marvelsample.app.core.model.base.error.ResourceError
import com.marvelsample.app.databinding.DetailScreenBinding
import com.marvelsample.app.ui.base.model.Result
import com.marvelsample.app.ui.characterdetails.comics.ComicListItem
import com.marvelsample.app.ui.characterdetails.comics.adapter.ComicListItemTaskDiffCallback
import com.marvelsample.app.ui.characterdetails.comics.adapter.ComicsListAdapter
import com.marvelsample.app.ui.utils.imageloader.CoilImageLoader
import com.marvelsample.app.ui.utils.imageloader.loadImageAfterMeasure
import com.marvelsample.app.ui.utils.launchUI
import com.marvelsample.app.ui.utils.textPaletteAsync
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

class DetailFragment : Fragment() {
    private val args: DetailFragmentArgs by navArgs()

    private val viewModel: DetailViewModel by viewModel(named("detailViewModel")) {
        parametersOf(args.itemId)
    }

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
            val itemId = args.itemId
            detailScreenHeaderImage.transitionName = "thumb$itemId"
            detailScreenCharacterName.transitionName = "title$itemId"

            initNavigation(this)

            initDetails(this, viewModel)

            initComicsList(this)
        }
    }

    private fun initNavigation(detailScreenBinding: DetailScreenBinding) {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        detailScreenBinding.detailScreenToolbar.apply {
            setupWithNavController(navController, appBarConfiguration)

            // When linking toolbar to navigation controller, it will use a "left" arrow as up
            // indicator. As we want a custom icon, we must set it again after linking step.
            //
            navigationIcon = ContextCompat.getDrawable(context, R.drawable.toolbar_navigation_icon)
        }
    }

    private fun initDetails(
        detailScreenBinding: DetailScreenBinding,
        viewModel: DetailViewModel
    ) {
        viewModel.itemDetailsObservable.observe(viewLifecycleOwner) {
            bindDetails(it, detailScreenBinding)
        }
        postponeEnterTransition()
        viewModel.load()
    }

    private fun initComicsList(detailScreenBinding: DetailScreenBinding) {
        val adapter = ComicsListAdapter(
            CoilImageLoader(requireContext()),
            ComicListItemTaskDiffCallback()
        ) { item: ComicListItem, view: View ->
            Snackbar.make(view, "Clicked comic ${item.name}.", Snackbar.LENGTH_SHORT).show()
        }

        detailScreenBinding.charactersDetailComicsContainer.apply {
            root.isVisible = true
            horizontalList.adapter = adapter

            lifecycleScope.launch {
                viewModel.loadComics().collectLatest {
                    adapter.submitData(it)
                }
            }

            lifecycleScope.launch {
                adapter.loadStateFlow.collectLatest { loadStates ->
                    detailScreenBinding.charactersDetailComicsContainer.horizontalListProgress.isVisible =
                        loadStates.refresh is LoadState.Loading
                }
            }
        }
    }

    private fun bindDetails(result: Result<CharacterModel>, binding: DetailScreenBinding) {
        binding.detailScreenProgress.isVisible = result is Result.Success

        when (result) {
            is Result.Error -> {
                bindError(result, binding)
            }
            is Result.Success -> {
                bindDetails(result.result, binding)
            }
            Result.Loading -> {
                // Do nothing
            }
        }
    }

    private fun bindDetails(
        character: CharacterModel,
        binding: DetailScreenBinding
    ) {
        binding.apply {
            detailScreenProgress.isVisible = false
            detailScreenCharacterDescription.isVisible = true
            detailScreenCharacterName.isVisible = true
            detailScreenHeaderImage.isVisible = true
            charactersDetailComicsContainer.root.isVisible = true

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