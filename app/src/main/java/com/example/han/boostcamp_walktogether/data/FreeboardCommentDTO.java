package com.example.han.boostcamp_walktogether.data;

import org.parceler.Parcel;

import java.util.Date;

/**
 * Created by Han on 2017-08-20.
 */

@Parcel
public class FreeboardCommentDTO {
    int freeboard_key;
    String user_email;
    String user_profile;
    String user_name;
    String comment;
    int comment_count;
    Date date;
    int statusCode;

    public int getFreeboard_key() {
        return freeboard_key;
    }

    public void setFreeboard_key(int freeboard_key) {
        this.freeboard_key = freeboard_key;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_profile() {
        return user_profile;
    }

    public void setUser_profile(String user_profile) {
        this.user_profile = user_profile;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
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
