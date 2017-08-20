package com.example.han.boostcamp_walktogether.util;

import com.example.han.boostcamp_walktogether.data.CommentAveragePointDTO;
import com.example.han.boostcamp_walktogether.data.CommentDTO;
import com.example.han.boostcamp_walktogether.data.FreeboardCommentDTO;
import com.example.han.boostcamp_walktogether.data.FreeboardDTO;
import com.example.han.boostcamp_walktogether.data.FreeboardImageDTO;
import com.example.han.boostcamp_walktogether.data.ParkRowDTO;
import com.example.han.boostcamp_walktogether.data.RecentCommentDTO;
import com.example.han.boostcamp_walktogether.data.WalkDiaryDTO;
import com.example.han.boostcamp_walktogether.data.WalkDiaryImageDTO;
import com.kakao.network.response.ResponseBody;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

    // 산책일지를 서버에 추가하기 위함
    @POST("/walk-diary/add")
    Call<WalkDiaryDTO> postWalkDiary(@Body WalkDiaryDTO walkDiaryDTO);

    // 산책일지의 이미지를 업로드 하기 위함
    @Multipart
    @POST("/walk-diary/add/map_image/{diary_key}/{user_email}")
    Call<ResponseBody> postWalkDiaryMapImage(@Path("diary_key") int diary_key, @Path("user_email") String user_email,
                                             @Part MultipartBody.Part image, @Part("name") RequestBody name);

    // 유저의 산책일지를 서버에서 가져오기 위함
    @GET("/walk-diary/search/{user_email}")
    Call<ArrayList<WalkDiaryDTO>> getUsersWalkDiary(@Path("user_email") String user_email);

    // 유저의 산책일지의 사진을 가져오기 위함
    @GET("/walk-diary/search/images/{user_email}")
    Call<ArrayList<WalkDiaryImageDTO>> getUserWalkDiaryImages(@Path("user_email") String user_email);

    // 유저가 선택한 산책일지를 삭제하기 위함
    @DELETE("/walk-diary/delete/{diary_key}/{user_email}")
    Call<ResponseBody> deleteWalkDiary(@Path("diary_key") int diary_key, @Path("user_email") String user_email);

    // 유저가 선택한 산책일지와 함께 사진을 서버에서 지우기 위함
    @DELETE("/walk-diary/delete/images/{diary_key}/{user_email}/{image_name}")
    Call<ResponseBody> deleteWalkDiaryImage(@Path("diary_key") int diary_key, @Path("user_email") String user_email,
                                            @Path("image_name") String image_name);

    // 선택 게시글의 좋아요를 했음을 보내기 위함
    @POST("/freeboard/like/add/{freeboard_key}/{user_email}")
    Call<FreeboardDTO> postLike(@Path("freeboard_key") int freeboard_key, @Path("user_email") String user_email,
                                @Body FreeboardDTO freeboardDTO);

    // 선택 게시글의 좋아요를 삭제하기 위함
    @DELETE("/freeboard/like/delete/{freeboard_key}/{user_email}")
    Call<ResponseBody> deleteLike(@Path("freeboard_key") int freeboard_key, @Path("user_email") String user_email);

    // 유저가 좋아요를 눌렀나 안 눌렀나
    @GET("/freeboard/like/search/{freeboard_key}/{user_email}")
    Call<FreeboardDTO> getUserPushLike(@Path("freeboard_key") int freeboard_key, @Path("user_email") String user_email);

    // 모든 게시글의 좋아요 수를 가져오기 위함
    @GET("/freeboard/like/search/{freeboard_key}")
    Call<FreeboardDTO> getLikeCount(@Path("freeboard_key") int freeboard_key);

    // 게시글의 댓글 수를 가져오기 위함
    @GET("/freeboard/comment/search/count/{freeboard_key}")
    Call<FreeboardCommentDTO> getFreeboardCommentCount(@Path("freeboard_key") int freeboard_key);

    // 게시글의 댓글을 서버에 추가하기 위함
    @POST("/freeboard/comment/add")
    Call<FreeboardCommentDTO> postFreeboardComment(@Body FreeboardCommentDTO freeboardCommentDTO);

    // 게시글의 댓글들을 모두 가져오기 위함
    @GET("/freeboard/comment/search/{freeboard_key}")
    Call<ArrayList<FreeboardCommentDTO>> getFreeboardComment(@Path("freeboard_key") int freeboard_key);

    // 게시글의 댓글들을 모두 가져오기 위함
    @GET("/freeboard/comment/search/five/{freeboard_key}")
    Call<ArrayList<FreeboardCommentDTO>> getFreeboardCommentFive(@Path("freeboard_key") int freeboard_key);


}
