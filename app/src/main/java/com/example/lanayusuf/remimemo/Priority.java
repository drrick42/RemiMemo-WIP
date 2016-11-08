package com.example.lanayusuf.remimemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by areally on 11/6/2016.
 */

public abstract class Priority extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout mLayout;
    private String mPriority;
    private List<EventRemimemo> eventRemimemoList;
    protected List<DynamicButton> allButton;

    public String getPriority(){
        return mPriority;
    }

    public void setPriority(String whichPriority){
        mPriority = whichPriority;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.priority);

        //If database not initialized, initialize it.
        EventDBHandler.getInstance().initializeDB(this);

        TextView priority = (TextView)findViewById(R.id.txtViewPriority);
        String setTxt = getPriority()+" Priority";
        priority.setText(setTxt);

        Button btnBack = (Button)findViewById(R.id.btnBackPriority);
        btnBack.setOnClickListener(this);

        Button btnAddEvent = (Button)findViewById(R.id.btnAddEvent);
        btnAddEvent.setOnClickListener(this);

        mLayout = (LinearLayout) findViewById(R.id.layout_Events);
        if( EventDBHandler.isDatabaseExists(this) ){
            printEvents();
        }
    }

    @Override
    public abstract void onClick(View v);

    private void printEvents(){
        eventRemimemoList = EventDBHandler.getInstance().queryEvents(getPriority());
        allButton = new ArrayList<>(eventRemimemoList.size());

        String nextLine = System.getProperty("line.separator");
        String fullEvent;
        int buttonId;

        for (int i=0;i<eventRemimemoList.size();i++){
            fullEvent = "";

            Button eventButtonData = new Button(this);
            buttonId = i;
            eventButtonData.setId(buttonId);

            fullEvent += eventRemimemoList.get(i).getEventName() + nextLine;
            fullEvent += eventRemimemoList.get(i).getEditTxtDate() + "    ";
            fullEvent += eventRemimemoList.get(i).getEditTxtTime();

            eventButtonData.setText(fullEvent);
            eventButtonData.setTextSize(TypedValue.COMPLEX_UNIT_SP,12f);
            eventButtonData.setBackgroundColor(Color.WHITE);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mLayout.addView(eventButtonData,lp);
            Button button_2 = (Button) findViewById(buttonId);
            button_2.setOnClickListener(this);

            DynamicButton dButton = new DynamicButton(eventRemimemoList.get(i),button_2.getId(),button_2);
            allButton.add(dButton);
        }

    }
}
