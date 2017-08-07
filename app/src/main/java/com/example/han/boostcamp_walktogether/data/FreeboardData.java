package com.example.han.boostcamp_walktogether.data;

import java.util.ArrayList;

/**
 * Created by Han on 2017-08-07.
 */

public class FreeboardData {

    String userID;
    String userProfilePictureURL;
    String userNickName;
    String title;
    String content;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserProfilePictureURL() {
        return userProfilePictureURL;
    }

    public void setUserProfilePictureURL(String userProfilePictureURL) {
        this.userProfilePictureURL = userProfilePictureURL;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<String> getImageArrayList() {
        return imageArrayList;
    }

    public void setImageArrayList(ArrayList<String> imageArrayList) {
        this.imageArrayList = imageArrayList;
    }

    ArrayList<String> imageArrayList;





}
