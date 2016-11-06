package com.example.lanayusuf.remimemo;

/**
 * Created by areally on 11/5/2016.
 */

public class EventDbSchema {
    public static final class EventTable{
        public static final String NAME = "eventTable";

        public static final class Cols{
            public static final String EVENTID = "eventId";
            public static final String EVENTNAME = "name";
            public static final String EVENTDESCRIPTION = "description";
            public static final String PRIORITY = "priority";
            public static final String DATE = "date";
            public static final String TIME = "time";
            public static final String LOCATION = "location";
        }
    }
}
