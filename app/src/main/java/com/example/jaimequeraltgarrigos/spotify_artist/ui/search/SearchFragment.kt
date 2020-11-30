package com.example.jaimequeraltgarrigos.spotify_artist.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.jaimequeraltgarrigos.spotify_artist.R
import com.example.jaimequeraltgarrigos.spotify_artist.ui.adapter.ArtistsAdapter
import com.example.jaimequeraltgarrigos.spotify_artist.work.CalendarSyncWorker
import com.example.jaimequeraltgarrigos.spotify_artist.work.CalendarSyncWorker.Companion.DESCRIPTION
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.search_fragment.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        delayedInit()
    }

    private fun delayedInit() {
        lifecycleScope.launch {
            setupRecurringWork()
        }
    }

    private fun setupRecurringWork() {
        val repeatingRequest = PeriodicWorkRequestBuilder<CalendarSyncWorker>(
            17, TimeUnit.MINUTES
        )
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            CalendarSyncWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest)
    }

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
            searchView.setQuery(goldentifyQuery, false)
            searchView.clearFocus()
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