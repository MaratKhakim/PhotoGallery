package com.example.user.photogallery.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.photogallery.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SavedPhotosFragment extends Fragment {


    public SavedPhotosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_photos, container, false);
    }

}
