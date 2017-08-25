package com.example.han.boostcamp_walktogether.data;

import com.example.han.boostcamp_walktogether.helper.FirebaseHelper;
import com.google.firebase.auth.FirebaseUser;

import org.parceler.Parcel;

import java.util.Date;

/**
 * Created by Han on 2017-08-11.
 */
@Parcel
public class CommentDTO {
    int comment_key;
    int park_key;
    String user_name;
    String user_id;
    String user_profile;
    float star_point;
    float pet_point;
    String comment;
    Date date;
    int statusCode;

    
    public int getComment_key() {
        return comment_key;
    }

    public void setComment_key(int comment_key) {
        this.comment_key = comment_key;
    }

    public int getPark_key() {
        return park_key;
    }

    public void setPark_key(int park_key) {
        this.park_key = park_key;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public float getStar_point() {
        return star_point;
    }

    public void setStar_point(float star_point) {
        this.star_point = star_point;
    }

    public float getPet_point() {
        return pet_point;
    }

    public void setPet_point(float pet_point) {
        this.pet_point = pet_point;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_profile() {
        return user_profile;
    }

    public void setUser_profile(String user_profile) {
        this.user_profile = user_profile;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
