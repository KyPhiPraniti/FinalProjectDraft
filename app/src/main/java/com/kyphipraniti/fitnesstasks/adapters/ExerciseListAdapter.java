package com.kyphipraniti.fitnesstasks.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kyphipraniti.fitnesstasks.R;

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.ViewHolder> {

    List<String> exercise;
    private Context mContext;
    private ExerciseListAdapterListener mListener;

    public interface ExerciseListAdapterListener {
        void onItemSelected(View view, int position);
    }

    public ExerciseListAdapter(Context context, ArrayList<String> aListExercise, ExerciseListAdapterListener listener) {
        this.exercise = aListExercise;
        mContext = context;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View v = inflater.inflate(R.layout.item_exercise, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.name.setText(exercise.get(position));
    }

    @Override
    public int getItemCount() {
        return exercise.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tvExerciseName);
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        mListener.onItemSelected(view, position);

                    }
                }
            });
        }
    }
}