package com.example.lanayusuf.remimemo;

import android.widget.Button;


/**
 * Created by areally on 11/7/2016.
 */

public class DynamicButton {
    private EventRemimemo oneEvent;
    private Button button;
    private int buttonId;

    public DynamicButton(EventRemimemo event, int id, Button classButton){

        oneEvent = event;
        button = classButton;
        buttonId = id;
    }

    public Button getButton(){
        return button;
    }

    public EventRemimemo getEvent(){
        return oneEvent;
    }

    public int getButtonId(){
        return buttonId;
    }
}
