package com.example.han.boostcamp_walktogether.data;

import org.parceler.Parcel;

/**
 * Created by Han on 2017-08-24.
 */
@Parcel
public class ParkListDTO{
        int park_key;
        String name;
        String address;
        String image_url;
        double latitude;
        double longitude;
        double distance;
        float avgStar;
        float avgPet;

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

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public float getAvgStar() {
        return avgStar;
    }

    public void setAvgStar(float avgStar) {
        this.avgStar = avgStar;
    }

    public float getAvgPet() {
        return avgPet;
    }

    public void setAvgPet(float avgPet) {
        this.avgPet = avgPet;
    }
}
