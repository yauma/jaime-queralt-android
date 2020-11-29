package com.example.jaimequeraltgarrigos.spotify_artist.ui.search

import android.app.PendingIntent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.jaimequeraltgarrigos.spotify_artist.R
import com.example.jaimequeraltgarrigos.spotify_artist.ui.adapter.ArtistsAdapter
import com.example.jaimequeraltgarrigos.spotify_artist.work.CalendarSyncWorker.Companion.DESCRIPTION
import com.example.jaimequeraltgarrigos.spotify_artist.work.ReadCalendar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.search_fragment.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


@AndroidEntryPoint
class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private lateinit var viewModel: SearchViewModel

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

        // Show a snackbar whenever the [ViewModel.snackbar] is updated a non-null value
        viewModel.snackbar.observe(this) { text ->
            text?.let {
                Snackbar.make(search, text, Snackbar.LENGTH_SHORT).show()
                viewModel.onSnackbarShown()
            }
        }

        // show the spinner when [MainViewModel.spinner] is true
        viewModel.spinner.observe(this) { value ->
            value.let { show ->
                progressBar.visibility = if (show) View.VISIBLE else View.GONE
            }
        }

        val goldentifyQuery = activity?.intent?.getStringExtra(DESCRIPTION)
        if (goldentifyQuery != null && goldentifyQuery.length > 3) {
            viewModel.queryMade(goldentifyQuery)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                val length = query?.length ?: -1
                return when {
                    length > 3 -> {
                        runBlocking {
                            delay(300)
                        }
                        viewModel.queryMade(query!!)
                        true
                    }
                    length == 0 -> {
                        viewModel.clearAdapter()
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        })
    }
}