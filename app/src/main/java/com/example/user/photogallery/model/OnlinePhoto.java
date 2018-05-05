package com.example.user.photogallery.model;

import java.io.Serializable;

public class OnlinePhoto implements Serializable {
    private String mId;
    private String mUrl;

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
}
