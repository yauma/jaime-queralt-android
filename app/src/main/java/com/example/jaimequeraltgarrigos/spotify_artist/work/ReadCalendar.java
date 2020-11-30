package com.example.jaimequeraltgarrigos.spotify_artist.work;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReadCalendar {
    private static String[] selectionsArg;

    public static List<ArtistCalendarEvent> readCalendar(Context context) {
        List<ArtistCalendarEvent> eventList = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        final Cursor cursor = contentResolver.query(CalendarContract.Calendars.CONTENT_URI,
                (new String[]{CalendarContract.Calendars._ID, CalendarContract.Calendars.CALENDAR_DISPLAY_NAME}), null, null, null);

        while (cursor.moveToNext()) {
            final String _id = cursor.getString(0);

            selectionsArg = new String[]{_id, "Goldentify"};

            Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();

            long startMills = System.currentTimeMillis();
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String start = sdfDate.format(startMills);

            Calendar endTime = Calendar.getInstance();
            endTime.set(2021, Calendar.SEPTEMBER, 2, 20, 0);
            long endMills = endTime.getTimeInMillis();
            ContentUris.appendId(builder, startMills);
            ContentUris.appendId(builder, endMills);


            Cursor eventCursor = contentResolver.query(builder.build(), new String[]{CalendarContract.Instances.TITLE,
                            CalendarContract.Instances.BEGIN, CalendarContract.Instances.END, CalendarContract.Instances.DESCRIPTION},
                    "((" + CalendarContract.Instances.CALENDAR_ID + " = ?) AND ("
                            + CalendarContract.Instances.TITLE + " = ?))", selectionsArg, null);

            while (eventCursor.moveToNext()) {
                final String title = eventCursor.getString(0);
                final Date begin = new Date(eventCursor.getLong(1));
                final Date end = new Date(eventCursor.getLong(2));
                final String description = eventCursor.getString(3);

                eventList.add(new ArtistCalendarEvent(title, begin.getTime(), end.getTime(), description));
                Log.d("Cursor", "Title: " + title + "\tDescription: " + description + "\tBegin: " + begin + "\tEnd: " + end);
            }
        }
        return eventList;
    }
}
