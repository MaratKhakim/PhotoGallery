package com.example.user.photogallery.adapter;

import android.content.Context;

import com.example.user.photogallery.fragment.OnlineSlideShowDialogFragment;
import com.example.user.photogallery.fragment.SlideShowDialogFragment;
import com.example.user.photogallery.model.Photo;

import java.util.ArrayList;

public class OnlineAdapter extends PhotoAdapter {

    public OnlineAdapter(Context context, ArrayList<Photo> images) {
        mContext = context;
        mPhotos = images;
    }

    @Override
    protected SlideShowDialogFragment createSlideShowDialogFragment() {
        return OnlineSlideShowDialogFragment.newInstance();
    }
}
