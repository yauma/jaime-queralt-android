package com.example.jaimequeraltgarrigos.spotify_artist

import android.app.Application
import android.os.Build
import androidx.work.*
import androidx.work.PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS
import com.example.jaimequeraltgarrigos.spotify_artist.work.CalendarSyncWorker
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class MyApplication : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    /**
     * onCreate is called before the first screen is shown to the user.
     *
     * Use it to setup any background tasks, running expensive setup operations in a background
     * thread to avoid delaying app start.
     */
    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }

    private fun delayedInit() {
        applicationScope.launch {
            setupRecurringWork()
        }
    }

    /**
     * Setup WorkManager background job to 'fetch' new "GOLDENTIFY" Calendar events.
     */
    private fun setupRecurringWork() {

        val repeatingRequest = PeriodicWorkRequestBuilder<CalendarSyncWorker>(
            MIN_PERIODIC_INTERVAL_MILLIS,
            TimeUnit.MINUTES
        )
            .build()

        WorkManager.getInstance(this).enqueue(repeatingRequest)
    }
}