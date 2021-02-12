package com.marvelsample.app.ui.characterdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.PagingData
import androidx.transition.TransitionInflater
import com.marvelsample.app.core.model.base.error.ResourceError
import com.marvelsample.app.databinding.DetailScreenBinding
import com.marvelsample.app.ui.base.model.Result
import com.marvelsample.app.ui.characterdetails.comics.ComicListItem
import com.marvelsample.app.ui.characterdetails.compose.CharacterDetailBody
import com.marvelsample.app.ui.characterdetails.compose.DetailScaffold
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
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
            composeView.setContent {
                Bind(
                    viewModel.itemDetailsObservable.observeAsState(Result.Loading).value,
                    viewModel.loadComics()
                )
            }
            viewModel.load()
        }
    }

    @Composable
    fun Bind(characterDetails: Result<CharacterModel>, comics: Flow<PagingData<ComicListItem>>) {
        Crossfade(targetState = findNavController().currentDestination) {
            DetailScaffold {
                CharacterDetailBody(characterDetails, comics) {
                    findNavController().popBackStack()
                }
            }
        }
    }

    @Preview(name = "Character preview")
    @Composable
    fun DefaultPreview() {
        val character = CharacterModel(
            "3-D Man",
            "3-D Man Description",
            "http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784.jpg"
        )

        val comics = listOf(
            ComicListItem(
                "3",
                "Character's comic 1",
                "http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784.jpg"
            )
        )

        Bind(characterDetails = Result.Success(character), flowOf(PagingData.from(comics)))
    }

    @Preview(name = "Character preview")
    @Composable
    fun DefaultPreviewWithDetailsError() {
        val comics = listOf(
            ComicListItem(
                "3",
                "Character's comic 1",
                "http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784.jpg"
            )
        )

        Bind(
            characterDetails = Result.Error(ResourceError.EmptyContent),
            flowOf(PagingData.from(comics))
        )
    }

    @Preview(name = "Character preview")
    @Composable
    fun DefaultPreviewWithoutComics() {
        val character = CharacterModel(
            "3-D Man",
            "3-D Man Description",
            "http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784.jpg"
        )

        Bind(characterDetails = Result.Success(character), flowOf(PagingData.from(emptyList())))
    }
}