package com.kyphipraniti.fitnesstasks.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kyphipraniti.fitnesstasks.R;
import com.kyphipraniti.fitnesstasks.adapters.ProgressPhotoAdapter;
import com.kyphipraniti.fitnesstasks.adapters.TasksAdapter;
import com.kyphipraniti.fitnesstasks.model.Task;

import java.util.ArrayList;
import java.util.Collections;

public class SummaryFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    ArrayList<Task> mTasks;
    RecyclerView rvTasks;

    ArrayList<StorageReference> mProgressPhotos;
    RecyclerView rvProgressPhotos;

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
        mTasks = Task.createCompletedTasksList(20);
        Collections.sort(mTasks);
        TasksAdapter adapter = new TasksAdapter(mTasks);
        rvTasks.setAdapter(adapter);

        rvProgressPhotos = v.findViewById(R.id.rvProgressPhotos);
        //get storage refs from database
        mProgressPhotos = new ArrayList<>();
        ProgressPhotoAdapter photoAdapter = new ProgressPhotoAdapter(mProgressPhotos);
        rvProgressPhotos.setAdapter(photoAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvTasks.setLayoutManager(linearLayoutManager);
        rvProgressPhotos.setLayoutManager(linearLayoutManager);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

    }
}
