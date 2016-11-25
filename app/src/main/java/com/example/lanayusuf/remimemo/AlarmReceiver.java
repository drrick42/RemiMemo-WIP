package com.example.lanayusuf.remimemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Derrick on 11/8/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String[] EventNames = null;
        Date[] EventTimes = null;
        boolean[] setAlerts = null;
        getEvents(EventNames, EventTimes, setAlerts, context);
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

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        
        for (int i = 0; i < eventsNo; i++) {
            Notification notification = new NotificationCompat.Builder(context)
                    .setContentTitle("RemiMemo")
                    .setContentText(EventNames[event[i]] + " is coming up!")
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, EditEvent.class), 0))
                    .build();

            notificationManager.notify(0, notification);
        }
    }

    private void getEvents(String[] names, Date[] dates, boolean[] set, Context context) {
        RemiNotifier notifier = RemiNotifier.getInstance();
        notifier.populateNotifications(context);
        names = notifier.eventNames;
        dates = notifier.eventAlerts;
        set = notifier.setAlert;
    }
}
