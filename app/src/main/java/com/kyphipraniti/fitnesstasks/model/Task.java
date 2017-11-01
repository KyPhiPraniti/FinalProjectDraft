package com.kyphipraniti.fitnesstasks.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;
import com.kyphipraniti.fitnesstasks.utils.Constants;

import java.util.Comparator;
import java.util.Date;

@IgnoreExtraProperties
public class Task implements Comparator<Task>, Comparable<Task>, Parcelable {

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
    private static final FirebaseDatabase FIREBASE_DATABASE = FirebaseDatabase.getInstance();
    private String key;
    private String title;

    private Deadline deadline;
    @PropertyName("units")
    private String units;
    @PropertyName("amount")
    private long amount;
    @PropertyName("completed")
    private boolean completed;
    @PropertyName("action")
    private String action;

    protected Task(Parcel in) {
        key = in.readString();
        title = in.readString();
        units = in.readString();
        amount = in.readLong();
        completed = in.readByte() != 0;
        action = in.readString();
    }

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

    public static Task writeTask(String action, long amount, String units, Date deadline) {
        DatabaseReference taskRef;
        String key;
        Task task;

        taskRef = FIREBASE_DATABASE.getReference().child(Constants.FIREBASE_CHILD_TASKS).child(getUid()).push();
        key = taskRef.getKey();
        task = new Task(action, amount, units, deadline, key, false);
        taskRef.setValue(task);
        return task;
    }

    private static String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Deadline getDeadline() {
        return deadline;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
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
        } else if (thisTime < taskTime) {
            return -1;
        } else {
            return 0;
        }
    }

    public String getFormattedDeadline(Deadline deadline) {
        return String.format("%02d : %02d", deadline.getHour(), deadline.getMin());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.getKey());
        parcel.writeString(this.getTitle());
        parcel.writeString(this.getUnits());
        parcel.writeLong(this.getAmount());
        parcel.writeByte((byte) (this.getCompleted() ? 1 : 0));
        parcel.writeString(this.getAction());
    }

    public boolean getCompleted() { return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getKey() {
        return key;
    }
}
