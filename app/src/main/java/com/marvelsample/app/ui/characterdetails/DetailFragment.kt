package com.marvelsample.app.ui.characterdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.marvelsample.app.databinding.DetailScreenBinding
import com.marvelsample.app.ui.base.model.Result
import com.marvelsample.app.ui.characterdetails.compose.CharacterDetailCard
import com.marvelsample.app.ui.characterdetails.compose.ThemedScaffold
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
                Bind(characterResult = viewModel.itemDetailsObservable.observeAsState(Result.Loading).value)
            }
            viewModel.load()
        }
    }

    @Composable
    fun Bind(characterResult: Result<CharacterModel>) {
        ThemedScaffold {
            CharacterDetailCard(
                characterResult = characterResult,
                modifier = Modifier.padding(16.dp)
            )
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

        Bind(characterResult = Result.Success(character))
    }
}