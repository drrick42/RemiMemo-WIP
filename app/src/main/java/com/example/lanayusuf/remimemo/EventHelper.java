package com.example.lanayusuf.remimemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.lanayusuf.remimemo.EventDbSchema.EventTable;

/**
 * Created by areally on 11/5/2016.
 */

public class EventHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "event.db";

    public EventHelper(Context context){
        super(context,DATABASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table "+ EventTable.NAME + "(" +
                EventTable.Cols.EVENTID + " integer primary key autoincrement, " +
                EventTable.Cols.EVENTNAME + ", " + EventTable.Cols.EVENTDESCRIPTION + ", " +
                EventTable.Cols.PRIORITY + ", " + EventTable.Cols.DATE + ", " +
                EventTable.Cols.TIME + ", " + EventTable.Cols.LOCATION + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
