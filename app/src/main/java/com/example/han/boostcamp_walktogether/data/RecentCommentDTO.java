package com.example.han.boostcamp_walktogether.data;

/**
 * Created by Han on 2017-08-13.
 */

public class RecentCommentDTO {

    String name;
    String address;
    String comment;
    float star_point;
    float pet_point;

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
}
