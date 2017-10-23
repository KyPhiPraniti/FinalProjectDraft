package com.kyphipraniti.fitnesstasks.model;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kyphipraniti.fitnesstasks.utils.Constants;

@SuppressWarnings("serial")
public class Workout implements Serializable {
    private String workoutName;
    private ArrayList<String> workoutTasks;
    private static final FirebaseDatabase FIREBASE_DATABASE = FirebaseDatabase.getInstance();

    public Workout() {

    }

    public Workout(String workoutName, ArrayList workoutTasks) {
        this.workoutName = workoutName;
        this.workoutTasks = workoutTasks;
    }

    public static void writeWorkout(String workoutName, ArrayList workoutTasks) {
        DatabaseReference workoutRef = FIREBASE_DATABASE.getReference().child(Constants.FIREBASE_CHILD_WORKOUTS).child(getUid());

        Workout workout = new Workout(workoutName, workoutTasks);
        workoutRef.push().setValue(workout);
    }

    private static String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public ArrayList<String> getWorkoutTasks() {
        return workoutTasks;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public void setWorkoutTasks(ArrayList<String> workoutTasks) {
        this.workoutTasks = workoutTasks;
    }
}
