package com.example.han.boostcamp_walktogether.data;

import org.parceler.Parcel;

/**
 * Created by Han on 2017-08-03.
 */
// 서버로부터 공원 정보를 가져오기 위한 DTO
@Parcel
public class ParkRowDTO {

    int park_key;
    String name;
    String address;
    String image_url;
    double latitude;
    double longitude;
    double distance;

    public int getPark_key() {
        return park_key;
    }

    public void setPark_key(int park_key) {
        this.park_key = park_key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(float longtitude) {
        this.longitude = longitude;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
