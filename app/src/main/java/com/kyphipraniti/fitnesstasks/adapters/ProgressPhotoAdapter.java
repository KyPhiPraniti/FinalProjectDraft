package com.kyphipraniti.fitnesstasks.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;
import com.kyphipraniti.fitnesstasks.R;

import java.util.List;

/**
 * Created by kystatham on 10/22/17.
 */

public class ProgressPhotoAdapter extends RecyclerView.Adapter<ProgressPhotoAdapter.ViewHolder> {

    final private List<StorageReference> mProgressPhotos;
    private Context mContext;

    public ProgressPhotoAdapter(List<StorageReference> photos) {
        mProgressPhotos = photos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        View photoView = layoutInflater.inflate(R.layout.item_progress_photo, parent, false);
        return new ViewHolder(photoView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StorageReference photo = mProgressPhotos.get(position);
        Glide.with(mContext)
                .using(new FirebaseImageLoader())
                .load(photo)
                .into(holder.ivPhoto);
    }

    @Override
    public int getItemCount() {
        return mProgressPhotos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivPhoto;

        ViewHolder(View itemView) {
            super(itemView);

            ivPhoto = itemView.findViewById(R.id.ivPhoto);
        }
    }
}
