package com.example.jaimequeraltgarrigos.spotify_artist.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.example.jaimequeraltgarrigos.spotify_artist.R
import com.example.jaimequeraltgarrigos.spotify_artist.ui.adapter.ArtistsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.search_fragment.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow

@AndroidEntryPoint
class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private lateinit var viewModel: SearchViewModel
    var queryTextChangedJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        val adapter = ArtistsAdapter()
        recyclerView.adapter = adapter
        viewModel.artists.observe(this) {
            adapter.submitList(it)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                val length = query?.length ?: 0
                return if (length > 3) {
                    runBlocking {
                        delay(300)
                        query?.let { viewModel.artistSearch(it) }
                    }
                    true
                } else {
                    false
                }

            }

        })

    }

    override fun onStart() {
        super.onStart()
    }
}