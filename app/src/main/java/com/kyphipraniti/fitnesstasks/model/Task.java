package com.kyphipraniti.fitnesstasks.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

public class Task {

    private final DateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    private long id;
    private String title;
    private Date deadline;
    private long amount;
    private boolean completed;
    private String action;
    private final static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public Task(String deadline, long amount, String action, boolean completed) {
        Date parsedDeadline = null;
        this.amount = amount;
        this.completed = completed;
        this.action = action;

        try {
            parsedDeadline = iso8601Format.parse(deadline);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (parsedDeadline != null) {
            this.deadline = parsedDeadline;
        } else {
            this.deadline = new Date();
        }

    }

    private void writeTask(String deadline, long amount, String action) {
        DatabaseReference taskRef = mDatabase.child("tasks").push();
        String key = taskRef.getKey();
        Task task = new Task(deadline, amount, action, false);

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

    private static long lastTaskId = 0;
    public static ArrayList<Task> createTasksList(int numTasks, boolean completed) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        ArrayList<Task> tasks = new ArrayList<Task>();
        for (int i = 1; i <= numTasks; i++) {
            long currTaskId = ++lastTaskId;
            String nowAsISO = df.format(new Date());

            tasks.add(new Task(
                    nowAsISO,
                    new Random().nextLong(),
                    "Task " + currTaskId,
                    i <= numTasks / 2));
        }

        return tasks;
    }

    public static ArrayList<Task> createCompletedTasksList(int numTasks) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        ArrayList<Task> tasks = new ArrayList<Task>();
        for (int i = 1; i <= numTasks; i++) {
            long currTaskId = ++lastTaskId;
            String nowAsISO = df.format(new Date());
            tasks.add(new Task(
                    nowAsISO,
                    currTaskId,
                    "Task " + currTaskId,
                    true));
        }

        return tasks;
    }
}
