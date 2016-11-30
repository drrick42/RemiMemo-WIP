package com.example.lanayusuf.remimemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Derrick on 11/8/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /**
        RemiNotifier notifier = RemiNotifier.getInstance();
        notifier.populateNotifications(context);
        String[] EventNames = notifier.eventNames;
        Date[] EventTimes = notifier.eventAlerts;
        boolean[] setAlerts = notifier.setAlert;
        Date time = Calendar.getInstance().getTime();
        long minDiff = 0;
        int[] event = new int[EventTimes.length]; // the events to notify
        int eventsNo = 0; // number of events to notify

        for (int i = 0; i < EventTimes.length; i++) {
            if (setAlerts[i]) {
                long diff = time.getTime() - EventTimes[i].getTime();

                if (diff < 0) {
                    diff = diff * -1;
                }
                if (minDiff == 0) {
                    event[eventsNo] = i;
                    eventsNo++;
                    minDiff = diff;
                } else if (diff < minDiff){
                    minDiff = diff;
                    event[0] = i;
                    eventsNo = 1;
                } else if (diff == minDiff) {
                    event[eventsNo] = i;
                    eventsNo++;
                }
            }
        }
         */

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        /**
        for (int i = 0; i < eventsNo; i++) {
         */
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            String vibrate = settings.getString("vibrate_pref", "ON");
            String sound = settings.getString("sound_pref", "ON");
            String name = intent.getStringExtra(RemiNotifier.event_name);
            String priority = intent.getStringExtra(RemiNotifier.event_pri);
            if (name == null) name = "An event";
            if (priority == null) priority = "None";
            if (vibrate.contains("ON")) {
                builder.setVibrate(new long[] { 500, 500, 100, 500, 100 });
            }
            if (sound.contains("ON")) {
                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                builder.setSound(uri);
            }

            Intent priority_page;
            if (priority.contains("High")) priority_page = new Intent(context, HighPriority.class);
            else if (priority.contains("Low")) priority_page = new Intent(context, LowPriority.class);
            else priority_page = new Intent(context, NoPriority.class);

            Notification notification = builder
                    .setContentTitle("RemiMemo")
                    .setContentText(name + " is coming up!")
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentIntent(PendingIntent.getActivity(context, 0, priority_page, 0))
                    .build();

            notificationManager.notify(0, notification);
        /**
        }
         */
    }
}
