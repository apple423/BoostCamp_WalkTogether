package com.example.han.boostcamp_walktogether.util;

import com.example.han.boostcamp_walktogether.data.CommentAveragePointDTO;
import com.example.han.boostcamp_walktogether.data.CommentDTO;
import com.example.han.boostcamp_walktogether.data.FreeboardDTO;
import com.example.han.boostcamp_walktogether.data.FreeboardImageDTO;
import com.example.han.boostcamp_walktogether.data.ParkRowDTO;
import com.example.han.boostcamp_walktogether.data.RecentCommentDTO;
import com.kakao.network.response.ResponseBody;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
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
            .client(new OkHttpClient().newBuilder().
            addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build())
            .baseUrl("http://52.79.111.22:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    // 현재 사용자의 위도 경도를 받아 반경 1km이내에 공원들을 가져올 수 있다.
    @GET("/db/{myLatitude}/{myLongitude}")
    Call<ArrayList<ParkRowDTO>> getNearestPark(@Path("myLatitude") double myLatitude, @Path("myLongitude") double myLongitude);

    // 장소별 게시판에 게시물을 등록한다.
    @POST("/freeboard/add/{park_key}")
    Call<FreeboardDTO> postFreeboard(@Path("park_key") int park_key , @Body FreeboardDTO freeboardDTO);

    // 장소별 게시판에 게시글 추가에서 추가된 이미지를 서버에 등록한다.
    @Multipart
    @POST("/freeboard/add/image/{park_key}/{freeboard_key}")
    Call<ResponseBody> postFreeboardImgage(@Path("park_key") int park_key, @Path("freeboard_key") int freeboard_key,
    @Part MultipartBody.Part image, @Part("name") RequestBody name);

    // 사용자가 사진을 추가하지 않았을시 빈 문자열을 테이블에 insert
    @POST("/freeboard/add/image/empty")
    Call<FreeboardImageDTO> postEmptyImage(@Body FreeboardImageDTO freeboardImageDTO);

    // 장소별 게시판에 모든 등록 글을 가져오기 위함
    @GET("/freeboard/search/{park_key}")
    Call<ArrayList<FreeboardDTO>> getAllFreeboard(@Path("park_key") int park_key);

    // 장소별 게시판에 특정 게시물을 눌렀을때의 사진을 가져오기 위함
    @GET("/freeboard/search/images/{park_key}/{freeboard_key}")
    Call<ArrayList<FreeboardImageDTO>> getImagesFreeboard(@Path("park_key") int park_key, @Path("freeboard_key") int freeboard_key);

    // 장소별 게시판에 목록에 사진을 보여주기 위함
    @GET("/freeboard/search/oneImage/{park_key}/{freeboard_key}")
    Call<FreeboardImageDTO> getOneImageFreeboard(@Path("park_key") int park_key,@Path("freeboard_key") int freeboard_key);

    // 코멘트 등록
    @POST("/comment/add/{park_key}")
    Call<CommentDTO> postComment(@Path("park_key") int park_key, @Body CommentDTO commentDTO);

    // 코멘트 겟겟
   @GET("/comment/search/{park_key}")
    Call<ArrayList<CommentDTO>> getAllComment(@Path("park_key") int park_key);

    // 해당 장소의 평균 좋아요와 펫 빈도
   @GET("/comment/search/average/{park_key}")
    Call<CommentAveragePointDTO> getAveragePoint(@Path("park_key") int park_key);

    // 전체 댓글 중 최신 댓글을 5개 가져온다.
    @GET("/comment/recent")
    Call<ArrayList<RecentCommentDTO>> getRecentComment();

}
