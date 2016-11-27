package com.example.lanayusuf.remimemo;

import android.annotation.SuppressLint;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.app.Activity;

/**
 * Created by LanaYusuf on 11/27/2016.
 */

public class EventMapTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity mainActivity;
    Instrumentation.ActivityMonitor activityMonitor;
    Button button;

    public EventMapTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(true);
        mainActivity = getActivity();
        activityMonitor = getInstrumentation().addMonitor(EventMap.class.getName(), null, false);
        button = (Button) mainActivity.findViewById(R.id.btnEventMap);
    }

    @SuppressLint("NewApi")
    public void testUI() {

        try {
            synchronized(this){
                wait(3000);
            }
        }
        catch(InterruptedException ex){
        }

        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.performClick();
            }
        });
        Activity nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);

        try {
            synchronized(this){
                wait(3000);
            }
        }
        catch(InterruptedException ex){
        }

        assertNotNull(nextActivity);
        nextActivity .finish();
    }
}
