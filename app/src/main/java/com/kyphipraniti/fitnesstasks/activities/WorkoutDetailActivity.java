package com.kyphipraniti.fitnesstasks.activities;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kyphipraniti.fitnesstasks.R;
import com.kyphipraniti.fitnesstasks.model.Workout;

//TODO: Need to change to DialogFragment
public class WorkoutDetailActivity extends AppCompatActivity {
    private ImageView selectedImage;
    private Workout mWorkout;
    private TextView tvName;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> listItems = new ArrayList<>();
    private ListView lvExercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_detail);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);

        selectedImage = (ImageView) findViewById(R.id.selectedImage);
        tvName = (TextView) findViewById(R.id.tvName);
        lvExercises = (ListView) findViewById(R.id.lvWorkoutExercise);

        mWorkout = (Workout) getIntent().getExtras().getSerializable("workout");

        Glide.with(WorkoutDetailActivity.this).load(mWorkout.getThumbnailDrawable()).centerCrop().into(selectedImage);

        tvName.setText(mWorkout.getWorkoutName());

        for (int i = 0; i < mWorkout.getWorkoutTasks().size(); i++) {
            listItems.add(mWorkout.getWorkoutTasks().get(i));
        }
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    protected void setListAdapter(ListAdapter adapter) {
        getListView().setAdapter(adapter);
    }

    protected ListView getListView() {
        if (lvExercises == null) {
            lvExercises = (ListView) findViewById(R.id.lvExercises);
        }
        return lvExercises;
    }
}
