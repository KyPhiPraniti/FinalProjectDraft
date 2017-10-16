package com.kyphipraniti.fitnesstasks.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.Task;
import com.kyphipraniti.fitnesstasks.R;

public class AddTaskFragment extends DialogFragment {

    private OnTaskAddListener mListener;
    private Button mAddTaskButton;

    public AddTaskFragment() {
        // Required empty public constructor
    }

    public static AddTaskFragment newInstance() {
        AddTaskFragment fragment = new AddTaskFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_task, container, false);
        setupViews(view);
        return view;
    }

    private void setupViews(View view) {
        mAddTaskButton = view.findViewById(R.id.btnAddTask);
        mAddTaskButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void onAddTaskButtonClick() {
        if (mListener != null) {
        }
        dismiss();
    }

    public interface OnTaskAddListener {
        void onTaskAdd(Task task);
    }
}
