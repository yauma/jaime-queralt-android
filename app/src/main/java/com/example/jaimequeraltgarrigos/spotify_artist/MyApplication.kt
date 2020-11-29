package com.example.jaimequeraltgarrigos.spotify_artist

import android.app.Application
import android.os.Build
import androidx.work.*
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
     * Setup WorkManager background job to 'fetch' new network data daily.
     */
    private fun setupRecurringWork() {

        val repeatingRequest = OneTimeWorkRequestBuilder<CalendarSyncWorker>()
            .build()

/*        val repeatingRequest = PeriodicWorkRequestBuilder<CalendarSyncWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()*/

        WorkManager.getInstance(this).enqueue(repeatingRequest)
    }
}