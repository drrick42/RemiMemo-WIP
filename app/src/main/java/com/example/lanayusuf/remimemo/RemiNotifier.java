package com.example.lanayusuf.remimemo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    public void setNotifications(Context context) {
        getEvents(context);
        setAlertTimes(context);
        createNotifications(context);
    }

    public void populateNotifications(Context context) {
        getEvents(context);
        setAlertTimes(context);
    }

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
        if (EventDBHandler.getInstance().isDatabaseExists(context)) {
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
        priorityAlertOptions[0] = getPrioritySetting(settings, "high_pri_alert_pref");
        priorityAlertOptions[1] = getPrioritySetting(settings, "low_pri_alert_pref");
        priorityAlertOptions[2] = getPrioritySetting(settings, "no_pri_alert_pref");
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
                            if (priorityAlertOptions[priority_type] == 24) {
                                // set eventDateAlerts back one day
                                cal.add(Calendar.DATE, -1);
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
    }

    private int getPrioritySetting(SharedPreferences pref, String priority) {
        String alertTime = pref.getString(priority, null);
        int i = 0;
        if (alertTime.contains("1")) {
            i = 1;
        } else if (alertTime.contains("24")) {
            i = 24;
        } else if (alertTime.contains("0")) {
            // i = 0
        } else {
            i = 2;
        }
        System.out.println(priority + " set to " + i);
        return i;
    }

    private Date getDate(String date, String time) throws ParseException {
        SimpleDateFormat dateTime = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
        SimpleDateFormat dateOnly = new SimpleDateFormat("MM/dd/yyyy");
        Date returnDate = new Date();
        if (time.length()==0 || time.contains("mm")) {
            returnDate = dateOnly.parse(date);
        } else {
            String fullDate = date;
            fullDate = fullDate.concat(" ");
            fullDate = fullDate.concat(time);
            returnDate = dateTime.parse(fullDate);
        }
        return returnDate;
    }

    private void createNotifications(Context context) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        for (int i = 0; i < LIMIT; i++) {
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


}
