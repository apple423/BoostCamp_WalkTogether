package com.example.han.boostcamp_walktogether.data;

/**
 * Created by Han on 2017-08-09.
 */
// 게시판 이미지 DTO
public class FreeboardImageDTO {

    int freeboard_key;
    int park_key;
    String image;

    public int getFreeboard_key() {
        return freeboard_key;
    }

    public void setFreeboard_key(int freeboard_key) {
        this.freeboard_key = freeboard_key;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPark_key() {
        return park_key;
    }
    public void setPark_key(int park_key) {
        this.park_key = park_key;
    }
}
