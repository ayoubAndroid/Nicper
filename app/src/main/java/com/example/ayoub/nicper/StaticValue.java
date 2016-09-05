package com.example.ayoub.nicper;

import com.example.ayoub.nicper.Object.Message.Time;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by hr2 on 29/07/2016.
 */
public class StaticValue {


    public StaticValue(){}

    public Time getTimeZone(){
        TimeZone tz = TimeZone.getTimeZone("GMT-11:00");
        Calendar c = Calendar.getInstance(tz);
        Time time = new Time( c.get(Calendar.MONTH) , c.get(Calendar.DAY_OF_MONTH),c.get(Calendar.HOUR),c.get(Calendar.MINUTE));
        return time;
    }
}
