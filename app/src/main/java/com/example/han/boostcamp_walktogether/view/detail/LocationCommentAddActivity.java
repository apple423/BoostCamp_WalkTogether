package com.example.han.boostcamp_walktogether.view.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.han.boostcamp_walktogether.ActionBar.BackButtonActionBarActivity;
import com.example.han.boostcamp_walktogether.R;
import com.example.han.boostcamp_walktogether.data.CommentDTO;
import com.example.han.boostcamp_walktogether.util.RetrofitUtil;
import com.example.han.boostcamp_walktogether.util.SharedPreferenceUtil;
import com.example.han.boostcamp_walktogether.util.StringKeys;

import org.parceler.Parcels;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Han on 2017-07-29.
 */

public class LocationCommentAddActivity extends BackButtonActionBarActivity implements RatingBar.OnRatingBarChangeListener{

    private final static float DEFAULT_RATING_VALUE = 0;
    private RatingBar mLocationStarRatingBar;
    private RatingBar mLocationPetRatingBar;
    private TextView mLocationCommentTitleTextView;
    private TextView mLocationCommentStarScoreTextView;
    private TextView mLocationCommentPetScoreTextView;
    private EditText mLocationCommentEditText;
    private Button mCommentAddButton;

    private float valueOfRating;
    private String ratingString;
    private int mParkKey;
    private String mUserName;
    private String mUserID;
    private String mUserProfile;
    private float mStarPoint;
    private float mPetPoint;
    private String mComment;
    private String mLocationName;
    private Activity mActivity;
    private RetrofitUtil retrofitUtil = RetrofitUtil.retrofit.create(RetrofitUtil.class);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_location_comment_add, null, false);
        mFrameLayout.addView(contentView, 0);
        mTextView.setText(getResources().getString(R.string.location_comment_add_activity_title));

        mActivity = this;
        mLocationCommentTitleTextView = (TextView)findViewById(R.id.location_comment_add_title_textView);
        mLocationCommentStarScoreTextView = (TextView)findViewById(R.id.location_comment_add_star_score);
        mLocationCommentPetScoreTextView = (TextView)findViewById(R.id.location_comment_add_pet_score);
        mLocationStarRatingBar = (RatingBar)findViewById(R.id.star_ratingBar);
        mLocationPetRatingBar = (RatingBar)findViewById(R.id.pet_ratingBar);
        mLocationCommentEditText = (EditText)findViewById(R.id.location_comment_add_editText);
        mCommentAddButton = (Button)findViewById(R.id.location_comment_add_button);

        mParkKey = getIntent().getIntExtra(StringKeys.LOCATION_ID_KEY,0);
        mLocationName = getIntent().getStringExtra(StringKeys.LOCATION_NAME);
        SharedPreferenceUtil.setUserProfileSharedPreference(this,StringKeys.USER_PROFILE, MODE_PRIVATE);
        mUserID = SharedPreferenceUtil.getUserProfile(StringKeys.USER_EMAIL);
        mUserName = SharedPreferenceUtil.getUserProfile(StringKeys.USER_NICK_NAME);
        mUserProfile = SharedPreferenceUtil.getUserProfile(StringKeys.USER_PROFILE_PICTURE);


        mLocationStarRatingBar.setOnRatingBarChangeListener(this);
        mLocationPetRatingBar.setOnRatingBarChangeListener(this);
        mCommentAddButton.setOnClickListener(onClickAddButton);

        ratingString = String.format(getResources().getString(R.string.zero_to_five),DEFAULT_RATING_VALUE);
        mLocationCommentStarScoreTextView.setText(ratingString);
        mLocationCommentPetScoreTextView.setText(ratingString);
        mLocationCommentTitleTextView.setText(mLocationName);

    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

        String str = String.format(getResources().getString(R.string.zero_to_five),rating);

        if(ratingBar.equals(mLocationStarRatingBar)){

            mStarPoint = rating;

            mLocationCommentStarScoreTextView.setText(str);

        }else if(ratingBar.equals(mLocationPetRatingBar)){

            mPetPoint = rating;

            mLocationCommentPetScoreTextView.setText(str);
        }

    }

    View.OnClickListener onClickAddButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            CommentDTO commentDTO = setCommentDTO();
            Call<CommentDTO> commentDTOCall = retrofitUtil.postComment(mParkKey,commentDTO);
            commentDTOCall.enqueue(commentDTOCallback);

        }
    };

    private CommentDTO setCommentDTO() {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setPark_key(mParkKey);
        commentDTO.setUser_name(mUserName);
        commentDTO.setUser_id(mUserID);
        commentDTO.setUser_profile(mUserProfile);
        mComment = mLocationCommentEditText.getText().toString();
        commentDTO.setComment(mComment);
        commentDTO.setStar_point(mStarPoint);
        commentDTO.setPet_point(mPetPoint);
        return commentDTO;
    }

    Callback<CommentDTO> commentDTOCallback = new Callback<CommentDTO>() {
        @Override
        public void onResponse(Call<CommentDTO> call, Response<CommentDTO> response) {
            if(response.isSuccessful()){
                Log.d("SuccessPostComment","yes");

                Call<ArrayList<CommentDTO>> getAllCommentCall = retrofitUtil.getAllComment(mParkKey);
                getAllCommentCall.enqueue(getAllCommentCallback);

            }

        }

        @Override
        public void onFailure(Call<CommentDTO> call, Throwable t) {

        }
    };


    Callback<ArrayList<CommentDTO>> getAllCommentCallback = new Callback<ArrayList<CommentDTO>>() {
        @Override
        public void onResponse(Call<ArrayList<CommentDTO>> call, Response<ArrayList<CommentDTO>> response) {
            if(response.isSuccessful()){

                Log.d("SuccessCommentList","yes");
                ArrayList<CommentDTO> commentArrayList = response.body();
                Intent intent = new Intent();
                intent.putExtra(StringKeys.COMMENT_LIST, Parcels.wrap(commentArrayList));
                mActivity.setResult(RESULT_OK,intent);
                finish();


            }

        }

        @Override
        public void onFailure(Call<ArrayList<CommentDTO>> call, Throwable t) {

        }
    };

}
