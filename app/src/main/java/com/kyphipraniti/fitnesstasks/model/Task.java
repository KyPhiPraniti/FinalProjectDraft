package com.kyphipraniti.fitnesstasks.model;

import java.util.Comparator;
import java.util.Date;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;
import com.kyphipraniti.fitnesstasks.utils.Constants;

@IgnoreExtraProperties
public class Task implements Comparator<Task>, Comparable<Task> {

    private static final FirebaseDatabase FIREBASE_DATABASE = FirebaseDatabase.getInstance();

    private static int lastTaskId = 0;
    private String title;
    private String key;

    private Deadline deadline;
    @PropertyName("units")
    private String units;
    @PropertyName("amount")
    private long amount;
    @PropertyName("completed")
    private boolean completed;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @PropertyName("action")
    private String action;

    public Task() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Task(String action, long amount, String units, Date deadline, String key, boolean completed) {
        this.units = units;
        this.amount = amount;
        this.completed = completed;
        this.action = action;
        this.key = key;
        this.deadline = new Deadline(deadline);
    }

    public static void writeTask(String action, long amount, String units, Date deadline) {
        DatabaseReference taskRef = FIREBASE_DATABASE.getReference().child(Constants.FIREBASE_CHILD_TASKS).child(getUid()).push();

        String key = taskRef.getKey();
        Task task = new Task(action, amount, units, deadline, key, false);
        taskRef.setValue(task);
    }

    private static String getUid() {
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

    public Deadline getDeadline() {
        return deadline;
    }

    public String getTitle() {
        return title;
    }

//    public static ArrayList<Task> createTasksList(int numTasks, boolean completed) {
//        TimeZone tz = TimeZone.getTimeZone("UTC");
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'", Locale.getDefault()); // Quoted "Z" to indicate UTC, no timezone offset
//        df.setTimeZone(tz);
//        ArrayList<Task> tasks = new ArrayList<>();
//        for (int i = 1; i <= numTasks; i++) {
//            long currTaskId = ++lastTaskId;
//
//            tasks.add(new Task("Task " + currTaskId,
//                    new Random().nextLong(),
//                    "Task " + currTaskId,
//                    new Date(),
//                    i <= numTasks / 2));
//        }
//
//        return tasks;
//    }

//    public static ArrayList<Task> createCompletedTasksList(int numTasks) {
//        TimeZone tz = TimeZone.getTimeZone("UTC");
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'", Locale.getDefault()); // Quoted "Z" to indicate UTC, no timezone offset
//        df.setTimeZone(tz);
//        ArrayList<Task> tasks = new ArrayList<>();
//        for (int i = 1; i <= numTasks; i++) {
//            long currTaskId = ++lastTaskId;
//            tasks.add(new Task("Task " + currTaskId,
//                    new Random().nextLong(),
//                    "Task " + currTaskId,
//                    new Date(),
//                    i <= numTasks / 2));
//        }
//
//        return tasks;
//    }

    public String getUnits() {
        return units;
    }

    @Override
    public int compare(Task t1, Task t2) {
        long t1Time = t1.getDeadline().getTimestamp();
        long t2Time = t2.getDeadline().getTimestamp();
        if (t1Time > t2Time) {
            return 1;
        } else if (t1Time < t2Time) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public int compareTo(@NonNull Task task) {
        long thisTime = this.getDeadline().getTimestamp();
        long taskTime = task.getDeadline().getTimestamp();
        if (thisTime > taskTime) {
            return 1;
        } else if (thisTime  < taskTime) {
            return -1;
        } else {
            return 0;
        }
    }

    public String getFormattedDeadline(Deadline deadline) {
        String Time = String.format("%02d : %02d", deadline.getHour(), deadline.getMin());
        return Time;
    }

    public String getKey() {
        return key;
    }
}
