package com.example.lanayusuf.remimemo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Derrick on 11/5/2016.
 */
public class RemiServices extends Service {

    private static int LIMIT = 10;
    public String[] eventNames = new String[LIMIT];
    public String[] eventPriorities = new String[LIMIT];
    public String[] eventTimes = new String[LIMIT];
    public String[] eventDates = new String[LIMIT];
    public Date[] eventAlerts = new Date[LIMIT];

    public int[] priorityAlertOptions = new int[3];

    public boolean[] setAlert = new boolean[LIMIT];

    public PendingIntent[] intents = new PendingIntent[LIMIT];
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // START YOUR TASKS
        setNotifications();
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

    public void setNotifications() {
        getEvents();
        setAlertTimes();
        createNotifications();
    }

    private void getEvents() {
        int counter = 0;
        for (int i = 0; i < 10; i++) {
            eventNames[i] = "";
            eventPriorities[i] = "";
            eventTimes[i] = "";
        }

        EventDBHandler.initializeDB(this);
        if (EventDBHandler.getInstance().isDatabaseExists(this)) {
            EventRemimemo event;
            List<EventRemimemo> eventRemimemoList = EventDBHandler.getInstance().queryEvents("High");
            for (int i = 0; i < eventRemimemoList.size(); i++) {
                event = eventRemimemoList.get(i);
                eventNames[counter] = event.getEventName();
                eventPriorities[counter] = "High";
                eventTimes[counter] = event.getEditTxtTime();
                eventDates[counter] = event.getEditTxtDate();
                counter++;
            }

            eventRemimemoList = EventDBHandler.getInstance().queryEvents("Low");
            for (int i = 0; i < eventRemimemoList.size(); i++) {
                event = eventRemimemoList.get(i);
                eventNames[counter] = event.getEventName();
                eventPriorities[counter] = "Low";
                eventTimes[counter] = event.getEditTxtTime();
                eventDates[counter] = event.getEditTxtDate();
                counter++;
            }

            eventRemimemoList = EventDBHandler.getInstance().queryEvents("None");
            for (int i = 0; i < eventRemimemoList.size(); i++) {
                event = eventRemimemoList.get(i);
                eventNames[counter] = event.getEventName();
                eventPriorities[counter] = "None";
                eventTimes[counter] = event.getEditTxtTime();
                eventDates[counter] = event.getEditTxtDate();
                counter++;
            }
        }
    }

    private void setAlertTimes() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        priorityAlertOptions[0] = getPrioritySetting(settings, "high_pri_alert_pref");
        priorityAlertOptions[1] = getPrioritySetting(settings, "low_pri_alert_pref");
        priorityAlertOptions[2] = getPrioritySetting(settings, "no_pri_alert_pref");
        for (int i = 0; i < eventNames.length; i++) {
            setAlert[i] = false;
            if (eventNames[i].length() > 0) {
                int priority_type = 0;
                switch (eventPriorities[i])
                {
                    case "High":
                        priority_type = 0;
                        break;
                    case "Low":
                        priority_type = 1;
                        break;
                    case "None":
                        priority_type = 2;
                        break;
                }
                try {
                    if (eventDates[i].length() > 0 && eventTimes[i].length() > 0) {
                        Date event_date = getDate(eventDates[i], eventTimes[i]);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(event_date);
                        if (priorityAlertOptions[priority_type] == 24) {
                            // set eventDateAlerts back one day
                            cal.add(Calendar.DAY_OF_MONTH, -1);
                            setAlert[i] = true;
                        } else if (priorityAlertOptions[priority_type] == 1) {
                            //set eventTimeAlerts back an hour, and eventDateAlerts back a day if necessary
                            cal.add(Calendar.HOUR_OF_DAY, -1);
                            setAlert[i] = true;
                        } else if (priorityAlertOptions[priority_type] == 2) {
                            //set eventTimeAlerts back two hours, and eventDateAlerts back a day if necessary
                            cal.add(Calendar.HOUR_OF_DAY, -2);
                            setAlert[i] = true;
                        } else {
                            //no alert
                        }
                        eventAlerts[i] = cal.getTime();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private int getPrioritySetting(SharedPreferences pref, String priority) {
        String alertTime = pref.getString(priority, "1");
        int i = 0;
        if (alertTime.contains("1")) {
            i = 1;
        } else if (alertTime.contains("24")) {
            i = 24;
        } else if (alertTime.contains("0")) {
            // i = 0
        }
        else {
            i = 2;
        }
        return i;
    }

    private Date getDate(String date, String time) throws ParseException {
        System.out.println("this is date " + date);
        System.out.println("this is time " + time);

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/YYYY HH:mm");
        String tempTime = time;
        if (time.contains("PM") || time.contains("AM")) {
            String oldHour = time.substring(0,2);
            int hourInt = Integer.parseInt(oldHour);
            hourInt = hourInt + 12;
            String newHour = Integer.toString(hourInt);
            tempTime = tempTime.replace(oldHour, newHour);
        }
        String fullDate = date;
        fullDate.concat(" ");
        fullDate.concat(tempTime.substring(0,tempTime.length()-3));
        return sdf.parse(fullDate);
    }

    private void createNotifications() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        // remove all previous alarms
        alarmMgr.cancel(alarmIntent);
        // then repopulate the alarms
        for (int i = 0; i < eventAlerts.length; i++) {
            if (setAlert[i]) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(eventAlerts[i]);
                alarmMgr.set(AlarmManager.RTC, cal.getTimeInMillis(), alarmIntent);
            }
        }
    }

}
