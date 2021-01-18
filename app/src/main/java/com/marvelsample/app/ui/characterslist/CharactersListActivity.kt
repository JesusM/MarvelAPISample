package com.marvelsample.app.ui.characterslist

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.setContent
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.marvelsample.app.ui.characterslist.components.CharactersList
import com.marvelsample.app.ui.detail.ThemedScaffold
import kotlinx.coroutines.flow.Flow
import org.kodein.di.*
import org.kodein.di.android.closestDI
import org.kodein.di.android.retainedDI

@ExperimentalFoundationApi
class CharactersListActivity : AppCompatActivity(), DIAware {

    private val activityModule = DI.Module("itemDetail") {
        bind("charactersListViewModelFactory") from factory { activity: CharactersListActivity ->
            val viewModelFactory = object : AbstractSavedStateViewModelFactory(activity, null) {
                override fun <T : ViewModel?> create(
                    key: String,
                    modelClass: Class<T>,
                    handle: SavedStateHandle
                ): T {
                    return CharactersListViewModel(instance()) as T
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

    private val viewModelFactory: AbstractSavedStateViewModelFactory by instance(
        "charactersListViewModelFactory",
        arg = this
    )
    private val viewModel: CharactersListViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Bind(characters = viewModel.load())
        }
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
                    CharactersList(characters = characters, context = this@CharactersListActivity)
                }
            )
        }
    }
}