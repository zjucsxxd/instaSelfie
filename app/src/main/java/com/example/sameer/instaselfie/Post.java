package com.example.sameer.instaselfie;

/**
 * Created by sameer on 11/27/14.
 */

public class Post {
    private String userName;
    private String thumbUrl;
    private String lowResolutionImageUrl;
    private String highResolutionImageUrl;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getLowResolutionImageUrl() {
        return lowResolutionImageUrl;
    }

    public void setLowResolutionImageUrl(String lowResolutionImageUrl) {
        this.lowResolutionImageUrl = lowResolutionImageUrl;
    }

    public String getHighResolutionImageUrl() {
        return highResolutionImageUrl;
    }

    public void setHighResolutionImageUrl(String highResolutionImageUrl) {
        this.highResolutionImageUrl = highResolutionImageUrl;
    }

    public Post(String userName, String thumbUrl, String lowResolutionImageUrl, String highResolutionImageUrl) {
        this.userName = userName;
        this.thumbUrl = thumbUrl;
        this.lowResolutionImageUrl = lowResolutionImageUrl;
        this.highResolutionImageUrl = highResolutionImageUrl;
    }
}
