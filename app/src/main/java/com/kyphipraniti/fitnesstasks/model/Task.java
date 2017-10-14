package com.kyphipraniti.fitnesstasks.model;

import android.database.Cursor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

class Task {

    private final DateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    private long id;
    private Date deadline;
    private long amount;
    private boolean completed;


    Task(Cursor cursor) {
        String datetime;
        try {
            this.id = cursor.getLong(cursor.getColumnIndex("_id"));
            datetime = cursor.getString(cursor.getColumnIndex("deadline"));
            this.deadline = iso8601Format.parse(datetime);
            this.amount = cursor.getLong(cursor.getColumnIndex("amount"));
            this.completed = (cursor.getInt(cursor.getColumnIndex("completed")) != 0);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    Task(long id, String deadline, long amount, boolean completed) {
        Date parsedDeadline = null;
        this.id = id;
        this.amount = amount;
        this.completed = completed;

        if (deadline != null) {
            try {
                parsedDeadline = iso8601Format.parse(deadline);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        this.deadline = parsedDeadline;
    }
}
