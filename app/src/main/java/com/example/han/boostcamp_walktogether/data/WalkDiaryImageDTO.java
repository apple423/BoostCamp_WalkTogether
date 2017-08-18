package com.example.han.boostcamp_walktogether.data;

/**
 * Created by Han on 2017-08-16.
 */

public class WalkDiaryImageDTO {

    int diary_key;
    String user_email;
    String image_url;
    int statusCode;


    public int getDiary_key() {
        return diary_key;
    }

    public void setDiary_key(int diary_key) {
        this.diary_key = diary_key;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }


    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
