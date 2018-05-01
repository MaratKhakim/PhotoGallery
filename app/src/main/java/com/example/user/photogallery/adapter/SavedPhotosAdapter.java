package com.example.user.photogallery.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.user.photogallery.R;
import com.example.user.photogallery.model.SavedPhoto;

import java.util.ArrayList;

public class SavedPhotosAdapter extends RecyclerView.Adapter<SavedPhotosAdapter.SavedPhotoHolder> {

    private Context mContext;
    private ArrayList<SavedPhoto> mSavedPhotos;

    public SavedPhotosAdapter(Context context, ArrayList<SavedPhoto> savedPhotos) {
        mContext = context;
        mSavedPhotos = savedPhotos;
    }

    @NonNull
    @Override
    public SavedPhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_thumbnail, parent, false);
        return new SavedPhotoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedPhotoHolder holder, int position) {
        SavedPhoto savedPhoto = mSavedPhotos.get(position);
        Glide.with(mContext).load(savedPhoto.getUri())
                .thumbnail(0.5f)
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mSavedPhotos.size();
    }


    public class SavedPhotoHolder extends RecyclerView.ViewHolder{

        ImageView mImageView;

        public SavedPhotoHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.thumbnail);
        }
    }
}
