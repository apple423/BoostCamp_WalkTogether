package com.example.han.boostcamp_walktogether.data;

import java.util.ArrayList;

/**
 * Created by Han on 2017-08-07.
 */
// 게시판 DTO
public class FreeboardDTO {

    int park_key;
    int freeboard_key;
    String user_id;
    String user_profie;
    String user_name;
    String title;
    String content;
    String date;
    int insertId;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_profie() {
        return user_profie;
    }

    public void setUser_profie(String user_profie) {
        this.user_profie = user_profie;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPark_key() {
        return park_key;
    }

    public void setPark_key(int park_key) {
        this.park_key = park_key;
    }

    public int getInsertId() {
        return insertId;
    }

    public void setInsertId(int insertId) {
        this.insertId = insertId;
    }

    public int getFreeboard_key() {
        return freeboard_key;
    }

    public void setFreeboard_key(int freeboard_key) {
        this.freeboard_key = freeboard_key;
    }
}
