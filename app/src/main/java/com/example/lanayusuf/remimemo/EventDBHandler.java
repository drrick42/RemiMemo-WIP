package com.example.lanayusuf.remimemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.lanayusuf.remimemo.EventDbSchema.EventTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by areally on 11/5/2016.
 */

public class EventDBHandler {
    private final static EventDBHandler INSTANCE= new EventDBHandler();

    private static Context mContext;
    private static SQLiteDatabase mDatabase;

    private EventDBHandler(){
        //Only here for instantiation purpose
    }

    public static EventDBHandler getInstance(){
        return INSTANCE;
    }

    public static boolean isDatabaseExists(Context context){
        File file = context.getDatabasePath(EventHelper.getDbName());
        return file.exists();
    }

    public static void initializeDB(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new EventHelper(mContext).getWritableDatabase();
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

    public void addEvent(EventRemimemo event){
        ContentValues values = getContentValues(event);

        mDatabase.insert(EventTable.NAME,null,values);

    }

    public void updateEvent(EventRemimemo event){
        ContentValues values = getContentValues(event);
        String event_id = Long.toString(event.getEventId());

        mDatabase.update(EventTable.NAME, values,EventTable.Cols.EVENTID + " = ?", new String[]{event_id});
    }

    public void deleteEvent(EventRemimemo event){
        String event_id = Long.toString(event.getEventId());

        mDatabase.delete(EventTable.NAME, EventTable.Cols.EVENTID + " = ?", new String[]{event_id});
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
        List<EventRemimemo> events = new ArrayList<>();

        if(cursor.moveToFirst()){
            do {
                events.add(listOneEvent(cursor));
            } while (cursor.moveToNext());
        }

        return events;
    }

    private EventRemimemo listOneEvent(Cursor cursor){
        EventRemimemo oneEvent = new EventRemimemo();

        oneEvent.setEventId(cursor.getLong(cursor.getColumnIndex(EventTable.Cols.EVENTID)));
        oneEvent.setEventName(cursor.getString(cursor.getColumnIndex(EventTable.Cols.EVENTNAME)));
        oneEvent.setEventDescription(cursor.getString(cursor.getColumnIndex(EventTable.Cols.EVENTDESCRIPTION)));
        oneEvent.setEventPriority(cursor.getString(cursor.getColumnIndex(EventTable.Cols.PRIORITY)));
        oneEvent.setEditTxtDate(cursor.getString(cursor.getColumnIndex(EventTable.Cols.DATE)));
        oneEvent.setEditTxtTime(cursor.getString(cursor.getColumnIndex(EventTable.Cols.TIME)));
        oneEvent.setEventLocation(cursor.getString(cursor.getColumnIndex(EventTable.Cols.LOCATION)));

        return oneEvent;
    }

}
