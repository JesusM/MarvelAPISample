package com.marvelsample.app.ui.characterslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import com.marvelsample.app.R
import com.marvelsample.app.databinding.CharactersListScreenBinding
import com.marvelsample.app.ui.characterdetails.compose.ThemedScaffold
import com.marvelsample.app.ui.characterslist.compose.CharactersList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named

class CharactersListFragment : Fragment() {

    private val viewModel: CharactersListViewModel by viewModel(named("charactersListViewModel"))

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return CharactersListScreenBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CharactersListScreenBinding.bind(view).apply {
            charactersListComposeView.setContent {
                Bind(characters = viewModel.load())
            }
        }
    }

    @Preview(name = "Character preview")
    @Composable
    fun DefaultPreview() {
        val characters = listOf(ListItem(
            3,
            "3-D Man Description",
            "http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784.jpg"
        ))

        Bind(characters = flowOf(PagingData.from(characters)))
    }

    @Composable
    fun Bind(characters: Flow<PagingData<ListItem>>) {
        ThemedScaffold {
            Scaffold(
                topBar = {
                    TopAppBar(title = {
                        Text(text = "Characters")
                    })
                },
                bodyContent = {
                    CharactersList(characters = characters) { characterId : Int ->
                        val args = Bundle().apply {
                            putInt("itemId", characterId)
                        }

                        findNavController().navigate(R.id.navigateToDetail, args, null, null)
                    }
                }
            )
        }
    }
}