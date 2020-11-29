package com.example.jaimequeraltgarrigos.spotify_artist.work

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.jaimequeraltgarrigos.spotify_artist.MainActivity
import com.example.jaimequeraltgarrigos.spotify_artist.R
import com.example.jaimequeraltgarrigos.spotify_artist.work.CalendarSyncWorker.Companion.NOTIFICATION_ID
import com.example.jaimequeraltgarrigos.spotify_artist.work.CalendarSyncWorker.Companion.PRIMARY_CHANNEL_ID

class AlarmReceiver : BroadcastReceiver() {
    private lateinit var mNotificationManager: NotificationManager

    override fun onReceive(context: Context, intent: Intent?) {
        mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        deliverNotification(context, intent)
    }

    private fun deliverNotification(context: Context, intent: Intent?) {
        val contentIntent = Intent(context, MainActivity::class.java)
        val goldentifyQuery = intent?.getStringExtra(CalendarSyncWorker.DESCRIPTION)
        contentIntent.putExtra(CalendarSyncWorker.DESCRIPTION, goldentifyQuery)
        val contentPendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Build the notification
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(
            context,
            PRIMARY_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.circle)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(context.getString(R.string.notification_text))
            .setContentIntent(contentPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        // Deliver the notification
        mNotificationManager.notify(NOTIFICATION_ID, builder.build())
    }

}
