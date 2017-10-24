package com.kyphipraniti.fitnesstasks.activities;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.kyphipraniti.fitnesstasks.R;
import com.kyphipraniti.fitnesstasks.model.Workout;

public class CreateNewWorkoutActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnAddExercise;
    private Button btnSaveWorkout;
    private EditText etWorkoutTitle;
    private Workout workout;
    private final int REQUEST_CODE = 20;
    ArrayAdapter<String> adapter;
    ArrayList<String> listItems = new ArrayList<>();
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_workout);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);

        if (mListView == null) {
            mListView = (ListView) findViewById(R.id.lvExercises);
        }

        btnAddExercise = (Button) findViewById(R.id.btnAdd);
        btnSaveWorkout = (Button) findViewById(R.id.btnSave);
        etWorkoutTitle = (EditText) findViewById(R.id.etWorkoutName);

        btnAddExercise.setOnClickListener(this);
        btnSaveWorkout.setOnClickListener(this);

        workout = new Workout();
    }

    protected ListView getListView() {
        if (mListView == null) {
            mListView = (ListView) findViewById(R.id.lvExercises);
        }
        return mListView;
    }

    protected void setListAdapter(ListAdapter adapter) {
        getListView().setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        Button b = (Button) v;
        switch (b.getId()) {
            case R.id.btnAdd:
                Intent intent = new Intent(this, ExerciseListActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.btnSave:
                String workoutName = etWorkoutTitle.getText().toString();
                workout.setWorkoutName(workoutName);
                workout.setWorkoutTasks(listItems);
                Intent i = new Intent();
                i.putExtra("workout", workout);
                setResult(RESULT_OK, i);

                Workout.writeWorkout(workoutName, listItems);

                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String name = data.getExtras().getString("exercise");

            //Add chosen exercises to New Workout
            if (name != null) {
                listItems.add(name);
                setListAdapter(adapter);
                adapter.notifyDataSetChanged();
                getListView().smoothScrollToPosition(listItems.size());
                Toast.makeText(getApplication(), name + " Added", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
