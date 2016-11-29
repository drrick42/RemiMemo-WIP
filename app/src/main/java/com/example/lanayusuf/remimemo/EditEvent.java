package com.example.lanayusuf.remimemo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
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

    //Error checking
    private enum ERRORS{PRIORITY, DATE, TIME, TITLE}
    private boolean error[];
    //EditEvent class for creating a new event
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

    private String defaultDate;
    private String defaultTime;
    private String defaultTitle;


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

        //User clicks button when cancels creating event
        Button btnDelete = (Button)findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(this);

        error = new boolean[]{true,true,true,true};
        event = new EventRemimemo();

        //******SET UP PAGE VALUES******//
        //User is able to edit the event name
        EditText editTxtEventName = (EditText) findViewById(R.id.editTxt_name);
        //User is able to edit the event description
        EditText editTxtEventDescription = (EditText) findViewById(R.id.editTxt_description);
        //User is able to add address location of event
        EditText editTxtEventLocation = (EditText) findViewById(R.id.editTxt_location);
        defaultTitle = editTxtEventName.getText().toString();

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
        defaultDate = editTxtDate.getText().toString();
        defaultTime = editTxtTime.getText().toString();

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
        String date = "";
        if(month+1 < 10) {
            date += "0" + Integer.toString(month+1);
        }else{
            date += Integer.toString(month+1);
        }

        date += "/";

        if(day < 10){
            date += "0" + Integer.toString(day);
        }else{
            date += Integer.toString(day);
        }

        date += "/"+ Integer.toString(year);

        editTxtDate.setText(date);
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
        }else if(hour<10){
            if(minute < 10){
                editTxtTime.setText(new StringBuilder().append("0").append(hour).append(":").append("0").append(minute).append(" AM"));
            }else{
                editTxtTime.setText(new StringBuilder().append("0").append(hour).append(":").append(minute).append(" AM"));
            }
        }else if(hour>=10 && hour < 12){
            if(minute < 10){
                editTxtTime.setText(new StringBuilder().append(hour).append(":").append("0").append(minute).append(" AM"));
            }else{
                editTxtTime.setText(new StringBuilder().append(hour).append(":").append(minute).append(" AM"));
            }
        }else if(hour == 12){
            if(minute < 10){
                editTxtTime.setText(new StringBuilder().append(hour).append(":").append("0").append(minute).append(" PM"));
            }else{
                editTxtTime.setText(new StringBuilder().append(hour).append(":").append(minute).append(" PM"));
            }
        }else{ //When hour larger than 12

            hour -= 12;

            if(hour<10){
                if(minute < 10){
                    editTxtTime.setText(new StringBuilder().append("0").append(hour).append(":").append("0").append(minute).append(" PM"));
                }else{
                    editTxtTime.setText(new StringBuilder().append("0").append(hour).append(":").append(minute).append(" PM"));
                }
            }else{
                if(minute < 10){
                    editTxtTime.setText(new StringBuilder().append(hour).append(":").append("0").append(minute).append(" PM"));
                }else{
                    editTxtTime.setText(new StringBuilder().append(hour).append(":").append(minute).append(" PM"));
                }
            }

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btn_done:
                //bring to previous Priority page with event updated

                event.setEventName(editEventName.getText().toString());
                event.setEventDescription(editEventDescription.getText().toString());
                event.setEventLocation(editEventLocation.getText().toString());
                event.setEditTxtDate(editTxtDate.getText().toString());
                event.setEditTxtTime(editTxtTime.getText().toString());

                //if the event date is NOT the default
                if (!event.getEditTxtDate().contentEquals(defaultDate)){
                    error[ERRORS.DATE.ordinal()] = false;
                }

                //if the event time is NOT the default
                if (!event.getEditTxtTime().contentEquals(defaultTime)){
                    error[ERRORS.TIME.ordinal()] = false;
                }

                //if the event title is NOT the default
                if (!event.getEventName().contentEquals(defaultTitle)){
                    error[ERRORS.TITLE.ordinal()] = false;
                }

                //if priority,date, time, and title have been set correctly
                if(!error[ERRORS.PRIORITY.ordinal()] && !error[ERRORS.DATE.ordinal()] &&
                        !error[ERRORS.TIME.ordinal()] && !error[ERRORS.TITLE.ordinal()] ) {

                    if (event.getEventId() == -1L) {
                        EventDBHandler.getInstance().addEvent(event);
                    } else {
                        EventDBHandler.getInstance().updateEvent(event);
                    }

                    RemiNotifier.getInstance().setNotifications(this);

                    finish();
                    chooseActivityToStart();
                }else {
                    if(error[ERRORS.TITLE.ordinal()]){
                        //If the title is the default title or title is empty, reset title
                        event.setEventName(defaultTitle);
                    }
                    displayError();
                }

                break;

            case R.id.btn_cancel:
                finish();
                break;

            case R.id.btn_delete:
                if(event.getEventId()!=-1L){
                    EventDBHandler.getInstance().deleteEvent(event);
                }

                finish();
                chooseActivityToStart();

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
            error[ERRORS.PRIORITY.ordinal()] = true;
        }else{
            error[ERRORS.PRIORITY.ordinal()] = false;
            event.setEventPriority(parent.getItemAtPosition(position).toString());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }

    public void displayError(){

        String priorityError = "";
        String dateError = "";
        String timeError = "";
        String nameError = "";

        if(error[ERRORS.PRIORITY.ordinal()]){
            priorityError = "Priority, ";
        }

        if(error[ERRORS.DATE.ordinal()]){
            dateError = "Date, ";
        }

        if(error[ERRORS.TIME.ordinal()]){
            timeError = "Time, ";
        }

        if(error[ERRORS.TITLE.ordinal()]){
            nameError = "Title, ";
        }

        Toast toast = Toast.makeText(this, "ERROR! "+ priorityError +
                dateError + timeError + nameError +" needs to be chosen!", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
        toast.show();
    }

    private void chooseActivityToStart(){
        Intent intent = getIntent();

        if(intent.getStringExtra("SUPER_CLASS_PRIORITY").contentEquals(mPriority[1])){
            startActivity(new Intent(this, HighPriority.class));
        }else if(intent.getStringExtra("SUPER_CLASS_PRIORITY").contentEquals(mPriority[2])){
            startActivity(new Intent(this, LowPriority.class));
        }else {
            startActivity(new Intent(this, NoPriority.class));
        }
    }

    //listening for screen orientation change
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}
