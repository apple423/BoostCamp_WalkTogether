package com.example.han.boostcamp_walktogether.view.detail;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.han.boostcamp_walktogether.ActionBar.BackButtonActionBarActivity;
import com.example.han.boostcamp_walktogether.Adapters.LocationFreeboardCommentAdapter;
import com.example.han.boostcamp_walktogether.Adapters.LocationFreeboardViewPagerAdapter;
import com.example.han.boostcamp_walktogether.R;
import com.example.han.boostcamp_walktogether.data.FreeboardCommentDTO;
import com.example.han.boostcamp_walktogether.data.FreeboardDTO;
import com.example.han.boostcamp_walktogether.data.FreeboardImageDTO;
import com.example.han.boostcamp_walktogether.util.RetrofitUtil;
import com.example.han.boostcamp_walktogether.util.SharedPreferenceUtil;
import com.example.han.boostcamp_walktogether.util.StringKeys;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kakao.network.response.ResponseBody;
import com.ldoublem.thumbUplib.ThumbUpView;

import org.parceler.Parcels;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Han on 2017-07-28.
 */

public class LocationFreeboardSelectActivity extends BackButtonActionBarActivity{


    private final static int COMMENT_ADD_SCREEN = 101;
    private ViewPager mLocationPicutreViewPager;
    private LocationFreeboardViewPagerAdapter locationFreeboardViewPagerAdapter;
    private DatabaseReference databaseReference;
    private String mLocationID;
    private Context mContext;
    private int mLocationKey;
    private int mLocationFreeboardKey;
    private TextView mUserNameTextView;
    private TextView mTitleTextView;
    private TextView mTimeTextView;
    private TextView mContentTextView;
    private TextView mCommentTextView;
    private TextView mShowCommentTextView;
    private FreeboardDTO mFreeboardDTO;
    private ImageView mUserProfileImageView;
    private RecyclerView mFreeboardCommentRecyclerview;
    private ThumbUpView mThumbUpView;
    private String mUserEmail;
    private FreeboardDTO mFreeboardUserLikeSendDTO;
    private LocationFreeboardCommentAdapter mLocationFreeboardCommentAdapter;
    private int mCountComment;
    private ArrayList<FreeboardDTO> mFreeboardLikeList;
    private ArrayList<FreeboardDTO> mFreeboardUserLikeList;
    private RetrofitUtil retrofitUtil = RetrofitUtil.retrofit.create(RetrofitUtil.class);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_location_freeboard_select,mFrameLayout,false);
        mFrameLayout.addView(contentView, 0);
        mTextView.setText(getResources().getString(R.string.location_freeboard_select_activity_title));

        mContext = this;
        mUserNameTextView = (TextView)findViewById(R.id.location_freeboard_select_user_detail);
        mTitleTextView = (TextView)findViewById(R.id.location_freeboard_select_title_detail);
        mTimeTextView = (TextView)findViewById(R.id.location_freeboard_select_time_detail);
        mContentTextView = (TextView) findViewById(R.id.location_freeboard_select_content);
        mLocationPicutreViewPager = (ViewPager)findViewById(R.id.location_freeboard_viewPager);
        mUserProfileImageView = (ImageView)findViewById(R.id.location_freeboard_select_user_profile);
        mFreeboardCommentRecyclerview = (RecyclerView)findViewById(R.id.location_freeboard_comment_recyclerView);
        mCommentTextView = (TextView)findViewById(R.id.location_freeboard_select_comment_textView);
        mThumbUpView = (ThumbUpView)findViewById(R.id.location_freeboard_select_like);
        mShowCommentTextView = (TextView) findViewById(R.id.location_freeboard_comment_show_textView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mLocationFreeboardCommentAdapter = new LocationFreeboardCommentAdapter(this,getResources());
        mFreeboardCommentRecyclerview.setLayoutManager(linearLayoutManager);
        mFreeboardCommentRecyclerview.setAdapter(mLocationFreeboardCommentAdapter);

        locationFreeboardViewPagerAdapter = new LocationFreeboardViewPagerAdapter(this,getLayoutInflater(),getResources());
        mLocationPicutreViewPager.setAdapter(locationFreeboardViewPagerAdapter);


        Parcelable parcelable = getIntent().getParcelableExtra(StringKeys.LOCATION_FREEBOARD_PARCELABLE);
        mFreeboardDTO = Parcels.unwrap(parcelable);
        mLocationFreeboardKey = mFreeboardDTO.getFreeboard_key();
        mLocationKey = mFreeboardDTO.getPark_key();
        SharedPreferenceUtil.setUserProfileSharedPreference(this,StringKeys.USER_PROFILE,MODE_PRIVATE);
        mUserEmail = SharedPreferenceUtil.getUserProfile(StringKeys.USER_EMAIL);
        mFreeboardUserLikeSendDTO = new FreeboardDTO();
        mFreeboardLikeList = new ArrayList<>();
        mFreeboardUserLikeList = new ArrayList<>();

        drawView();


        mCommentTextView.setOnClickListener(onClickCommentListener);
        mShowCommentTextView.setOnClickListener(onClickCommentListener);
        mThumbUpView.setOnThumbUp(onClickLike);


        Call<FreeboardCommentDTO> getFreeboardCommentCountCall = retrofitUtil.getFreeboardCommentCount(mLocationFreeboardKey);
        getFreeboardCommentCountCall.enqueue(getFreeboardCommentCountCallback);

        Call<ArrayList<FreeboardImageDTO>> imageArrayListCall = retrofitUtil.getImagesFreeboard(mLocationKey,mLocationFreeboardKey);
        imageArrayListCall.enqueue(imageArrayListCallback);

        Call<FreeboardDTO> getFreeboardUserLikeCall = retrofitUtil.getUserPushLike(mLocationFreeboardKey,mUserEmail);
        getFreeboardUserLikeCall.enqueue(getFreeboardLikeUserCallback);

        Call<ArrayList<FreeboardCommentDTO>> getFreeboardCommentFiveCall = retrofitUtil.getFreeboardCommentFive(mLocationFreeboardKey);
        getFreeboardCommentFiveCall.enqueue(getFreeboardCommentCallback);

    }

    View.OnClickListener onClickCommentListener = new View.OnClickListener(){


        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext,FreeboardCommentAddActivity.class);
            intent.putExtra(StringKeys.LOCATION_FREEBOARD_KEY,mLocationFreeboardKey);
            //mContext.startActivity(intent);
            startActivityForResult(intent,COMMENT_ADD_SCREEN);

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==COMMENT_ADD_SCREEN && resultCode ==RESULT_OK){

            mCountComment = data.getIntExtra(StringKeys.FREEBOARD_COMMNET_COUNT,0);
            String commentCount = String.format(
                    getResources().getString(R.string.location_freeboard_comment),mCountComment);
            mCommentTextView.setText(commentCount);

            Parcelable freeboardCommentsListParcelabe = data.getParcelableExtra(StringKeys.FREEBOARD_COMMNETS);
            ArrayList<FreeboardCommentDTO> freeboardCommentDTOArrayList = Parcels.unwrap(freeboardCommentsListParcelabe);
            mLocationFreeboardCommentAdapter.setFreeboardCommentDTOArrayList(freeboardCommentDTOArrayList);



        }

    }

    private void drawView() {
        mUserNameTextView.setText(mFreeboardDTO.getUser_name());
        mTitleTextView.setText(mFreeboardDTO.getTitle());
        String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).format(mFreeboardDTO.getDate());
        mTimeTextView.setText(dateString);
        mContentTextView.setText(mFreeboardDTO.getContent());
        Glide.with(this).load(mFreeboardDTO.getUser_profile()).into(mUserProfileImageView);
    }

    Callback<FreeboardCommentDTO> getFreeboardCommentCountCallback = new Callback<FreeboardCommentDTO>() {
        @Override
        public void onResponse(Call<FreeboardCommentDTO> call, Response<FreeboardCommentDTO> response) {
            if(response.isSuccessful()){

                mCountComment = response.body().getComment_count();
                String commentCount = String.format(
                        getResources().getString(R.string.location_freeboard_comment),mCountComment);
                mCommentTextView.setText(commentCount);
            }

        }

        @Override
        public void onFailure(Call<FreeboardCommentDTO> call, Throwable t) {

        }
    };

    Callback<ArrayList<FreeboardImageDTO>> imageArrayListCallback = new Callback<ArrayList<FreeboardImageDTO>>() {
        @Override
        public void onResponse(Call<ArrayList<FreeboardImageDTO>> call, Response<ArrayList<FreeboardImageDTO>> response) {
            if(response.isSuccessful()){

                ArrayList<FreeboardImageDTO> freeboardImageDTOs = response.body();
                if(freeboardImageDTOs.get(0).getImage().equals("empty")) mLocationPicutreViewPager.setVisibility(View.GONE);
                locationFreeboardViewPagerAdapter.setImageArrayList(response.body());
            }

        }

        @Override
        public void onFailure(Call<ArrayList<FreeboardImageDTO>> call, Throwable t) {

        }
    };


    Callback<FreeboardDTO> getFreeboardLikeUserCallback = new Callback<FreeboardDTO>() {
        @Override
        public void onResponse(Call<FreeboardDTO> call, Response<FreeboardDTO> response) {
            if (response.isSuccessful()) {

                int userLike = response.body().getLike_count();

                if(userLike == 0 ){

                    mThumbUpView.setUnlike();
                }
                else{

                    mThumbUpView.setLike();
                }

            }

        }

        @Override
        public void onFailure(Call<FreeboardDTO> call, Throwable t) {

        }
    };

    ThumbUpView.OnThumbUp onClickLike = new ThumbUpView.OnThumbUp() {
        @Override
        public void like(boolean like) {
            if(like){
                Call<FreeboardDTO> freeboardLikeDTOCall
                        = retrofitUtil.postLike(mLocationFreeboardKey, mUserEmail, mFreeboardUserLikeSendDTO);
                freeboardLikeDTOCall.enqueue(new Callback<FreeboardDTO>() {
                    @Override
                    public void onResponse(Call<FreeboardDTO> call, Response<FreeboardDTO> response) {
                        if (response.isSuccessful()) {

                            Log.d("LikePostSuccess", "tetete");
                        }

                    }

                    @Override
                    public void onFailure(Call<FreeboardDTO> call, Throwable t) {

                    }
                });

            }
            else{
                Call<com.kakao.network.response.ResponseBody> freeboardLikeDeleteCall
                        = retrofitUtil.deleteLike(mLocationFreeboardKey, mUserEmail);
                freeboardLikeDeleteCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {

                            Log.d("DeleteLikeSuccess", "gsasga");

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });

            }


        }
    };


    Callback<ArrayList<FreeboardCommentDTO>> getFreeboardCommentCallback = new Callback<ArrayList<FreeboardCommentDTO>>() {
        @Override
        public void onResponse(Call<ArrayList<FreeboardCommentDTO>> call, Response<ArrayList<FreeboardCommentDTO>> response) {
            if(response.isSuccessful()){
                ArrayList<FreeboardCommentDTO> freeboardCommentDTOArrayList = response.body();

                    mLocationFreeboardCommentAdapter.setFreeboardCommentDTOArrayList(freeboardCommentDTOArrayList);
                    if (freeboardCommentDTOArrayList.size()>=5){

                        mShowCommentTextView.setVisibility(View.VISIBLE);
                    }

            }
        }

        @Override
        public void onFailure(Call<ArrayList<FreeboardCommentDTO>> call, Throwable t) {

        }
    };

    @Override
    public void onBackPressed() {
        new AsyncCountLike().execute();
    }

    public class AsyncCountLike extends AsyncTask<Void,Void,Void>{


        @Override
        protected Void doInBackground(Void... params) {

            Call<ArrayList<FreeboardDTO>> getAllFreeboardCall = retrofitUtil.getAllFreeboard(mLocationKey);
            try {
                ArrayList<FreeboardDTO> freeboardArraylist = getAllFreeboardCall.execute().body();
                for(FreeboardDTO data : freeboardArraylist){
                    Call<FreeboardDTO> getLikeCall = retrofitUtil.getLikeCount(data.getFreeboard_key());
                    mFreeboardLikeList.add(getLikeCall.execute().body());
                    Call<FreeboardDTO> getUserLikeCall = retrofitUtil.getUserPushLike(data.getFreeboard_key(),mUserEmail);
                    mFreeboardUserLikeList.add(getUserLikeCall.execute().body());

                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Intent intent = new Intent();
            intent.putExtra(StringKeys.PARK_LIKE_LIST,Parcels.wrap(mFreeboardLikeList));
            intent.putExtra(StringKeys.PARK_USER_LIKE,Parcels.wrap(mFreeboardUserLikeList));
            setResult(RESULT_OK,intent);
            finish();

        }
    }
}
