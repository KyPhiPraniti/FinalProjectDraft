package com.kyphipraniti.fitnesstasks.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kyphipraniti.fitnesstasks.R;
import com.kyphipraniti.fitnesstasks.activities.CreateNewWorkoutActivity;
import com.kyphipraniti.fitnesstasks.adapters.RVAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class WorkoutFragment extends Fragment {

    //TODO: Change according to Task Model
    ArrayList workoutNames = new ArrayList<>(Arrays.asList("Core Workout", "Chest Workout", "Leg Workout", "Full Body Workout"));
    ArrayList workoutImages = new ArrayList<>(Arrays.asList(R.drawable.core, R.drawable.chest_workout, R.drawable.leg_workout,
            R.drawable.full_body_workout));

    private Button btnCreateWorkout;

    private OnFragmentInteractionListener mListener;

    public WorkoutFragment() {
    }

    public static WorkoutFragment newInstance() {
        WorkoutFragment fragment = new WorkoutFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_workout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnCreateWorkout = view.findViewById(R.id.btn_createNewWorkout);

        RecyclerView rv = view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        RVAdapter adapter = new RVAdapter(getContext(), workoutNames, workoutImages);
        rv.setAdapter(adapter);


        btnCreateWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreateNewWorkoutActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

    }
}
