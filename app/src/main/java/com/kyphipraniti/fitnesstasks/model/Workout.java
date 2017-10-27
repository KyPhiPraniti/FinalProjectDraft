package com.kyphipraniti.fitnesstasks.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kyphipraniti.fitnesstasks.R;
import com.kyphipraniti.fitnesstasks.utils.Constants;

@SuppressWarnings("serial")
public class Workout implements Serializable {
    private String mWorkoutName;
    private int mThumbnailDrawable;
    private ArrayList<String> mWorkoutTasks;

    private static final FirebaseDatabase FIREBASE_DATABASE = FirebaseDatabase.getInstance();

    public Workout(String workoutName, int thumbnailDrawable, ArrayList workoutTasks) {
        this.mWorkoutName = workoutName;
        this.mThumbnailDrawable = thumbnailDrawable;
        this.mWorkoutTasks = workoutTasks;
    }

    public Workout() {

    }

    public int getThumbnailDrawable() {
        return mThumbnailDrawable;
    }

    public void setThumbnailDrawable(int mThumbnailDrawable) {
        this.mThumbnailDrawable = mThumbnailDrawable;
    }

    public static void writeWorkout(String workoutName, int thumbnailDrawable, ArrayList workoutTasks) {
        DatabaseReference workoutRef = FIREBASE_DATABASE.getReference().child(Constants.FIREBASE_CHILD_WORKOUTS).child(getUid());

        Workout workout = new Workout(workoutName, thumbnailDrawable, workoutTasks);
        workoutRef.push().setValue(workout);
    }

    public static List<Workout> getWorkouts() {
        List<Workout> workouts = new ArrayList<>();
        workouts.add(new Workout("Core Workout", R.drawable.core, new ArrayList<>(Arrays.asList("Bicycle Crunches. 3 sets, 12 "
            + "reps", "hanging Leg Raise. 3 sets, 12 reps", "Back Extenxion. 3 sets, 12 reps", "Plank. 3 minutes"))));
        workouts.add(new Workout("Chest Workout", R.drawable.chest_workout, new ArrayList<>(Arrays.asList("DumbBell bench " +
            "press. 3 sets, 12 reps", "Barbell incline bench press. 3 sets, 12 reps", "Incline dumbBell fly. 3 sets, 12 reps"))));
        workouts.add(new Workout("Leg Workout", R.drawable.leg_workout, new ArrayList<>(Arrays.asList("Barbell Squat. 4 sets, "
            + "4-6 reps", "Dumbbell Lunges. 4 sets, 12 reps each leg", "Leg Press. 3 sets, 12-15 reps", "Lying Leg Curls. 3 " +
            "sets, 12 reps", "Leg Extensions. 3 sets, 20 reps"))));
        workouts.add(new Workout("Full Body Workout", R.drawable.full_body_workout, new ArrayList<>(Arrays.asList("Cable Curl. " +
            "" + "" + "3 sets, 20 reps", "Seated Row. 3 sets, 20 reps"))));
        workouts.add(new Workout("Back Workout", R.drawable.back_workout, new ArrayList<>(Arrays.asList("Wide-Grip Lat " +
            "Pulldown. " + "3 sets, 12 reps", "Underhand Cable Pulldowns. 3 sets, 8 reps", "Bent Over Two-Dumbbell Row With " +
            "Palms In. 3 sets, 12 reps", "Stiff-Legged Barbell Deadlift. 3 sets, 8 reps"))));
        workouts.add(new Workout("Triceps Workout", R.drawable.triceps_workout, new ArrayList<>(Arrays.asList("Close-Grip " +
            "Barbell Bench Press. 3 sets, 8 reps", " Standing Overhead Barbell Triceps Extension. 3 sets, 8 reps", "Triceps Pushdown. 3 " +
            "sets, 8 reps"))));

        return workouts;
    }

    private static String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public String getWorkoutName() {
        return mWorkoutName;
    }

    public ArrayList<String> getWorkoutTasks() {
        return mWorkoutTasks;
    }

    public void setWorkoutName(String workoutName) {
        this.mWorkoutName = workoutName;
    }

    public void setWorkoutTasks(ArrayList<String> workoutTasks) {
        this.mWorkoutTasks = workoutTasks;
    }
}
