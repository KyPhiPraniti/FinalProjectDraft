package com.kyphipraniti.fitnesstasks.fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.kyphipraniti.fitnesstasks.R;
import com.kyphipraniti.fitnesstasks.adapters.ProgressPhotoAdapter;
import com.kyphipraniti.fitnesstasks.adapters.TasksAdapter;
import com.kyphipraniti.fitnesstasks.model.Task;

public class SummaryFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference dbReference = mDatabase.getReference();

    List<Task> mTasks;
    RecyclerView rvTasks;

    ArrayList<Uri> mProgressPhotos;
    RecyclerView rvProgressPhotos;
    ProgressPhotoAdapter progressPhotoAdapter;

    public SummaryFragment() {
    }

    public static SummaryFragment newInstance() {
        SummaryFragment fragment = new SummaryFragment();
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
        View v = inflater.inflate(R.layout.fragment_summary, container, false);

        rvTasks = v.findViewById(R.id.rvTasks);
        mTasks = CalendarFragment.getCompletedTasks();
        //mTasks = Task.createCompletedTasksList(20);
        Collections.sort(mTasks);
        TasksAdapter adapter = new TasksAdapter(mTasks);
        rvTasks.setAdapter(adapter);

        rvProgressPhotos = v.findViewById(R.id.rvProgressPhotos);
        mProgressPhotos = new ArrayList<>();
        ArrayList<String> photoStrings = new ArrayList<String>(Arrays.asList("https://firebasestorage.googleapis.com/v0/b/fitnesstasks.appspot.com/o/stock-photo-portrait-of-young-fitness-woman-flexing-muscles-and-smiling-african-female-model-in-sportswear-564988303.jpg?alt=media&token=7393d5e9-98af-436d-b057-13256596c2b7",
                "https://firebasestorage.googleapis.com/v0/b/fitnesstasks.appspot.com/o/strong-man-flex-arm-close-very-flexing-his-muscles-32938566.jpg?alt=media&token=c9d24b23-dc0f-4166-8c4c-14815c873a7c",
                "https://firebasestorage.googleapis.com/v0/b/fitnesstasks.appspot.com/o/images%2Fphoto.jpg?alt=media&token=2a586991-7184-4d33-aebb-80c17c7e5eb1"));
        for(int i = 0; i < photoStrings.size(); i++) {
            Uri uri = Uri.parse(photoStrings.get(i));
            mProgressPhotos.add(uri);
        }
        progressPhotoAdapter = new ProgressPhotoAdapter(mProgressPhotos);
        rvProgressPhotos.setAdapter(progressPhotoAdapter);

        LinearLayoutManager taskLinearLayoutManager = new LinearLayoutManager(getContext());
        rvTasks.setLayoutManager(taskLinearLayoutManager);
        LinearLayoutManager photoLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvProgressPhotos.setLayoutManager(photoLinearLayoutManager);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        final Query progressPhotos = dbReference.child(Constants.FIREBASE_CHILD_USERS).child(Constants.FIREBASE_CHILD_PHOTOS);
//        progressPhotos.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                mProgressPhotos.add(dataSnapshot.getValue(Uri.class));
//                progressPhotoAdapter.notifyItemInserted(mProgressPhotos.size());
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

    public interface OnFragmentInteractionListener {

    }
}
