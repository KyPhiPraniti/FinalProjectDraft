package com.kyphipraniti.fitnesstasks.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kyphipraniti.fitnesstasks.R;
import com.kyphipraniti.fitnesstasks.model.Task;
import com.kyphipraniti.fitnesstasks.utils.Constants;
import com.kyphipraniti.fitnesstasks.viewholders.ItemTouchHelperViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference dbReference = mDatabase.getReference();
    private final FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

    final private List<Task> mTasks;
    private Context mContext;

    public TasksAdapter(List<Task> tasks) {
        mTasks = tasks;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        View taskView = layoutInflater.inflate(R.layout.item_task, parent, false);
        return new ViewHolder(taskView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Task task = mTasks.get(position);

        holder.tvAmount.setText(String.valueOf(task.getAmount()));
        holder.tvAction.setText(task.getAction());
        holder.tvDeadline.setText(task.getFormattedDeadline(task.getDeadline()));
        holder.tvUnits.setText(task.getUnits());

    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    @Override
    public void onItemDismiss(int position) {
        Task changingTask = mTasks.get(position);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(Constants.FIREBASE_CHILD_COMPLETED, true);
        if (mCurrentUser != null) {
            dbReference.child(Constants.FIREBASE_CHILD_TASKS)
                    .child(mCurrentUser.getUid())
                    .child(changingTask.getKey())
                    .updateChildren(childUpdates);
        }
        mTasks.remove(position);
        notifyItemRemoved(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        final TextView tvAmount;
        final TextView tvUnits;
        final TextView tvDeadline;
        final TextView tvAction;

        ViewHolder(View itemView) {
            super(itemView);

            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvUnits = itemView.findViewById(R.id.tvUnits);
            tvDeadline = itemView.findViewById(R.id.tvDeadline);
            tvAction = itemView.findViewById(R.id.tvAction);

        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}
