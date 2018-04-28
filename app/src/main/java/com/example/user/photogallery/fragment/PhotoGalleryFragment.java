package com.example.user.photogallery.fragment;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.user.photogallery.R;
import com.example.user.photogallery.adapter.GalleryAdapter;
import com.example.user.photogallery.helper.SingletonRequestQueue;
import com.example.user.photogallery.model.GalleryItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PhotoGalleryFragment extends Fragment {

    //replace api_key with your key
    private static final String API_KEY = "api_key";
    private static final String TAG = "PhotoGalleryFragment";

    private RecyclerView mPhotoRecyclerView;
    private GalleryAdapter mAdapter;
    private ArrayList<GalleryItem> mItems = new ArrayList<>();
    private ProgressDialog pDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pDialog = new ProgressDialog(getActivity());
        mAdapter = new GalleryAdapter(getActivity(), mItems);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        mPhotoRecyclerView = view.findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mPhotoRecyclerView.setLayoutManager(mLayoutManager);
        mPhotoRecyclerView.setItemAnimator(new DefaultItemAnimator());
        setupAdapter();
        fetchImages();

        return view;
    }

    private void setupAdapter() {
        if (isAdded()) {
            mPhotoRecyclerView.setAdapter(mAdapter);
        }
    }

    private void fetchImages() {

        pDialog.setMessage("Downloading json...");
        pDialog.show();

        String url = buildUrlGetRecent();

        JsonObjectRequest req = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                pDialog.hide();

                mItems.clear();

                try {
                    JSONObject photosJsonObject = response.getJSONObject("photos");
                    JSONArray photoJsonArray = photosJsonObject.getJSONArray("photo");

                    for (int i = 0; i < photoJsonArray.length(); i++){
                        JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);

                        GalleryItem item = new GalleryItem();
                        item.setId(photoJsonObject.getString("id"));
                        item.setTitle(photoJsonObject.getString("title"));

                        if (!photoJsonObject.has("url_s")){
                            continue;
                        }

                        item.setUrl(photoJsonObject.getString("url_s"));
                        mItems.add(item);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }

                mAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                pDialog.hide();
            }
        });

        // Adding request to request queue
        SingletonRequestQueue.getInstance().addToRequestQueue(req);
    }

    private String buildUrlGetRecent(){
        return Uri.parse("https://api.flickr.com/services/rest").buildUpon()
                .appendQueryParameter("method", "flickr.photos.getRecent")
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1")
                .appendQueryParameter("extras", "url_s")
                .build().toString();
    }
}
