package com.example.user.photogallery.model;

import android.net.Uri;

public class SavedPhoto {
    private String mName;
    private Uri mUri;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Uri getUri() {
        return mUri;
    }

    public void setUri(Uri uri) {
        mUri = uri;
    }
}
