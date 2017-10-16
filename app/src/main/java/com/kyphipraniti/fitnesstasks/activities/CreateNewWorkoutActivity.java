package com.kyphipraniti.fitnesstasks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.kyphipraniti.fitnesstasks.R;

public class CreateNewWorkoutActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnAddExercise;
    private Button btnSaveWorkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_workout);

        btnAddExercise= (Button) findViewById(R.id.btnAdd);
        btnSaveWorkout= (Button) findViewById(R.id.btnSave);

        btnAddExercise.setOnClickListener(this);
        btnSaveWorkout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Button b = (Button) v;
        switch(b.getId()) {
            case R.id.btnAdd:
                Intent intent = new Intent(this, ExerciseListActivity.class);
                startActivity(intent);
                break;
            case R.id.btnSave:
                break;
        }
    }
}
