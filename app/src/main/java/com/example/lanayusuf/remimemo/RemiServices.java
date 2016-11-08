package com.example.lanayusuf.remimemo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Derrick on 11/5/2016.
 */
public class RemiServices extends Service {

    public Integer idNum = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        // START YOUR TASKS

        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        // STOP YOUR TASKS
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    public void GetNotification(String message, String event_time, int alert) {

    }
}
