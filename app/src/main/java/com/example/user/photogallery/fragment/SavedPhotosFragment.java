package com.example.user.photogallery.fragment;


import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.photogallery.R;
import com.example.user.photogallery.adapter.SavedAdapter;
import com.example.user.photogallery.model.Photo;

import java.io.File;
import java.util.ArrayList;

public class SavedPhotosFragment extends Fragment {

    private RecyclerView mPhotoRecyclerView;
    private TextView mTextView;

    public static SavedPhotosFragment newInstance(){
        return new SavedPhotosFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_saved_photos, container, false);
        mPhotoRecyclerView = view.findViewById(R.id.recycler_view_saved_photos);
        mTextView = view.findViewById(R.id.no_saved_photos_view);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mPhotoRecyclerView.setLayoutManager(mLayoutManager);
        mPhotoRecyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        ArrayList<Photo> savedPhotos = getSavedPhotos();

        // if there is no saved photos then show a message
        if (savedPhotos.size() < 1){
            mTextView.setVisibility(View.VISIBLE);
        } else {
            //otherwise display all saved photos
            if (mTextView.isShown())
                mTextView.setVisibility(View.GONE);

            SavedAdapter adapter = new SavedAdapter(getActivity(), savedPhotos);
            mPhotoRecyclerView.setAdapter(adapter);
        }
    }

    // get all saved photos from the PhotoGallery folder
    private ArrayList<Photo> getSavedPhotos(){
        ArrayList<Photo> savedPhotos = new ArrayList<>();

        File folder= new File(Environment.getExternalStorageDirectory(), getResources().getString(R.string.folder_name));

        if (folder.exists()){
            File[] files=folder.listFiles();

            for (File file : files) {
                Photo photo = new Photo();
                photo.setId(file.getName());
                photo.setLink(file.getAbsolutePath());

                savedPhotos.add(photo);
            }
        }

        return savedPhotos;
    }

}
