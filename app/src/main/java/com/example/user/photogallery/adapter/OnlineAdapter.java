package com.example.user.photogallery.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.user.photogallery.R;
import com.example.user.photogallery.activity.MainActivity;
import com.example.user.photogallery.fragment.OnlineSlideShowDialogFragment;
import com.example.user.photogallery.model.OnlinePhoto;

import java.util.ArrayList;

public class OnlineAdapter extends RecyclerView.Adapter<OnlineAdapter.PhotoHolder> {

    public static final String IMAGES = "images";
    public static final String POSITION = "position";

    private ArrayList<OnlinePhoto> mItems;
    private Context mContext;

    public OnlineAdapter(Context context, ArrayList<OnlinePhoto> images) {
        mContext = context;
        this.mItems = images;
    }

    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_thumbnail, parent, false);

        final PhotoHolder photoHolder = new PhotoHolder(itemView);
        photoHolder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save a list of photos and a position of the selected photo to transfer
                Bundle bundle = new Bundle();
                bundle.putSerializable(IMAGES, mItems);
                bundle.putInt(POSITION, photoHolder.getAdapterPosition());

                FragmentTransaction fragmentTransaction = ((MainActivity) mContext).getSupportFragmentManager().beginTransaction();
                OnlineSlideShowDialogFragment fragment = OnlineSlideShowDialogFragment.newInstance();
                fragment.setArguments(bundle);
                fragment.show(fragmentTransaction, "slideShow");
            }
        });

        return photoHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
        OnlinePhoto image = mItems.get(position);

        Glide.with(mContext).load(image.getUrl())
                .thumbnail(0.5f)
                .apply(new RequestOptions().placeholder(R.drawable.placeholder))
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class PhotoHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;

        public PhotoHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.thumbnail);
        }
    }

}
