package com.example.lanayusuf.remimemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "+++ onCreate() +++");
        setContentView(R.layout.activity_main);

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

    }

    protected void onStart() {
        super.onStart();
        Log.d(TAG, "++ onStart() ++");
    }

    protected void onResume() {
        super.onResume();
        Log.d(TAG, "+ onResume() +");
    }

    protected void onPause() {
        super.onPause();
        Log.d(TAG, "- onPause() -");
    }

    protected void onStop() {
        super.onStop();
        Log.d(TAG, "-- onStop() --");
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "--- onDestroy() ---");

        android.os.Debug.stopMethodTracing();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnHighPriority:
                System.out.println("pressed high priority button");
                break;
            case R.id.btnLowPriority:
                break;
            case R.id.btnNoPriority:
                break;
            case R.id.btnEventMap:
                startActivity(new Intent(this, EventMap.class));
                break;
            case R.id.btnSettings:
                break;
        }
    }
}
