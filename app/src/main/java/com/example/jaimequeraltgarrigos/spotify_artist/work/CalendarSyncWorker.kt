package com.example.jaimequeraltgarrigos.spotify_artist.work

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat

class CalendarSyncWorker(val context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {

    companion object {
        const val WORK_NAME =
            "com.example.jaimequeraltgarrigos.spotify_artist.work.calendarsyncworker"
        const val NOTIFICATION_ID = 0
        const val DESCRIPTION = "Description"

        // Notification channel ID.
        const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
    }

    //TODO ask user permision calendar
    override suspend fun doWork(): Result {
        withContext(Dispatchers.IO) {
            val artistList = ReadCalendar.readCalendar(context)
            initScheduler(artistList.first())
        }
        return Result.success()
    }

    private fun initScheduler(artistCalendarEvent: ArtistCalendarEvent) {
        val notifyIntent = Intent(context, AlarmReceiver::class.java)
        notifyIntent.putExtra(DESCRIPTION, artistCalendarEvent.description)

        val notifyPendingIntent = PendingIntent.getBroadcast(
            context, NOTIFICATION_ID, notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(
            AlarmManager.RTC,
            artistCalendarEvent.begin, notifyPendingIntent
        )

        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.

        // Create the NotificationChannel with all the parameters.
        val notificationChannel = NotificationChannel(
            PRIMARY_CHANNEL_ID,
            "Artist Search Alert",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationChannel.description = "Notifies every time GOLDENTIFY " +
                "is scheduled in the Calendar"
        mNotificationManager.createNotificationChannel(notificationChannel)
    }
}