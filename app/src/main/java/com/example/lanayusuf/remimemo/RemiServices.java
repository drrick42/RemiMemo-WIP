package com.example.lanayusuf.remimemo;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;

/**
 * Created by Derrick on 11/5/2016.
 */
public class RemiServices extends Service implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // START YOUR TASKS
        //RemiNotifier.getInstance().setNotifications(this);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        RemiNotifier.getInstance().setNotifications(this);
    }

}
