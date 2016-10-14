package com.example.lanayusuf.remimemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
