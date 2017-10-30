package com.kyphipraniti.fitnesstasks.adapters;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kyphipraniti.fitnesstasks.R;
import com.kyphipraniti.fitnesstasks.model.Task;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {

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
    public void onBindViewHolder(ViewHolder holder, int position) {
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

    class ViewHolder extends RecyclerView.ViewHolder {
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
    }
}
