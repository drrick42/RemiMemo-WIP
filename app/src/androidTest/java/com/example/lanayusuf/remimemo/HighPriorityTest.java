package com.example.lanayusuf.remimemo;
import com.example.lanayusuf.remimemo.HighPriority;
import com.example.lanayusuf.remimemo.MainActivity;


import android.annotation.SuppressLint;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.view.MotionEvent;
import android.widget.Button;
import android.app.Activity;

/**
 * Created by LanaYusuf on 11/27/2016.
 */

public class HighPriorityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity mainActivity;
    Instrumentation.ActivityMonitor activityMonitor;
    Button button;

    public HighPriorityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(true);
        mainActivity = getActivity();
        activityMonitor = getInstrumentation().addMonitor(HighPriority.class.getName(), null, false);
        button = (Button) mainActivity.findViewById(R.id.btnHighPriority);
    }

    @SuppressLint("NewApi")
    public void testUI() {
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
