package com.example.lanayusuf.remimemo;

import android.content.Intent;
import android.view.View;

/**
 * Created by LanaYusuf on 10/19/2016.
 */
public class HighPriority extends Priority{

    public HighPriority(){
        setPriority("High");
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this,EditEvent.class);
        intent.putExtra("SUPER_CLASS_PRIORITY",getPriority());

        switch (v.getId())
        {
            case R.id.btnAddEvent:
                //bring to edit event screen

                startActivity(intent);
                break;

            case R.id.btnBackPriority:
                //bring to Main Options screen
                startActivity(new Intent(this, MainActivity.class));
                break;
            default:
                for(int i=0;i<allButton.size();i++){
                    if(v.getId()==allButton.get(i).getButtonId()){
                        intent.putExtra("EVENT_ID",allButton.get(i).getEvent().getEventId());
                        intent.putExtra("EVENT_NAME",allButton.get(i).getEvent().getEventName());
                        intent.putExtra("EVENT_DESCRIPTION",allButton.get(i).getEvent().getEventDescription());
                        intent.putExtra("EVENT_PRIORITY",allButton.get(i).getEvent().getEventPriority());
                        intent.putExtra("EVENT_DATE",allButton.get(i).getEvent().getEditTxtDate());
                        intent.putExtra("EVENT_TIME",allButton.get(i).getEvent().getEditTxtTime());
                        intent.putExtra("EVENT_LOCATION",allButton.get(i).getEvent().getEventLocation());

                        startActivity(intent);
                        break;
                    }
                }
        }
    }

}
