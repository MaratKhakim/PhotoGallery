package com.example.user.photogallery.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.user.photogallery.R;
import com.example.user.photogallery.activity.MainActivity;
import com.example.user.photogallery.fragment.OnlineSlideShowDialogFragment;
import com.example.user.photogallery.model.OnlinePhoto;

import java.util.ArrayList;

public class OnlineAdapter extends RecyclerView.Adapter<OnlineAdapter.PhotoHolder> {

    private ArrayList<OnlinePhoto> mItems;
    private Context mContext;

    public class PhotoHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;

        public PhotoHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.thumbnail);
        }
    }


    public OnlineAdapter(Context context, ArrayList<OnlinePhoto> images) {
        mContext = context;
        this.mItems = images;
    }

    @Override
    public PhotoHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_thumbnail, parent, false);

        final PhotoHolder photoHolder = new PhotoHolder(itemView);
        photoHolder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", mItems);
                bundle.putInt("position", photoHolder.getAdapterPosition());

                FragmentTransaction fragmentTransaction = ((MainActivity) mContext).getSupportFragmentManager().beginTransaction();
                OnlineSlideShowDialogFragment fragment = OnlineSlideShowDialogFragment.newInstance();
                fragment.setArguments(bundle);
                fragment.show(fragmentTransaction, "slideShow");
            }
        });

        return photoHolder;
    }

    @Override
    public void onBindViewHolder(PhotoHolder holder, int position) {
        OnlinePhoto image = mItems.get(position);

        /*RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);*/

        Glide.with(mContext).load(image.getUrl())
                .thumbnail(0.5f)
                //.apply(requestOptions)
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

}
