package com.example.han.boostcamp_walktogether.data;

/**
 * Created by Han on 2017-08-03.
 */

public class ParkRowDTO {

    String P_IDX;
    String P_PARK;
    String P_ADDR;
    String P_IMG;
    float LONGITUDE;
    float LATITUDE;


    public String getP_IDX() {
        return P_IDX;
    }

    public void setP_IDX(String p_IDX) {
        P_IDX = p_IDX;
    }

    public String getP_PARK() {
        return P_PARK;
    }

    public void setP_PARK(String p_PARK) {
        P_PARK = p_PARK;
    }

    public String getP_ADDR() {
        return P_ADDR;
    }

    public void setP_ADDR(String p_ADDR) {
        P_ADDR = p_ADDR;
    }


    public String getP_IMG() {
        return P_IMG;
    }

    public void setP_IMG(String p_IMG) {
        P_IMG = p_IMG;
    }


    public float getLONGITUDE() {
        return LONGITUDE;
    }

    public void setLONGITUDE(float LONGITUDE) {
        this.LONGITUDE = LONGITUDE;
    }

    public float getLATITUDE() {
        return LATITUDE;
    }

    public void setLATITUDE(float LATITUDE) {
        this.LATITUDE = LATITUDE;
    }

}
