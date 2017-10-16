package com.kyphipraniti.fitnesstasks.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kyphipraniti.fitnesstasks.R;
import com.kyphipraniti.fitnesstasks.activities.WorkoutDetailActivity;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

    ArrayList<String> workoutNames;
    ArrayList<Integer> workoutImages;
    Context context;

    public RVAdapter(Context context, ArrayList<String> workoutNames, ArrayList<Integer> workoutImages) {
        this.context = context;
        this.workoutNames = workoutNames;
        this.workoutImages = workoutImages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.name.setText(workoutNames.get(position));
        holder.image.setImageResource(workoutImages.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WorkoutDetailActivity.class);
                intent.putExtra("image", workoutImages.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return workoutNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);

        }
    }

}
