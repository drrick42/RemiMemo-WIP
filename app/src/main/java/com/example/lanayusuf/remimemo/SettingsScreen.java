package com.example.lanayusuf.remimemo;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * Created by Derrick on 11/6/2016.
 */
public class SettingsScreen extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        Button btnDone = (Button)findViewById(R.id.btnBackSettings);
        btnDone.setOnClickListener(this);

        String[] alert = {"Never", "1 Hour Before", "2 Hours Before", "4 Hours Before", "24 Hours Before"};

        Spinner high_spinner = (Spinner)findViewById(R.id.high_pri_alert);
        ArrayAdapter<String> high_adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, alert);
        high_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        high_spinner.setAdapter(high_adapter);
        high_spinner.setOnItemSelectedListener(this);

        Spinner low_spinner = (Spinner)findViewById(R.id.low_pri_alert);
        ArrayAdapter<String> low_adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, alert);
        low_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        low_spinner.setAdapter(low_adapter);
        low_spinner.setOnItemSelectedListener(this);

        Spinner no_spinner = (Spinner)findViewById(R.id.no_pri_alert);
        ArrayAdapter<String> no_adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, alert);
        no_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        no_spinner.setAdapter(no_adapter);
        no_spinner.setOnItemSelectedListener(this);

        String[] sound = {"On" , "Off" };

        Spinner high_spinner_sound = (Spinner)findViewById(R.id.high_pri_sound);
        ArrayAdapter<String> high_adapter_sound = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, sound);
        high_adapter_sound.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        high_spinner_sound.setAdapter(high_adapter_sound);
        high_spinner_sound.setOnItemSelectedListener(this);

        Spinner low_spinner_sound = (Spinner)findViewById(R.id.low_pri_sound);
        ArrayAdapter<String> low_adapter_sound = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, sound);
        low_adapter_sound.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        low_spinner_sound.setAdapter(low_adapter_sound);
        low_spinner_sound.setOnItemSelectedListener(this);

        Spinner no_spinner_sound = (Spinner)findViewById(R.id.no_pri_sound);
        ArrayAdapter<String> no_adapter_sound = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, sound);
        no_adapter_sound.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        no_spinner_sound.setAdapter(no_adapter_sound);
        no_spinner_sound.setOnItemSelectedListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnBackSettings:
                //bring to Main Options screen
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        switch (v.getId())
        {
            case R.id.high_pri_alert:
                switch (position) {
                    case 0:
                        // Throw error dialog pop-up. User needs to select.
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                }
                break;
            case R.id.low_pri_alert:
                switch (position) {
                    case 0:
                        // Throw error dialog pop-up. User needs to select.
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                }
                break;
            case R.id.no_pri_alert:
                switch (position) {
                    case 0:
                        // Throw error dialog pop-up. User needs to select.
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                }
                break;
            case R.id.high_pri_sound:
                switch (position) {
                    case 0:
                        // Throw error dialog pop-up. User needs to select.
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
                break;
            case R.id.low_pri_sound:
                switch (position) {
                    case 0:
                        // Throw error dialog pop-up. User needs to select.
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
                break;
            case R.id.no_pri_sound:
                switch (position) {
                    case 0:
                        // Throw error dialog pop-up. User needs to select.
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
                break;
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }

    public NotificationCompat.Builder MakeNotification (String message) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("RemiMemo")
                .setContentText(message);

        Intent resultIntent = new Intent(this, EventMap.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
        return mBuilder;
    }
}
