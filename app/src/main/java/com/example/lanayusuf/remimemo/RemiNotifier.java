package com.example.lanayusuf.remimemo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Derrick on 11/12/2016.
 */
public class RemiNotifier {

    private RemiNotifier(int init){
        //Only here for instantiation purpose
        eventNames = new String[init];
        eventPriorities = new String[init];
        eventTimes = new String[init];
        eventDates = new String[init];
        eventAlerts = new Date[init];
        priorityAlertOptions = new int[3];
        setAlert = new boolean[init];
    }

    public static RemiNotifier getInstance(){
        return new RemiNotifier(LIMIT);
    }

    private static int LIMIT = 50;

    public String[] eventNames;
    public String[] eventPriorities;
    public String[] eventTimes;
    public String[] eventDates;
    public Date[] eventAlerts;
    public int[] priorityAlertOptions;
    public boolean[] setAlert;
    public static final long defaultTime = 0L;
    public static String event_name = "EVENT_NAME";
    public static String event_pri = "EVENT_PRI";

    public void setNotifications(Context context) {
        getEvents(context);
        setAlertTimes(context);
        createNotifications(context);
    }
    /**
    public void populateNotifications(Context context) {
        getEvents(context);
        setAlertTimes(context);
    }
     */

    private void getEvents(Context context) {
        int counter = 0;
        for (int i = 0; i < LIMIT; i++) {
            eventNames[i] = "";
            eventPriorities[i] = "";
            eventTimes[i] = "";
            eventDates[i] = "";
            setAlert[i] = false;
        }

        EventDBHandler.initializeDB(context);
        if (EventDBHandler.isDatabaseExists(context)) {
            EventRemimemo event;
            List<EventRemimemo> eventRemimemoList = EventDBHandler.getInstance().queryEvents("High");
            for (int i = 0; i < eventRemimemoList.size(); i++) {
                if (counter < LIMIT) {
                    event = eventRemimemoList.get(i);
                    eventNames[counter] = event.getEventName();
                    eventPriorities[counter] = "High";
                    eventTimes[counter] = event.getEditTxtTime();
                    eventDates[counter] = event.getEditTxtDate();
                    counter++;
                }
            }

            eventRemimemoList = EventDBHandler.getInstance().queryEvents("Low");
            for (int i = 0; i < eventRemimemoList.size(); i++) {
                if (counter < LIMIT) {
                    event = eventRemimemoList.get(i);
                    eventNames[counter] = event.getEventName();
                    eventPriorities[counter] = "Low";
                    eventTimes[counter] = event.getEditTxtTime();
                    eventDates[counter] = event.getEditTxtDate();
                    counter++;
                }
            }

            eventRemimemoList = EventDBHandler.getInstance().queryEvents("None");
            for (int i = 0; i < eventRemimemoList.size(); i++) {
                if (counter < LIMIT) {
                    event = eventRemimemoList.get(i);
                    eventNames[counter] = event.getEventName();
                    eventPriorities[counter] = "None";
                    eventTimes[counter] = event.getEditTxtTime();
                    eventDates[counter] = event.getEditTxtDate();
                    counter++;
                }
            }
        }
    }

    private void setAlertTimes(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        priorityAlertOptions[0] = Integer.parseInt(settings.getString("high_pri_alert_pref", "1"));
        priorityAlertOptions[1] = Integer.parseInt(settings.getString("low_pri_alert_pref", "1"));
        priorityAlertOptions[2] = Integer.parseInt(settings.getString("no_pri_alert_pref", "1"));
        for (int i = 0; i < eventNames.length; i++) {
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
                if (!eventTimes[i].contains("mm") && !eventDates[i].contains("MM")
                        && eventTimes[i].length() > 0 && eventDates[i].length() > 0) {
                    try {
                        if (eventDates[i].length() > 0 && eventTimes[i].length() > 0) {

                            Date event_date = getDate(eventDates[i], eventTimes[i]);

                            Calendar cal = Calendar.getInstance();
                            cal.setTime(event_date);
                            int time_change = priorityAlertOptions[priority_type] * -1;
                            cal.add(Calendar.HOUR_OF_DAY, time_change);

                            Date current_time = Calendar.getInstance().getTime();
                            if (priorityAlertOptions[priority_type] != 0) {
                                long diff = cal.getTime().getTime() - current_time.getTime();
                                if (diff > 0) {
                                    setAlert[i] = true;
                                }
                            }
                            eventAlerts[i] = cal.getTime();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private Date getDate(String date, String time) throws ParseException {
        SimpleDateFormat dateTime = new SimpleDateFormat("MM/dd/yyyy hh:mm aa", Locale.US);
        SimpleDateFormat dateOnly = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        Date returnDate;
        if (time.length()==0 || time.contains("mm")) returnDate = dateOnly.parse(date);
        else {
            String fullDate = date;
            fullDate = fullDate.concat(" ");
            fullDate = fullDate.concat(time);
            returnDate = dateTime.parse(fullDate);
        }
        return returnDate;
    }

    private void createNotifications(Context context) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        for (int i = 0; i < LIMIT; i++) {
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra(event_name, eventNames[i]);
            intent.putExtra(event_pri, eventPriorities[i]);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, i, intent, 0);
            if (setAlert[i]) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(eventAlerts[i]);
                System.out.println("Event notification set to: " + cal.getTime().toString());
                alarmMgr.set(AlarmManager.RTC, cal.getTimeInMillis(), alarmIntent);
            } else {
                alarmMgr.cancel(alarmIntent);
            }
        }
    }

    //Made this here because didn't want getDate to be public
    public Date acquireDate(String someDate, String someTime){

        Date thisDate = Calendar.getInstance().getTime();
        thisDate.setTime(defaultTime);

        try {
            thisDate = getDate(someDate,someTime);

        }catch (ParseException e){
            Log.e("***ACQUIRE DATE***","Parse exception here: ");
            e.printStackTrace();
        }

        return thisDate;
    }


}
