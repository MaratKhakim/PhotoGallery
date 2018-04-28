package com.example.user.photogallery.model;


import java.io.Serializable;

public class GalleryItem implements Serializable {
    private String mTitle;
    private String mId;
    private String mUrl;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    @Override
    public String toString() {
        return mTitle;
    }
}
