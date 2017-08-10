package com.example.han.boostcamp_walktogether.util;

import com.example.han.boostcamp_walktogether.data.FreeboardDTO;
import com.example.han.boostcamp_walktogether.data.FreeboardImageDTO;
import com.example.han.boostcamp_walktogether.data.ParkRowDTO;
import com.kakao.network.response.ResponseBody;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Han on 2017-08-08.
 */
// 서버에서 get과 post를 하기 위한 retrofit helper
public interface RetrofitUtil {


    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://52.79.111.22:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    // 현재 사용자의 위도 경도를 받아 반경 1km이내에 공원들을 가져올 수 있다.
    @GET("/db/{myLatitude}/{myLongitude}")
    Call<List<ParkRowDTO>> getNearestPark(@Path("myLatitude") double myLatitude, @Path("myLongitude") double myLongitude);

    // 장소별 게시판에 게시물을 등록한다.
    @POST("/freeboard/add/{park_key}")
    Call<FreeboardDTO> postFreeboard(@Path("park_key") int park_key , @Body FreeboardDTO freeboardDTO);

    // 장소별 게시판에 게시글 추가에서 추가된 이미지를 서버에 등록한다.
    @Multipart
    @POST("/freeboard/add/image/{park_key}/{freeboard_key}")
    Call<ResponseBody> postFreeboardImgage(@Path("park_key") int park_key, @Path("freeboard_key") int freeboard_key,
    @Part MultipartBody.Part image, @Part("name") RequestBody name);

    @GET("/freeboard/search/{park_key}")
    Call<List<FreeboardDTO>> getAllFreeboard(@Path("park_key") int park_key);

    @GET("/freeboard/search/images/{park_key}")
    Call<List<FreeboardImageDTO>> getImagesFreeboard(@Path("park_key") int park_key);

    @GET("/freeboard/search/oneImage/{park_key}/{freeboard_key}")
    Call<FreeboardImageDTO> getOneImageFreeboard(@Path("park_key") int park_key,@Path("freeboard_key") int freeboard_key);


}
