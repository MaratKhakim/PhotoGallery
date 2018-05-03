package com.example.user.photogallery.fragment;


import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.photogallery.R;
import com.example.user.photogallery.adapter.SavedAdapter;
import com.example.user.photogallery.model.SavedPhoto;

import java.io.File;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SavedPhotosFragment extends Fragment {

    private static final String TAG = "SavedPhotosFragment";

    private RecyclerView mPhotoRecyclerView;
    public SavedAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_saved_photos, container, false);
        mPhotoRecyclerView = view.findViewById(R.id.recycler_view_saved_photos);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mPhotoRecyclerView.setLayoutManager(mLayoutManager);
        mPhotoRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ArrayList<SavedPhoto> items = getSavedPhotos();
        mAdapter = new SavedAdapter(getActivity(), items);
        setupAdapter();
        Log.d(TAG, "onCreateView");
        return view;
    }

    private void setupAdapter() {
        if (isAdded()) {
            mPhotoRecyclerView.setAdapter(mAdapter);
        }
    }

    private ArrayList<SavedPhoto> getSavedPhotos(){
        ArrayList<SavedPhoto> savedPhotos = new ArrayList<>();

        File folder= new File(Environment.getExternalStorageDirectory(), getResources().getString(R.string.folder_name));
        SavedPhoto photo;

        if (folder.exists()){
            File[] files=folder.listFiles();

            for (int i=0;i<files.length;i++)
            {
                File file=files[i];

                photo=new SavedPhoto();
                photo.setName(file.getName());
                photo.setUri(Uri.fromFile(file));

                savedPhotos.add(photo);
            }
        }

        return savedPhotos;
    }

}
