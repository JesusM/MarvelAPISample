package com.marvelsample.app.ui.characterslist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.marvelsample.app.R
import com.marvelsample.app.databinding.CharactersListScreenBinding
import com.marvelsample.app.ui.characterdetails.DetailFragment
import com.marvelsample.app.ui.characterslist.adapter.CharactersListAdapter
import com.marvelsample.app.ui.characterslist.adapter.ListItemTaskDiffCallback
import com.marvelsample.app.ui.utils.imageloader.CoilImageLoader
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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
            val adapter = CharactersListAdapter(
                CoilImageLoader(requireContext()),
                ListItemTaskDiffCallback()
            ) { item: ListItem, view: View ->
                val thumb = view.findViewById<ImageView>(R.id.image)
                val title = view.findViewById<TextView>(R.id.text)
                val extras = FragmentNavigatorExtras(
                    thumb to "thumb${item.id}",
                    title to "title${item.id}"
                )

                val args = Bundle().apply {
                    putInt(DetailFragment.ITEM_ID_ARG, item.id)
                }

                findNavController().navigate(R.id.navigateToDetail, args, null, extras)
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
                    charactersListProgress.isVisible = loadStates.refresh is LoadState.Loading
                    charactersListErrorMessage.apply {
                        val isError = loadStates.refresh is LoadState.Error
                        isVisible = isError
                        if (isError) {
                            text = (loadStates.refresh as LoadState.Error).error.message
                        }
                    }
                }
            }
        }
    }
}