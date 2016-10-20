package com.example.lanayusuf.remimemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by LanaYusuf on 10/19/2016.
 */
public class NoPriority extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.priority);

        TextView priority = (TextView)findViewById(R.id.txtViewPriority);
        priority.setText("No Priority");

        Button btnAddEvent = (Button)findViewById(R.id.btnAddEvent);
        btnAddEvent.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnAddEvent:
                //bring to edit event screen

        }
    }

}
