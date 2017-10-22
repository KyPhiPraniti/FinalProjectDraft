package com.kyphipraniti.fitnesstasks.fragments;

import java.util.ArrayList;
import java.util.Arrays;

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
import android.widget.Toast;

import com.kyphipraniti.fitnesstasks.R;
import com.kyphipraniti.fitnesstasks.activities.CreateNewWorkoutActivity;
import com.kyphipraniti.fitnesstasks.adapters.RVAdapter;

import static android.app.Activity.RESULT_OK;
import static com.kyphipraniti.fitnesstasks.R.id.rv;

public class WorkoutFragment extends Fragment {

    //TODO: Change according to Task Model
    ArrayList workoutNames = new ArrayList<>(Arrays.asList("Core Workout", "Chest Workout", "Leg Workout", "Full Body Workout"));
    ArrayList workoutImages = new ArrayList<>(Arrays.asList(R.drawable.core, R.drawable.chest_workout, R.drawable.leg_workout,
        R.drawable.full_body_workout));

    private Button mBtnCreateWorkout;
    private final int REQUEST_CODE = 10;
    private RVAdapter mAdapter;
    private RecyclerView mRv;

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

        mBtnCreateWorkout = view.findViewById(R.id.btn_createNewWorkout);

        mRv = view.findViewById(rv);
        mRv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        mRv.setLayoutManager(llm);

        mAdapter = new RVAdapter(getContext(), workoutNames, workoutImages);
        mRv.setAdapter(mAdapter);

        mBtnCreateWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreateNewWorkoutActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String name = data.getExtras().getString("workout");

            if (name != null) {
                int position = 0;
                workoutNames.add(position, name);
                workoutImages.add(position, R.drawable.placeholder);
                mAdapter.notifyItemInserted(position);
                mRv.scrollToPosition(position);
                Toast.makeText(getContext(), name + " Workout Added", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public interface OnFragmentInteractionListener {

    }
}
