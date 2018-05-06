package com.example.user.photogallery.adapter;

import android.content.Context;

import com.example.user.photogallery.fragment.SavedSlideShowDialogFragment;
import com.example.user.photogallery.fragment.SlideShowDialogFragment;
import com.example.user.photogallery.model.Photo;

import java.util.ArrayList;

public class SavedAdapter extends PhotoAdapter {

    public SavedAdapter(Context context, ArrayList<Photo> savedPhotos) {
        mContext = context;
        mPhotos = savedPhotos;
    }

    @Override
    protected SlideShowDialogFragment createSlideShowDialogFragment() {
        return SavedSlideShowDialogFragment.newInstance();
    }
}
