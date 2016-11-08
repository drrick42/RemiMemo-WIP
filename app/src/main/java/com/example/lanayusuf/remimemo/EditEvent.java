package com.example.lanayusuf.remimemo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by LanaYusuf on 10/21/2016.
 */
public class EditEvent extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    //EditEvent class for creating a new event
    //TODO: make EditEvent class for editing an existing class
    private boolean error = true;
    private int day;
    private int month;
    private int year;

    private int hour;
    private int minute;

    private EditText editEventName;
    private EditText editEventDescription;

    private Spinner editEventPriority;
    private ArrayAdapter<String> mAdapter;

    private EditText editEventLocation;
    private EditText editTxtDate;
    private EditText editTxtTime;
    private String[] mPriority = {"Select", "High", "Low", "None"};
    private EventRemimemo event;

    public void setEventPage(){

        Intent intent = getIntent();

        if(intent.hasExtra("EVENT_ID")){
            event.setEventId(intent.getLongExtra("EVENT_ID",-1L));
        }

        if (intent.hasExtra("EVENT_NAME")){
            editEventName.setText(intent.getStringExtra("EVENT_NAME"));
        }

        if (intent.hasExtra("EVENT_DESCRIPTION")){
            editEventDescription.setText(intent.getStringExtra("EVENT_DESCRIPTION"));
        }

        if (intent.hasExtra("EVENT_PRIORITY")){
            int spinner = mAdapter.getPosition(intent.getStringExtra("EVENT_PRIORITY"));
            editEventPriority.setSelection(spinner);
        }

        if (intent.hasExtra("EVENT_DATE")){
            editTxtDate.setText(intent.getStringExtra("EVENT_DATE"));
        }

        if (intent.hasExtra("EVENT_TIME")){
            editTxtTime.setText(intent.getStringExtra("EVENT_TIME"));
        }

        if(intent.hasExtra("EVENT_LOCATION")){
            editEventLocation.setText(intent.getStringExtra("EVENT_LOCATION"));
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_event);

        //User clicks button when finished creating event
        Button btnDone = (Button)findViewById(R.id.btn_done);
        btnDone.setOnClickListener(this);

        //User clicks button when cancels creating event
        Button btnCancel = (Button)findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);

        event = new EventRemimemo();

        //******SET UP PAGE VALUES******//
        //User is able to edit the event name
        EditText editTxtEventName = (EditText) findViewById(R.id.editTxt_name);
        //User is able to edit the event description
        EditText editTxtEventDescription = (EditText) findViewById(R.id.editTxt_description);
        //User is able to add address location of event
        EditText editTxtEventLocation = (EditText) findViewById(R.id.editTxt_location);

        //User selects mPriority of event
        editEventPriority = (Spinner)findViewById(R.id.spinner_priority);
        mAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, mPriority);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editEventPriority.setAdapter(mAdapter);

        //User clicks on text and calendar pops up
        editTxtDate = (EditText) findViewById(R.id.editTxt_date);
        //User clicks on text and time pops up
        editTxtTime = (EditText) findViewById(R.id.editTxt_time);

        //******SET EVENT VALUES******//
        //listener to get event name
        editEventName = editTxtEventName;
        //listener to get event description
        editEventDescription = editTxtEventDescription;
        //listener to get event location
        editEventLocation = editTxtEventLocation;
        editEventPriority.setOnItemSelectedListener(this);
        editTxtDate.setOnClickListener(this);
        editTxtTime.setOnClickListener(this);

        //******SET INTENT VALUES IF EXISTS******//
        setEventPage();

    }

    @Override
    public void onDateSet(DatePicker view, int years, int monthOfYear, int dayOfMonth) {
        year = years;
        month = monthOfYear;
        day = dayOfMonth;
        updateDate();
    }

    // updates the date in the date EditText
    private void updateDate() {
        editTxtDate.setText(new StringBuilder().append(month + 1).append("/").append(day).append("/").append(year));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfDay) {
        hour = hourOfDay;
        minute = minuteOfDay;
        updateTime();
    }

    // updates the time in the time EditText
    private void updateTime() {
        //format correctly
        if(hour == 0){
            if(minute < 10){
                editTxtTime.setText(new StringBuilder().append("12").append(":").append("0").append(minute).append(" AM"));
            }else{
                editTxtTime.setText(new StringBuilder().append("12").append(":").append(minute).append(" AM"));
            }
        }else if(hour == 12){
            if(minute < 10){
                editTxtTime.setText(new StringBuilder().append(hour).append(":").append("0").append(minute).append(" PM"));
            }else{
                editTxtTime.setText(new StringBuilder().append(hour).append(":").append(minute).append(" PM"));
            }
        }else if(hour < 12){
            if(minute < 10){
                editTxtTime.setText(new StringBuilder().append(hour).append(":").append("0").append(minute).append(" AM"));
            }else{
                editTxtTime.setText(new StringBuilder().append(hour).append(":").append(minute).append(" AM"));
            }
        }else{
            if(minute < 10){
                editTxtTime.setText(new StringBuilder().append(hour-12).append(":").append("0").append(minute).append(" PM"));
            }else{
                editTxtTime.setText(new StringBuilder().append(hour-12).append(":").append(minute).append(" PM"));
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btn_done:
                //bring to previous Priority page with event updated
                if(!error) {

                    event.setEventName(editEventName.getText().toString());
                    event.setEventDescription(editEventDescription.getText().toString());
                    event.setEventLocation(editEventLocation.getText().toString());
                    event.setEditTxtDate(editTxtDate.getText().toString());
                    event.setEditTxtTime(editTxtTime.getText().toString());

                    EventDBHandler.getInstance().addEvent(event);

                    finish();
                    if(event.getEventPriority().contentEquals(mPriority[1])){
                        startActivity(new Intent(this, HighPriority.class));
                    }else if(event.getEventPriority().contentEquals(mPriority[2])){
                        startActivity(new Intent(this, LowPriority.class));
                    }else {
                        startActivity(new Intent(this, NoPriority.class));
                    }

                }else{
                    displayError();
                }

                break;

            case R.id.btn_cancel:
                finish();
                break;


            case R.id.editTxt_date:
                //Calendar pop-up
                Calendar calendarDate = Calendar.getInstance(TimeZone.getDefault());
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                        calendarDate.get(Calendar.YEAR), calendarDate.get(Calendar.MONTH),
                        calendarDate.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();
                break;

            case R.id.editTxt_time:
                //Clock pop-up
                Calendar calendarTime = Calendar.getInstance(TimeZone.getDefault());
                TimePickerDialog timePickerdialog = new TimePickerDialog(this, this,
                        calendarTime.get(Calendar.HOUR_OF_DAY), calendarTime.get(Calendar.MINUTE),
                        DateFormat.is24HourFormat(this));
                timePickerdialog.show();
                break;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        if(position == 0){
            error = true;
        }else{
            error = false;
            event.setEventPriority(parent.getItemAtPosition(position).toString());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }

    public void displayError(){
        Toast toast = Toast.makeText(this, "ERROR! Priority needs to be chosen!", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
        toast.show();
    }



}
