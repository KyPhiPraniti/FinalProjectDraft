package com.kyphipraniti.fitnesstasks.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Task {

    private final DateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    private long id;
    private Date deadline;
    private long amount;
    private boolean completed;
    private String action;
    private final static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public Task(String deadline, long amount, String action) {
        System.out.println(deadline);
        Date parsedDeadline = null;
        this.amount = amount;
        this.completed = false;
        this.action = action;

        try {
            parsedDeadline = iso8601Format.parse(deadline);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(parsedDeadline);

        if (parsedDeadline != null) {
            this.deadline = parsedDeadline;
        } else {
            try {
                this.deadline = iso8601Format.parse("2017-07-07 8:00PM");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeTask(String deadline, long amount, String action) {
        DatabaseReference taskRef = mDatabase.child("tasks").push();
        String key = taskRef.getKey();
        Task task = new Task(deadline, amount, action);

        mDatabase.setValue(key, task);
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public String getAction() {
        return action;
    }

    public long getAmount() {
        return amount;
    }

    public boolean isCompleted() {
        return completed;
    }

    public Date getDeadline() {
        return deadline;
    }
}
