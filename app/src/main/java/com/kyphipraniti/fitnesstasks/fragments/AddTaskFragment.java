package com.kyphipraniti.fitnesstasks.fragments;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kyphipraniti.fitnesstasks.R;
import com.kyphipraniti.fitnesstasks.model.Task;
import com.kyphipraniti.fitnesstasks.receivers.WakefulReceiver;
import com.kyphipraniti.fitnesstasks.utils.Utils;

public class AddTaskFragment extends DialogFragment {

    private DatePicker mDeadlineDatePicker;
    private EditText mActionEditText;
    private EditText mAmountEditText;
    private EditText mUnitsEditText;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private final static DateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'", Locale.getDefault()); // Quoted
    // "Z" to indicate UTC, no timezone offset
    private TimePicker mDeadlineTimePicker;


    public AddTaskFragment() {
        // Required empty public constructor
    }

    private void createNotification(Task task) {
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
        getDialog().setTitle("Add Task");
        View view = inflater.inflate(R.layout.fragment_add_task, container, false);
        setupViews(view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialogFragmentStyle);

        super.onCreate(savedInstanceState);
    }

    private void setupViews(View view) {
        Button mAddTaskButton = view.findViewById(R.id.btnAddTask);
        mDeadlineDatePicker = view.findViewById(R.id.dpDeadline);
        mDeadlineTimePicker = view.findViewById(R.id.tpDeadline);
        mActionEditText = view.findViewById(R.id.etAction);
        mAmountEditText = view.findViewById(R.id.etAmount);
        mUnitsEditText = view.findViewById(R.id.etUnits);

        mAddTaskButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Date dateTime = Utils.getDateTimeFromDateTimePicker(mDeadlineDatePicker, mDeadlineTimePicker);

                String action = mActionEditText.getText().toString();
                String amountStr = mAmountEditText.getText().toString();
                long amount;
                if (amountStr.equals("")) {
                    amount = 0;
                } else {
                    amount = Integer.parseInt(mAmountEditText.getText().toString());
                }
                String units = mUnitsEditText.getText().toString();

                Task writtenTask = Task.writeTask(action, amount, units, dateTime);

                WakefulReceiver wakefulReceiver = new WakefulReceiver();
                wakefulReceiver.setAlarm(getContext(), writtenTask);
                dismiss();
            }

        });
    }
}
