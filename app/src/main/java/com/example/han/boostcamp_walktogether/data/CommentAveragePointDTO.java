package com.example.han.boostcamp_walktogether.data;

/**
 * Created by Han on 2017-08-11.
 */

public class CommentAveragePointDTO {

    int park_key;
    float avgStar;
    float avgPet;


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

    public int getPark_key() {
        return park_key;
    }

    public void setPark_key(int park_key) {
        this.park_key = park_key;
    }
}
