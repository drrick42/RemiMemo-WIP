package com.example.lanayusuf.remimemo;


import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by areally on 11/27/2016.
 */

public class PeriodicRepeat {

    private final static PeriodicRepeat INSTANCE = new PeriodicRepeat();

    private static boolean hasRun = false;
    private long repeatAtMidnight = 86400*1000L;
    private Calendar remindTime;

    private PeriodicRepeat(){
        if(!hasRun) {
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            long initialDelay = 0L;

            long someDelay = 60*1000;//Repeat in a minute
            scheduler.scheduleWithFixedDelay(updatePriorities, initialDelay, someDelay, TimeUnit.MILLISECONDS);
            hasRun = true;
        }
    }

    public boolean hasScheduled(){
        return hasRun;
    }

    public static PeriodicRepeat getInstance(){
        return INSTANCE;
    }

    final private Runnable updatePriorities = new Runnable() {
        @Override
        public void run() {
            try{
                updatePriority();
            }catch (Throwable t){
                Log.d("+++PRIORITY HALT+++", "run: ");
                t.printStackTrace();
            }
        }
    };

    private void updatePriority(){
        List<EventRemimemo> eventList = EventDBHandler.getInstance().queryEvents("Low");
        Calendar rightNow = Calendar.getInstance();
        Date date;
        String someTime,someDate;

        long weekInMilliSec = 7 * 24 * 60 * 60 * 1000;

        if(eventList.size()>0){
            setRemindDate();
        }

        for (int i=0;i<eventList.size();i++){
            someDate  = eventList.get(i).getEditTxtDate();
            someTime = eventList.get(i).getEditTxtTime();
            date = RemiNotifier.getInstance().acquireDate(someDate,someTime);

            //If date exists
            if(date.getTime() != RemiNotifier.defaultTime){

                //If time difference since epoch is less than 7 days, update the event to high priority
                if(date.getTime() - rightNow.getTime().getTime() < weekInMilliSec){
                    eventList.get(i).setEventPriority("High");
                    EventDBHandler.getInstance().updateEvent(eventList.get(i));
                }
            }
        }

    }

    private void setRemindDate(){
        remindTime = Calendar.getInstance();

        //set HH:mm:ss:ms AM_PM to 00:00:00:00 AM and day of month += 1
        remindTime.set(Calendar.HOUR,0);
        remindTime.set(Calendar.MINUTE,0);
        remindTime.set(Calendar.SECOND,0);
        remindTime.set(Calendar.MILLISECOND,0);
        remindTime.set(Calendar.AM_PM,Calendar.AM);
        remindTime.add(Calendar.DAY_OF_MONTH,1);

        //Set it so that it always delays to 12 midnight every day.
        repeatAtMidnight = remindTime.getTime().getTime() - Calendar.getInstance().getTime().getTime();
    }


}
