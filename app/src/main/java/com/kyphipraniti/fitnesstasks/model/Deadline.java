package com.kyphipraniti.fitnesstasks.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Deadline {

    private final static DateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'", Locale.getDefault()); // Quoted "Z" to indicate UTC, no timezone offset
    private final static Calendar CALENDAR = Calendar.getInstance();

    private int year;
    private int month;
    private int day;
    private int hour;
    private int min;
    private long timestamp;

    public Deadline() {

    }

    public Deadline(Date date) {
        CALENDAR.setTime(date);
        this.year = CALENDAR.get(Calendar.YEAR);
        this.month = CALENDAR.get(Calendar.MONTH);
        this.day = CALENDAR.get(Calendar.DAY_OF_MONTH);
        this.hour = CALENDAR.get(Calendar.HOUR);
        this.min = CALENDAR.get(Calendar.MINUTE);
        this.timestamp = CALENDAR.getTimeInMillis();
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

    public int getMin() {
        return min;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return DATEFORMAT.format(CALENDAR.getTime());
    }
}

