package com.kyphipraniti.fitnesstasks.fragments;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kyphipraniti.fitnesstasks.R;
import com.kyphipraniti.fitnesstasks.adapters.TasksAdapter;
import com.kyphipraniti.fitnesstasks.model.Task;
import com.kyphipraniti.fitnesstasks.utils.Constants;

import static android.app.Activity.RESULT_OK;

public class CalendarFragment extends Fragment implements DatePicker.OnDateChangedListener {

    private static Date currentDateView;
    private TasksAdapter mTasksAdapter;
    private List<Task> mTasks;
    private static List<Task> mCompletedTasks = new ArrayList<>();
    private List<Task> mAllTasks;
    private FirebaseUser currentUser;
    private DatePicker mDatePicker;
    private RecyclerView mRvTasks;
    private LinearLayoutManager mLinearLayoutManager;
    private final static DateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference dbReference = mDatabase.getReference();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();


    private FloatingActionButton mFabAdd;
    private FloatingActionButton mFabAddTask;
    private FloatingActionButton mFabAddPhoto;
    private LinearLayout mAddTaskLayout;
    private LinearLayout mAddPhotoLayout;
    private boolean fabExpanded = false;

    public final String APP_TAG = "FitTask";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    File photoFile;

    public CalendarFragment() {
    }

    public static List<Task> getCompletedTasks() {
        return mCompletedTasks;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
//                Bitmap takenImage = BitmapFactory.decodeFile(photoFileName);
//                new UploadPhoto().execute(takenImage);

                Toast.makeText(getContext(), "Picture was saved!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupDatePicker(View view) {
        DatePicker mDatePicker = view.findViewById(R.id.dpDeadline);
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

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
        mRvTasks.setItemAnimator(new DefaultItemAnimator());

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP |
            ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT){

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                moveItem(viewHolder.getAdapterPosition(),target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                completeTask(viewHolder.getAdapterPosition());
            }

        });
        itemTouchHelper.attachToRecyclerView(mRvTasks);
    }

    private void completeTask(int position) {
        mCompletedTasks.add(mTasks.get(position));
        mTasks.remove(position);
        mTasksAdapter.notifyItemRemoved(position);
    }

    private void moveItem(int oldPos, int newPos) {
        Task task = mTasks.get(oldPos);

        mTasks.remove(oldPos);
        mTasks.add(newPos, task);
        mTasksAdapter.notifyItemMoved(oldPos, newPos);
    }

    private void setupFloatingActionButton(View view) {
        mFabAdd = view.findViewById(R.id.fabAdd);
        mFabAddTask = view.findViewById(R.id.fabAddTask);
        mFabAddPhoto = view.findViewById(R.id.fabAddPhoto);

        mAddTaskLayout = view.findViewById(R.id.layoutAddTask);
        mAddPhotoLayout = view.findViewById(R.id.layoutAddPhoto);

        mFabAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(fabExpanded) {
                    closeSubMenusFab();
                } else {
                    openSubMenusFab();
                }
            }
        });

        mFabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchAddTaskFragment();
            }
        });

        mFabAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchTakePhotoFragment();
            }
        });

        closeSubMenusFab();
    }

    private void closeSubMenusFab(){
        mAddTaskLayout.setVisibility(View.INVISIBLE);
        mAddPhotoLayout.setVisibility(View.INVISIBLE);
        mFabAdd.setImageResource(R.drawable.ic_add_24dp);
        fabExpanded = false;
    }

    private void openSubMenusFab(){
        mAddTaskLayout.setVisibility(View.VISIBLE);
        mAddPhotoLayout.setVisibility(View.VISIBLE);
        //Change settings icon to 'X' icon
        mFabAdd.setImageResource(R.drawable.ic_close_24dp);
        fabExpanded = true;
    }

    private void launchAddTaskFragment() {
        FragmentManager fm = getActivity().getSupportFragmentManager();

        AddTaskFragment addTaskFragment = AddTaskFragment.newInstance();
        addTaskFragment.show(fm, "add");
        closeSubMenusFab();

    }

    private void launchTakePhotoFragment() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
        closeSubMenusFab();
    }

    public File getPhotoFileUri(String fileName) {
        // Only continue if the SD Card is mounted
        if (isExternalStorageAvailable()) {
            File mediaStorageDir = new File(
                    getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                Log.d(APP_TAG, "failed to create directory");
            }

            File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

            return file;
        }
        return null;
    }

    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
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

                        mAllTasks.add(task);
                        mTasks.clear();
                        mTasks.addAll(getDatesBetweenStartAndFinishWithFilter(getStartDate(), getEndDate()));
                        mTasksAdapter.notifyDataSetChanged();
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
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(start);
        calendar.setTimeInMillis(end);
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
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        currentDateView = c.getTime();

        mTasks.clear();

        mTasks.addAll(getDatesBetweenStartAndFinishWithFilter(getStartDate(), getEndDate()));
        mTasksAdapter.notifyDataSetChanged();

    }

    private class UploadPhoto extends AsyncTask<Bitmap, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Bitmap... bitmaps) {
            Bitmap progressPhoto = bitmaps[0];
            Long timestamp = System.currentTimeMillis();
            StorageReference storageRef = storage.getReference();
            StorageReference photoRef = storageRef.child(Constants.FIREBASE_CHILD_PHOTOS);
            StorageReference progressPhotoRef = photoRef.child(currentUser.getUid()).child(timestamp.toString()).child(photoFileName);
            ByteArrayOutputStream progressPhotoStream = new ByteArrayOutputStream();
            progressPhoto.compress(Bitmap.CompressFormat.JPEG, 90, progressPhotoStream);
            byte[] bytes = progressPhotoStream.toByteArray();
            progressPhotoRef.putBytes(bytes).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(getContext(), "Picture wasn't saved!", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String key = dbReference.child(Constants.FIREBASE_CHILD_USERS)
                            .child(currentUser.getUid())
                            .child("photos")
                            .push().getKey();
                    dbReference.child(Constants.FIREBASE_CHILD_USERS)
                            .child(currentUser.getUid())
                            .child(Constants.FIREBASE_CHILD_PHOTOS)
                            .child(key)
                            .setValue(taskSnapshot.getDownloadUrl());
                }
            });
            return bitmaps[0];
        }
    }
}
