package com.example.ayoub.nicper.Object.Message;

/**
 * Created by Admin on 04/06/2016.
 */
public class Time {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int seconde;

    public Time(){}

    public Time(int year, int month, int day, int hour, int minute, int seconde) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.seconde = seconde;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSeconde() {
        return seconde;
    }
}
