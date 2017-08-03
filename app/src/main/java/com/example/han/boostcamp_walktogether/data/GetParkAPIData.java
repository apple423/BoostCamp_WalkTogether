package com.example.han.boostcamp_walktogether.data;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by Han on 2017-08-03.
 */

public interface GetParkAPIData {

    @GET("/63574b517561707038304542595071/json/SearchParkInfoService/1/97/")
    Call<ParkAPIDTO> getAllParkData();

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://openAPI.seoul.go.kr:8088")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
