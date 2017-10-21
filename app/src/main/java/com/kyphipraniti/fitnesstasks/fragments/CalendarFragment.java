package com.kyphipraniti.fitnesstasks.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kyphipraniti.fitnesstasks.R;
import com.kyphipraniti.fitnesstasks.adapters.TasksAdapter;
import com.kyphipraniti.fitnesstasks.model.Task;
import com.kyphipraniti.fitnesstasks.utils.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment implements DatePicker.OnDateChangedListener {

    private static Date currentDateView;
    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference dbReference = mDatabase.getReference();
    private TasksAdapter mTasksAdapter;
    private List<Task> mTasks;
    private List<Task> mAllTasks;
    private FirebaseUser currentUser;

    public CalendarFragment() {
    }

    public static CalendarFragment newInstance() {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        setupDatePicker(view);
        setupRecyclerView(view);
        setupFloatingActionButton(view);
        initiateTasks();
        super.onViewCreated(view, savedInstanceState);
    }

    private void setupDatePicker(View view) {
        DatePicker mDatePicker = view.findViewById(R.id.dpDeadline);
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        mDatePicker.init(year, month, day, this);
        currentDateView = new Date(cal.getTimeInMillis());
    }

    private void setupRecyclerView(View view) {
        mTasks = new ArrayList<>();
        mAllTasks = new ArrayList<>();
        mTasksAdapter = new TasksAdapter(mTasks);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());

        RecyclerView mRvTasks = view.findViewById(R.id.rvTasks);
        mRvTasks.setAdapter(mTasksAdapter);
        mRvTasks.setLayoutManager(mLinearLayoutManager);
    }

    private void setupFloatingActionButton(View view) {
        FloatingActionButton mFabAdd = view.findViewById(R.id.fabAdd);
        mFabAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                launchAddTaskFragment();
            }
        });
    }

    private void launchAddTaskFragment() {
        FragmentManager fm = getActivity().getSupportFragmentManager();

        AddTaskFragment addTaskFragment = AddTaskFragment.newInstance();
        addTaskFragment.show(fm, "add");

    }

    private long getStartDate() {
        Calendar c = Calendar.getInstance();
        c.setTime(currentDateView);
        return c.getTimeInMillis();
    }

    private long getEndDate() {
        Calendar c = Calendar.getInstance();
        c.setTime(currentDateView);
        c.add(Calendar.DATE, 1);
        return c.getTimeInMillis();
    }

    private void initiateTasks() {
        dbReference.child(Constants.FIREBASE_CHILD_TASKS)
                .child(currentUser.getUid())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Task task = dataSnapshot.getValue(Task.class);
                        if (task != null) {
                            System.out.println("Action: " + task.getAction());
                        }

                        mAllTasks.add(task);
                        mTasks.addAll(getDatesBetweenStartAndFinishWithFilter(getStartDate(), getEndDate()));
                        mTasksAdapter.notifyItemInserted(mTasks.size());
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private List<Task> getDatesBetweenStartAndFinishWithFilter(long start, long end) {
            List<Task> filteredList = new ArrayList<>();
            for (Task task : mAllTasks) {
                if (task.getDeadline().getTimestamp() > start && task.getDeadline().getTimestamp() < end) {
                    filteredList.add(task);
                }
            }
            return filteredList;
    }

    @Override
    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);

        currentDateView = c.getTime();

        mTasks.clear();

        mTasks.addAll(getDatesBetweenStartAndFinishWithFilter(getStartDate(), getEndDate()));
        mTasksAdapter.notifyDataSetChanged();

    }
}
