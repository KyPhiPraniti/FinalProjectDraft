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

import com.kyphipraniti.fitnesstasks.model.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.kyphipraniti.fitnesstasks.R;
import com.kyphipraniti.fitnesstasks.adapters.TasksAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment implements DatePicker.OnDateChangedListener {

    private TasksAdapter mTasksAdapter;
    private List<Task> mTasks;
    private DatePicker mDatePicker;
    private RecyclerView mRvTasks;
    private LinearLayoutManager mLinearLayoutManager;
    private final static DateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    private OnFragmentInteractionListener mListener;
    private FloatingActionButton mFabAdd;

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
        setupDatePicker(view);
        setupRecyclerView(view);
        setupFloatingActionButton(view);
        initiateTasks();
        super.onViewCreated(view ,savedInstanceState);
    }

    private void setupDatePicker(View view) {
        mDatePicker = view.findViewById(R.id.datePicker);
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        System.out.println(year);
        System.out.println(month);
        System.out.println(day);

        mDatePicker.init(year, month, day, this);
    }

    private void setupRecyclerView(View view) {
        mTasks = new ArrayList<>();
        mTasksAdapter = new TasksAdapter(mTasks);
        mLinearLayoutManager = new LinearLayoutManager(getContext());

        mRvTasks = view.findViewById(R.id.rvTasks);
        mRvTasks.setAdapter(mTasksAdapter);
        mRvTasks.setLayoutManager(mLinearLayoutManager);
    }

    private void setupFloatingActionButton(View view) {
        mFabAdd = view.findViewById(R.id.fabAdd);
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

    private void initiateTasks() {
        Calendar c = Calendar.getInstance();
        Date dateToday = c.getTime();
        String dayToday = DATEFORMAT.format(dateToday);
        for (int i = 0; i < 10; ++i) {
            Task task = new Task(dayToday, (int) (Math.random() * 20) + 1, "Bench Press");
            mTasks.add(task);
            mTasksAdapter.notifyItemInserted(mTasks.size() - 1);
        }

        // Initiate with fake tasks for now.



//        DatabaseReference tasksReference = mDatabase.getReference("tasks");
//
//        Calendar c = Calendar.getInstance();
//        Date dateToday = c.getTime();
//        String dayStart = DATEFORMAT.format(dateToday);
//        c.add(Calendar.DATE, 1);
//        Date dateTomorrow = c.getTime();
//        String dayEnd = DATEFORMAT.format(dateTomorrow);
//        tasksReference.child().startAt(dayStart).endAt(dayEnd).addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Task task = dataSnapshot.getValue(Task.class);
//                if (task != null) {
//                    System.out.println("Action: " + task.getAction());
//                }
//
//                mTasks.add(task);
//                mTasksAdapter.notifyItemInserted(mTasks.size() - 1);
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
        mTasks.clear();
        mTasksAdapter.notifyDataSetChanged();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        String taskDay = DATEFORMAT.format(calendar.getTime());

        generateData(taskDay);
    }

    private void generateData(String taskDay) {
        String[] workoutTasks = {"Squat", "Bench", "Deadlift", "Overhead Press" };

        for (int i = 0; i < (Math.random() * 10) + 1; ++i) {
            Task task = new Task(taskDay, (int) (Math.random() * 20) + 1, workoutTasks[(int) (Math.random() * 3)]);

            mTasks.add(task);
            mTasksAdapter.notifyItemInserted(mTasks.size() - 1);
        }

    }

    interface OnFragmentInteractionListener {

    }
}
