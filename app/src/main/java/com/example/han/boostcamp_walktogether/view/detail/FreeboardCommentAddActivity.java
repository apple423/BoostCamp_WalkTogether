package com.example.han.boostcamp_walktogether.view.detail;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.han.boostcamp_walktogether.ActionBar.BackButtonActionBarActivity;
import com.example.han.boostcamp_walktogether.Adapters.LocationFreeboardCommentAdapter;
import com.example.han.boostcamp_walktogether.Adapters.LocationFreeboardCommentDetailAdapter;
import com.example.han.boostcamp_walktogether.R;
import com.example.han.boostcamp_walktogether.data.FreeboardCommentDTO;
import com.example.han.boostcamp_walktogether.data.FreeboardDTO;
import com.example.han.boostcamp_walktogether.util.RetrofitUtil;
import com.example.han.boostcamp_walktogether.util.SharedPreferenceUtil;
import com.example.han.boostcamp_walktogether.util.StringKeys;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Han on 2017-08-18.
 */

public class FreeboardCommentAddActivity extends BackButtonActionBarActivity {


    private int mFreeboardKey,mCountComment;
    private RecyclerView mCommentRecyclerView;
    private LinearLayout mNoDataLinearLayout;
    private Button mButtonPost;
    private EditText mFreeboardCommentEditText;
    private String mUserEmail,mUserProfile,mUserName;
    private ArrayList<FreeboardCommentDTO> mFreeboardCommentDTOArrayList;
    private RetrofitUtil mRetrofitUtil = RetrofitUtil.retrofit.create(RetrofitUtil.class);
    private LocationFreeboardCommentDetailAdapter mLocationFreeboardCommentDetailAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_location_freeboard_comment, null, false);
        mFrameLayout.addView(contentView, 0);
        mTextView.setText(getResources().getString(R.string.location_freeboard_comment_title));

        mCommentRecyclerView = (RecyclerView)findViewById(R.id.location_freeboard_comment_detail_recyclerView);
        mNoDataLinearLayout = (LinearLayout)findViewById(R.id.location_freeboard_comment_detail_noData);
        mButtonPost = (Button)findViewById(R.id.location_freeboard_comment_detail_add_button);
        mFreeboardCommentEditText = (EditText) findViewById(R.id.location_freeboard_comment_detail_editText);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mLocationFreeboardCommentDetailAdapter = new LocationFreeboardCommentDetailAdapter(this,getResources());

        mCommentRecyclerView.setLayoutManager(linearLayoutManager);
        mCommentRecyclerView.setAdapter(mLocationFreeboardCommentDetailAdapter);

        mFreeboardKey = getIntent().getIntExtra(StringKeys.LOCATION_FREEBOARD_KEY,0);

        SharedPreferenceUtil.setUserProfileSharedPreference(this,StringKeys.USER_PROFILE,MODE_PRIVATE);
        mUserEmail = SharedPreferenceUtil.getUserProfile(StringKeys.USER_EMAIL);
        mUserName = SharedPreferenceUtil.getUserProfile(StringKeys.USER_NICK_NAME);
        mUserProfile = SharedPreferenceUtil.getUserProfile(StringKeys.USER_PROFILE_PICTURE);


        mButtonPost.setOnClickListener(onClickPostButtonListener);

        getFreeboardComment();

    }

    private void getFreeboardComment() {
        Call<ArrayList<FreeboardCommentDTO>> getFreeboardComment = mRetrofitUtil.getFreeboardComment(mFreeboardKey);
        getFreeboardComment.enqueue(getFreeboardCommentCallback);
    }


    Callback<ArrayList<FreeboardCommentDTO>> getFreeboardCommentCallback = new Callback<ArrayList<FreeboardCommentDTO>>() {
        @Override
        public void onResponse(Call<ArrayList<FreeboardCommentDTO>> call, Response<ArrayList<FreeboardCommentDTO>> response) {
            if(response.isSuccessful()){
                ArrayList<FreeboardCommentDTO> freeboardCommentDTOArrayList = response.body();
                if(freeboardCommentDTOArrayList.size()==0){

                    mCommentRecyclerView.setVisibility(View.GONE);
                    mNoDataLinearLayout.setVisibility(View.VISIBLE);

                }
                else{

                    mLocationFreeboardCommentDetailAdapter.setFreeboardCommentDTOArrayList(freeboardCommentDTOArrayList);
                    mCommentRecyclerView.setVisibility(View.VISIBLE);
                    mNoDataLinearLayout.setVisibility(View.GONE);
                }


            }
            else{

                mCommentRecyclerView.setVisibility(View.GONE);
                mNoDataLinearLayout.setVisibility(View.VISIBLE);

            }
        }

        @Override
        public void onFailure(Call<ArrayList<FreeboardCommentDTO>> call, Throwable t) {

        }
    };

    View.OnClickListener onClickPostButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FreeboardCommentDTO freeboardCommentDTO = new FreeboardCommentDTO();

            String comment = mFreeboardCommentEditText.getText().toString();
            freeboardCommentDTO.setFreeboard_key(mFreeboardKey);
            freeboardCommentDTO.setUser_email(mUserEmail);
            freeboardCommentDTO.setUser_name(mUserName);
            freeboardCommentDTO.setUser_profile(mUserProfile);
            freeboardCommentDTO.setComment(comment);

            Call<FreeboardCommentDTO> postFreeboardCommentCall = mRetrofitUtil.postFreeboardComment(freeboardCommentDTO);
            postFreeboardCommentCall.enqueue(postFreeboardCommentCallback);



        }
    };

    Callback<FreeboardCommentDTO> postFreeboardCommentCallback = new Callback<FreeboardCommentDTO>() {
        @Override
        public void onResponse(Call<FreeboardCommentDTO> call, Response<FreeboardCommentDTO> response) {
            if(response.isSuccessful()){

               getFreeboardComment();

            }

        }

        @Override
        public void onFailure(Call<FreeboardCommentDTO> call, Throwable t) {

        }
    };


    @Override
    public void onBackPressed() {
/*
        Call<FreeboardCommentDTO> getFreeboardCommentCountCall = mRetrofitUtil.getFreeboardCommentCount(mFreeboardKey);
        getFreeboardCommentCountCall.enqueue(getFreeboardCommentCountCallback);

        Call<ArrayList<FreeboardCommentDTO>> getFreeboardCommentFiveCall = mRetrofitUtil.getFreeboardCommentFive(mFreeboardKey);
        getFreeboardCommentFiveCall.enqueue(getFiveFreeboardCommentCallback);*/
        new AsyncCommentForResult().execute();

    }


    Callback<ArrayList<FreeboardCommentDTO>> getFiveFreeboardCommentCallback = new Callback<ArrayList<FreeboardCommentDTO>>() {
        @Override
        public void onResponse(Call<ArrayList<FreeboardCommentDTO>> call, Response<ArrayList<FreeboardCommentDTO>> response) {

            mFreeboardCommentDTOArrayList = response.body();
        }

        @Override
        public void onFailure(Call<ArrayList<FreeboardCommentDTO>> call, Throwable t) {

        }
    };

    Callback<FreeboardCommentDTO> getFreeboardCommentCountCallback = new Callback<FreeboardCommentDTO>() {
        @Override
        public void onResponse(Call<FreeboardCommentDTO> call, Response<FreeboardCommentDTO> response) {
            if(response.isSuccessful()){

                mCountComment = response.body().getComment_count();

            }

        }

        @Override
        public void onFailure(Call<FreeboardCommentDTO> call, Throwable t) {

        }
    };

    public class AsyncCommentForResult extends AsyncTask<Void,Void,Void>{


        @Override
        protected Void doInBackground(Void... params) {
            Call<FreeboardCommentDTO> getFreeboardCommentCountCall = mRetrofitUtil.getFreeboardCommentCount(mFreeboardKey);
            try {
                mCountComment = getFreeboardCommentCountCall.execute().body().getComment_count();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Call<ArrayList<FreeboardCommentDTO>> getFreeboardCommentFiveCall = mRetrofitUtil.getFreeboardCommentFive(mFreeboardKey);
            try {
                mFreeboardCommentDTOArrayList = getFreeboardCommentFiveCall.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Intent intent = new Intent();
            intent.putExtra(StringKeys.FREEBOARD_COMMNET_COUNT,mCountComment);
            intent.putExtra(StringKeys.FREEBOARD_COMMNETS, Parcels.wrap(mFreeboardCommentDTOArrayList));
            setResult(RESULT_OK,intent);
            finish();

        }
    }
}
