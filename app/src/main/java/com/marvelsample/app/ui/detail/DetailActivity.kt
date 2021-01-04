package com.marvelsample.app.ui.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.marvelsample.app.core.repository.model.characters.Character
import com.marvelsample.app.ui.base.compose.DetailsTheme
import com.marvelsample.app.ui.base.model.Result
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
        setContent {
            Bind(viewModel = viewModel)
        }
        viewModel.load(intent.getIntExtra(ITEM_ID_ARG, -1))
    }

    @Composable
    fun Bind(viewModel: DetailViewModel) {
        val characterResult by viewModel.itemObservable.observeAsState(Result.Loading)
        DetailsTheme {
            Surface(color = MaterialTheme.colors.background) {
                Scaffold(
                    topBar = {
                        TopAppBar(title = {
                            val text = when (characterResult) {
                                is Result.Success -> {
                                    (characterResult as Result.Success<Character>).result.name
                                }
                                else -> ""
                            }
                            Text(text = text)
                        })
                    },
                    bodyContent = {
                        CharacterDetailCard(
                            characterResult = characterResult,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                )
            }
        }
    }
}