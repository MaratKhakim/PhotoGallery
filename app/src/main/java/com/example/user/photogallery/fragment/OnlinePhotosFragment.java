package com.example.user.photogallery.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.user.photogallery.R;
import com.example.user.photogallery.adapter.OnlineAdapter;
import com.example.user.photogallery.helper.SingletonRequestQueue;
import com.example.user.photogallery.model.OnlinePhoto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OnlinePhotosFragment extends Fragment {

    //replace api_key with your key
    private static final String API_KEY = "api_key";
    private static final String TAG = "OnlinePhotosFragment";

    private OnlineAdapter mAdapter;
    private ProgressDialog pDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        TextView emptyStateTextView = (TextView) view.findViewById(R.id.empty_view);

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()){
            pDialog = new ProgressDialog(getActivity());
            RecyclerView photoRecyclerView = view.findViewById(R.id.recycler_view);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
            photoRecyclerView.setLayoutManager(mLayoutManager);
            photoRecyclerView.setItemAnimator(new DefaultItemAnimator());

            ArrayList<OnlinePhoto> items = fetchImages();
            mAdapter = new OnlineAdapter(getActivity(), items);
            photoRecyclerView.setAdapter(mAdapter);
        } else {
            emptyStateTextView.setText(R.string.no_internet_connection);
            emptyStateTextView.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private ArrayList<OnlinePhoto> fetchImages() {

        final ArrayList<OnlinePhoto> mItems = new ArrayList<>();

        pDialog.setMessage(getResources().getString(R.string.download_message));
        pDialog.show();

        String url = buildUrlGetRecent();

        JsonObjectRequest req = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                pDialog.hide();

                try {
                    JSONObject photosJsonObject = response.getJSONObject("photos");
                    JSONArray photoJsonArray = photosJsonObject.getJSONArray("photo");

                    for (int i = 0; i < photoJsonArray.length(); i++){
                        JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);

                        OnlinePhoto item = new OnlinePhoto();
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

        return mItems;
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
