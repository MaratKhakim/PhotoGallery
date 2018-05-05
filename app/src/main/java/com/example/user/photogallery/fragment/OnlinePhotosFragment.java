package com.example.user.photogallery.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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

public class OnlinePhotosFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    //replace api_key with your key
    private static final String API_KEY = "api_key";
    private static final String TAG = "OnlinePhotosFragment";

    private View mView;
    private OnlineAdapter mAdapter;
    private ProgressDialog pDialog;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mEmptyStateTextView;
    private boolean isInitialLoad = false;
    private ConnectivityManager mConnectivityManager;
    private NetworkInfo mNetworkInfo;

    public static OnlinePhotosFragment newInstance() {
        return new OnlinePhotosFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_online_photos, container, false);
        mEmptyStateTextView = (TextView) mView.findViewById(R.id.empty_view);

        mSwipeRefreshLayout = mView.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mConnectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();

        if (mNetworkInfo != null && mNetworkInfo.isConnected()) {
            setupUI();
        } else {
            mEmptyStateTextView.setVisibility(View.VISIBLE);
        }

        return mView;
    }

    private void setupUI() {
        pDialog = new ProgressDialog(getActivity());
        RecyclerView photoRecyclerView = mView.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        photoRecyclerView.setLayoutManager(mLayoutManager);
        photoRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ArrayList<OnlinePhoto> photos = fetchImages();
        mAdapter = new OnlineAdapter(getActivity(), photos);
        photoRecyclerView.setAdapter(mAdapter);
    }

    private ArrayList<OnlinePhoto> fetchImages() {

        final ArrayList<OnlinePhoto> photos = new ArrayList<>();

        pDialog.setMessage(getResources().getString(R.string.download_message));

        if (!isInitialLoad) {
            pDialog.show();
            isInitialLoad = true;
        }

        String url = buildUrlGetRecent();

        JsonObjectRequest req = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                if (pDialog.isShowing())
                    pDialog.hide();

                if (mSwipeRefreshLayout.isRefreshing())
                    mSwipeRefreshLayout.setRefreshing(false);

                try {
                    JSONObject photosJsonObject = response.getJSONObject("photos");
                    JSONArray photoJsonArray = photosJsonObject.getJSONArray("photo");

                    for (int i = 0; i < photoJsonArray.length(); i++) {
                        JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);

                        OnlinePhoto item = new OnlinePhoto();
                        item.setId(photoJsonObject.getString("id"));

                        if (!photoJsonObject.has("url_s")) {
                            continue;
                        }

                        item.setUrl(photoJsonObject.getString("url_s"));
                        photos.add(item);
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

        return photos;
    }

    private String buildUrlGetRecent() {
        return Uri.parse("https://api.flickr.com/services/rest").buildUpon()
                .appendQueryParameter("method", "flickr.photos.getRecent")
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1")
                .appendQueryParameter("extras", "url_s")
                .build().toString();
    }

    @Override
    public void onRefresh() {
        mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();

        if (mNetworkInfo != null && mNetworkInfo.isConnected()) {
            if (mEmptyStateTextView.isShown())
                mEmptyStateTextView.setVisibility(View.GONE);

            setupUI();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);

            }
        }, 4000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (pDialog != null)
            pDialog.dismiss();
    }
}
