package com.example.lanayusuf.remimemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.lanayusuf.remimemo.EventDbSchema.EventTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by areally on 11/5/2016.
 */

public class EventDBHandler {
    private final static EventDBHandler INSTANCE= new EventDBHandler();

    private static Context mContext;
    private static SQLiteDatabase mDatabase;
    private static boolean databaseExists = false;

    private EventDBHandler(){
        //Only here for instantiation purpose
    }

    public static EventDBHandler getInstance(){
        return INSTANCE;
    }

    public boolean isDatabaseExists(){ return databaseExists; }

    public void initializeDB(Context context){
        if(!databaseExists) {
            mContext = context.getApplicationContext();
            mDatabase = new EventHelper(mContext).getWritableDatabase();
            databaseExists = true;
        }
    }

    private static ContentValues getContentValues(EventRemimemo event){
        ContentValues values = new ContentValues();

        Log.d("+++INPUT NAME+++: ",event.getEventName());
        values.put(EventTable.Cols.EVENTNAME, event.getEventName());

        Log.d("+++INPUT DES+++: ",event.getEventDescription());
        values.put(EventTable.Cols.EVENTDESCRIPTION, event.getEventDescription());
        values.put(EventTable.Cols.PRIORITY, event.getEventPriority());

        Log.d("+++INPUT DATE+++: ",event.getEditTxtDate());
        values.put(EventTable.Cols.DATE, event.getEditTxtDate());
        values.put(EventTable.Cols.TIME,event.getEditTxtTime());
        values.put(EventTable.Cols.LOCATION, event.getEventLocation());

        return values;
    }

    public void addOrUpdateEvent(EventRemimemo event){
        ContentValues values = getContentValues(event);

        mDatabase.insert(EventTable.NAME,null,values);

        //String event_id = Integer.toString(event.getEventId());
        //mDatabase.update(EventTable.NAME, values,EventTable.Cols.EVENTID + " = ?", new String[]{event_id});
    }

    public List<EventRemimemo> queryEvents(String priority){
        String whereClause = EventTable.Cols.PRIORITY + " = ?";
        String[] whereArgs = new String[]{priority};

        Cursor cursor = mDatabase.query(
                EventTable.NAME,
                null, //Columns = null means select all columns
                whereClause,
                whereArgs,
                null, //groupBy
                null, //having
                null //orderBy
        );

        return listEvent(cursor);
    }

    private List<EventRemimemo> listEvent(Cursor cursor){
        List<EventRemimemo> events = new ArrayList<EventRemimemo>();

        if(cursor.moveToFirst()){
            do {
                EventRemimemo oneEvent = new EventRemimemo();

                oneEvent.setEventName(cursor.getString(cursor.getColumnIndex(EventTable.Cols.EVENTNAME)));
                oneEvent.setEventDescription(cursor.getString(cursor.getColumnIndex(EventTable.Cols.EVENTDESCRIPTION)));
                oneEvent.setEventPriority(cursor.getString(cursor.getColumnIndex(EventTable.Cols.PRIORITY)));
                oneEvent.setEditTxtDate(cursor.getString(cursor.getColumnIndex(EventTable.Cols.DATE)));
                oneEvent.setEditTxtTime(cursor.getString(cursor.getColumnIndex(EventTable.Cols.TIME)));
                oneEvent.setEventLocation(cursor.getString(cursor.getColumnIndex(EventTable.Cols.LOCATION)));

                events.add(oneEvent);
            } while (cursor.moveToNext());
        }

        return events;
    }

}
