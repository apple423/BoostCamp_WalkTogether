package com.example.han.boostcamp_walktogether.data;

import android.net.Uri;

import java.sql.Time;
import java.util.ArrayList;

/**
 * Created by Han on 2017-08-06.
 */
// 삭제해야 한다. 다른 사용처들을 보면서...

public class ParkFreeboardDTO {

    public String name;
    public String title;
    public String profileImageURL;
    public ArrayList<String> imageArrayList;
    public String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURLL) {
        this.profileImageURL = profileImageURLL;
    }

    public ArrayList<String> getImageArrayList() {
        return imageArrayList;
    }

    public void setImageArrayList(ArrayList<String> imageArrayList) {
        this.imageArrayList = imageArrayList;
    }
}
