package com.example.han.boostcamp_walktogether.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Han on 2017-08-01.
 */
// 파이어베이스 회원가입을 위한 UserData
public class UserData {

    private String email;
    private String password;
    private String nickName;
    private String imageURL;
    private String animalType;
    private String kind;
    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public UserData(String email, String nickName, String imageURL, String animalType, String kind, String uid) {
        this.email = email;
        this.nickName = nickName;
        this.imageURL = imageURL;
        this.animalType = animalType;
        this.kind = kind;
        this.uid = uid;

    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getAnimalType() {
        return animalType;
    }

    public void setAnimalType(String animalType) {
        this.animalType = animalType;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", getEmail());
        result.put("nickName", getNickName());
        result.put("imageURL", getImageURL());
        result.put("animalType", getAnimalType());
        result.put("kind", getKind());
        result.put("uid",getUid());

        return result;
    }
}
