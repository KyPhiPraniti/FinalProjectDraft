package com.kyphipraniti.fitnesstasks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.kyphipraniti.fitnesstasks.R;

public class WorkoutDetailActivity extends AppCompatActivity {
    ImageView selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_detail);
        selectedImage = (ImageView) findViewById(R.id.selectedImage);
        Intent intent = getIntent();
        selectedImage.setImageResource(intent.getIntExtra("image", 0));
    }
}
