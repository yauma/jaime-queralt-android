package com.example.jaimequeraltgarrigos.spotify_artist.work

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.graphics.Color
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class CalendarSyncWorker(val context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {

    companion object {
        const val NOTIFICATION_ID = 0
        const val DESCRIPTION = "Description"
        const val WORK_NAME =
            "com.example.jaimequeraltgarrigos.spotify_artist.work.CalendarSyncWorker"

        // Notification channel ID.
        const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
    }

    override suspend fun doWork(): Result {
        withContext(Dispatchers.IO) {
            val artistList = ReadCalendar.readCalendar(context)
            initScheduler(artistList)
        }
        return Result.success()
    }

    private fun initScheduler(artistCalendarEvents: List<ArtistCalendarEvent>) {
        val alarmManagers = arrayOfNulls<AlarmManager>(artistCalendarEvents.size)
        val intents = arrayOfNulls<Intent>(artistCalendarEvents.size)

        for (i in alarmManagers.indices) {
            val notifyIntent = Intent(context, AlarmReceiver::class.java)
            notifyIntent.putExtra(DESCRIPTION, artistCalendarEvents[i].description)
            intents[i] = notifyIntent
            val pendingIntent: PendingIntent = PendingIntent.getBroadcast(
                context, i,
                intents[i], 0
            )
            alarmManagers[i] = applicationContext.getSystemService(ALARM_SERVICE) as AlarmManager
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                alarmManagers[i]!!.setExact(
                    AlarmManager.RTC_WAKEUP,
                    artistCalendarEvents[i].begin, pendingIntent
                )
            }
        }

        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Notification channels are only available in OREO and higher.
        val notificationChannel =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel(
                    PRIMARY_CHANNEL_ID,
                    "Artist Search Alert",
                    NotificationManager.IMPORTANCE_HIGH
                )
            } else {
                TODO("VERSION.SDK_INT < O")
            }
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationChannel.description = "Notifies every time GOLDENTIFY " +
                "is scheduled in the Calendar"
        mNotificationManager.createNotificationChannel(notificationChannel)
    }
}