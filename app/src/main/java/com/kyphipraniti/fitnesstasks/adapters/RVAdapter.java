package com.kyphipraniti.fitnesstasks.adapters;

import java.util.List;

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
import com.kyphipraniti.fitnesstasks.model.Workout;
import com.kyphipraniti.fitnesstasks.utils.AnimationUtil;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

    private List<Workout> mWorkouts;
    Context context;
    int mPreviousPosition = 0;

    public RVAdapter(Context context, List<Workout> workouts) {
        this.context = context;
        this.mWorkouts = workouts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.name.setText(mWorkouts.get(position).getWorkoutName());
        holder.image.setImageResource(mWorkouts.get(position).getThumbnailDrawable());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Workout workout = mWorkouts.get(position);
                Intent intent = new Intent(context, WorkoutDetailActivity.class);
                intent.putExtra("workout", workout);
                context.startActivity(intent);
            }
        });

        if (position >= mPreviousPosition) {
            AnimationUtil.animate(holder, true);

        } else {
            AnimationUtil.animate(holder, false);

        }
        mPreviousPosition = position;
    }

    @Override
    public int getItemCount() {
        return mWorkouts.size();
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
