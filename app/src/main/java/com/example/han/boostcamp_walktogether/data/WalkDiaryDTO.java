package com.example.han.boostcamp_walktogether.data;

import java.util.Date;

/**
 * Created by Han on 2017-08-16.
 */

public class WalkDiaryDTO {

    int diary_key;
    String user_email;
    String content;
    Date date;
    long walk_time;
    float walk_distance;
    int insertId;
    int statusCode;

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDiary_key() {
        return diary_key;
    }

    public void setDiary_key(int diary_key) {
        this.diary_key = diary_key;
    }

    public int getInsertId() {
        return insertId;
    }

    public void setInsertId(int insertId) {
        this.insertId = insertId;
    }


    public long getWalk_time() {
        return walk_time;
    }

    public void setWalk_time(long walk_time) {
        this.walk_time = walk_time;
    }

    public float getWalk_distance() {
        return walk_distance;
    }

    public void setWalk_distance(float walk_distance) {
        this.walk_distance = walk_distance;
    }


    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
