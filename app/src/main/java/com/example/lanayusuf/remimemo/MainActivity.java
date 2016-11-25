package com.example.lanayusuf.remimemo;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //Main Options page

    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "+++ onCreate() +++");
        setContentView(R.layout.activity_main);

        //If database not initialized, initialize it.
        EventDBHandler.getInstance().initializeDB(this);

        View btnHighPriority = findViewById(R.id.btnHighPriority);
        btnHighPriority.setOnClickListener(this);
        View btnLowPriority = findViewById(R.id.btnLowPriority);
        btnLowPriority.setOnClickListener(this);
        View btnNoPriority = findViewById(R.id.btnNoPriority);
        btnNoPriority.setOnClickListener(this);
        View btnEventMap = findViewById(R.id.btnEventMap);
        btnEventMap.setOnClickListener(this);
        View btnSettings = findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(this);

        if (!isMyServiceRunning()){
                Intent serviceIntent = new Intent(this, RemiServices.class);
                this.startService(serviceIntent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnHighPriority:
                startActivity(new Intent(this, HighPriority.class));
                break;
            case R.id.btnLowPriority:
                startActivity(new Intent(this, LowPriority.class));
                break;
            case R.id.btnNoPriority:
                startActivity(new Intent(this, NoPriority.class));
                break;
            case R.id.btnEventMap:
                startActivity(new Intent(this, EventMap.class));
                break;
            case R.id.btnSettings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
    }


    //listening for screen orientation change
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (RemiServices.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
