package com.example.user.photogallery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.user.photogallery.R;
import com.example.user.photogallery.model.GalleryItem;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.PhotoHolder> {

    private List<GalleryItem> mItems;
    private Context mContext;

    public class PhotoHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;

        public PhotoHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.thumbnail);
        }
    }


    public GalleryAdapter(Context context, List<GalleryItem> images) {
        mContext = context;
        this.mItems = images;
    }

    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_thumbnail, parent, false);

        return new PhotoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PhotoHolder holder, int position) {
        GalleryItem image = mItems.get(position);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(mContext).load(image.getUrl())
                .thumbnail(0.5f)
                .apply(requestOptions)
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

}
