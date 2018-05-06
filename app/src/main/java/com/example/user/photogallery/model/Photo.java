package com.example.user.photogallery.model;

import java.io.Serializable;

public class Photo implements Serializable{

    private String mId;
    private String mLink;

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String mUrl) {
        this.mLink = mUrl;
    }
}
