package com.marvelsample.app.ui.characterslist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.marvelsample.app.databinding.CharactersListActivityBinding
import com.marvelsample.app.ui.characterslist.adapter.CharactersListAdapter
import com.marvelsample.app.ui.characterslist.adapter.ListItemTaskDiffCallback
import com.marvelsample.app.ui.detail.DetailActivity
import com.marvelsample.app.ui.utils.imageloader.CoilImageLoader
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.kodein.di.*
import org.kodein.di.android.closestDI
import org.kodein.di.android.retainedDI

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
        CharactersListActivityBinding.inflate(layoutInflater).apply {
            setContentView(root)

            val adapter = CharactersListAdapter(
                CoilImageLoader(this@CharactersListActivity),
                ListItemTaskDiffCallback()
            ) { item: ListItem, _: View ->
                val intent = Intent(this@CharactersListActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.ITEM_ID_ARG, item.id)
                startActivity(intent)
            }

            charactersList.adapter = adapter

            lifecycleScope.launch {
                viewModel.load().collectLatest {
                    adapter.submitData(it)
                }
            }

            lifecycleScope.launch {
                adapter.loadStateFlow.collectLatest { loadStates ->
                    Log.d("[CharacterList]", "Current paging state: $loadStates")
                    charactersListProgress.isVisible =
                        loadStates.refresh is LoadState.Loading
                }
            }
        }
    }
}