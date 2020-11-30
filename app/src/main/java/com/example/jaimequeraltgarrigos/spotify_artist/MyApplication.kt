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
class MyApplication : Application()