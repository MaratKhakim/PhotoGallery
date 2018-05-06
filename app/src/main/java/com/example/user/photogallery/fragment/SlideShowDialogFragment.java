package com.example.user.photogallery.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.photogallery.R;
import com.example.user.photogallery.adapter.OnlineAdapter;
import com.example.user.photogallery.adapter.PhotoAdapter;
import com.example.user.photogallery.model.Photo;

import java.util.ArrayList;

// super class for the slide show fragments
public abstract class SlideShowDialogFragment extends DialogFragment {

    protected ArrayList<Photo> mImages;

    // abstract method for subclasses to return their own PagerAdapters
    protected abstract PagerAdapter createPagerAdapter();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slide_show, container, false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        Bundle bundle = getArguments();
        mImages = (ArrayList<Photo>) bundle.getSerializable(PhotoAdapter.IMAGES);

        int selectedPosition = bundle.getInt(PhotoAdapter.POSITION, 0);

        PagerAdapter myViewPagerAdapter = createPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);

        viewPager.setCurrentItem(selectedPosition, false);

        return view;
    }
}
