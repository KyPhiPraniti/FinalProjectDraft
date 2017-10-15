package com.kyphipraniti.fitnesstasks.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kyphipraniti.fitnesstasks.R;
import com.kyphipraniti.fitnesstasks.adapters.ExerciseListAdapter;

import static com.kyphipraniti.fitnesstasks.model.ListOfExercises.aListExercise;

public class ExerciseListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        RecyclerView rv = (RecyclerView) findViewById(R.id.rvExercises);
        rv.setHasFixedSize(true);

        ExerciseListAdapter adapter = new ExerciseListAdapter(this, aListExercise);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }
}
