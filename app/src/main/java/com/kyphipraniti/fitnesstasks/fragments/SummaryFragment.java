package com.kyphipraniti.fitnesstasks.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.kyphipraniti.fitnesstasks.R;
import com.kyphipraniti.fitnesstasks.adapters.ProgressPhotoAdapter;
import com.kyphipraniti.fitnesstasks.adapters.TasksAdapter;
import com.kyphipraniti.fitnesstasks.model.Task;
import com.kyphipraniti.fitnesstasks.utils.Constants;

public class SummaryFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private FirebaseUser currentUser;
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference dbReference = mDatabase.getReference();

    List<Task> mTasks;
    RecyclerView rvTasks;
    TasksAdapter mTaskAdapter;

    ArrayList<String> mProgressPhotos;
    RecyclerView rvProgressPhotos;
    ProgressPhotoAdapter progressPhotoAdapter;
    TextView tvProgressPhotos;

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

        mTasks = new ArrayList<>();
        mTaskAdapter = new TasksAdapter(mTasks);
        rvTasks.setAdapter(mTaskAdapter);

        tvProgressPhotos = v.findViewById(R.id.tvProgressPhotos);
        rvProgressPhotos = v.findViewById(R.id.rvProgressPhotos);
        mProgressPhotos = new ArrayList<>();
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

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        readCompletedTasks();
        final Query progressPhotos = dbReference.child(Constants.FIREBASE_CHILD_USERS).child(currentUser.getUid()).child(Constants.FIREBASE_CHILD_PHOTOS);
        progressPhotos.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (tvProgressPhotos.getVisibility() != View.VISIBLE) {
                    tvProgressPhotos.setVisibility(View.VISIBLE);
                }
                mProgressPhotos.add(dataSnapshot.getValue(String.class));
                progressPhotoAdapter.notifyItemInserted(mProgressPhotos.size());
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

    private void readCompletedTasks() {
            dbReference.child(Constants.FIREBASE_CHILD_TASKS)
                .child(currentUser.getUid())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Task task = dataSnapshot.getValue(Task.class);

                        if (task.isCompleted()) {
                            mTasks.add(task);
                            mTaskAdapter.notifyDataSetChanged();
                        }
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

    }
}
