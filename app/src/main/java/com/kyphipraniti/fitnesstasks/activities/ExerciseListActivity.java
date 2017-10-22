package com.kyphipraniti.fitnesstasks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kyphipraniti.fitnesstasks.R;
import com.kyphipraniti.fitnesstasks.adapters.ExerciseListAdapter;

import static com.kyphipraniti.fitnesstasks.model.ListOfExercises.aListExercise;

public class ExerciseListActivity extends AppCompatActivity implements ExerciseListAdapter.ExerciseListAdapterListener {

    private String clickedExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        RecyclerView rv = (RecyclerView) findViewById(R.id.rvExercises);
        rv.setHasFixedSize(true);

        ExerciseListAdapter adapter = new ExerciseListAdapter(this, aListExercise, this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onItemSelected(View view, int position) {
        clickedExercise = aListExercise.get(position);
        Intent intent = new Intent();
        intent.putExtra("exercise", clickedExercise);
        setResult(RESULT_OK, intent);
        finish();
    }

}
