package com.example.han.boostcamp_walktogether.data;

import org.parceler.Parcel;

/**
 * Created by Han on 2017-08-03.
 */
// 파이어베이스에서 공원 정보를 얻어오기 위한 DTO 하지만 지금은 안쓰기 때문에
    // 코드 리펙토링을 진행하면서 삭제해야한다.
@Parcel
public class ParkDataFromFirebaseDTO {

    String address;
    String imageURL;
    float latitude;
    float longitude;
    String name;
    String uid;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
