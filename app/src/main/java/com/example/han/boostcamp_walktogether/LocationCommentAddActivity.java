package com.example.han.boostcamp_walktogether;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.han.boostcamp_walktogether.ActionBar.BackButtonActionBarActivity;

/**
 * Created by Han on 2017-07-29.
 */

public class LocationCommentAddActivity extends BackButtonActionBarActivity implements RatingBar.OnRatingBarChangeListener{

    private final static float DEFAULT_RATING_VALUE = 0;
    private RatingBar mLocationStarRatingBar;
    private RatingBar mLocationPetRatingBar;
    private TextView mLocationCommentStarScoreTextView;
    private TextView mLocationCommentPetScoreTextView;
    private EditText mLocationCommentEditText;

    private float valueOfRating;
    private String ratingString;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_location_comment_add, null, false);
        mFrameLayout.addView(contentView, 0);
        mTextView.setText(getResources().getString(R.string.location_comment_add_activity_title));


        mLocationCommentStarScoreTextView = (TextView)findViewById(R.id.location_comment_add_star_score);
        mLocationCommentPetScoreTextView = (TextView)findViewById(R.id.location_comment_add_pet_score);
        mLocationStarRatingBar = (RatingBar)findViewById(R.id.star_ratingBar);
        mLocationPetRatingBar = (RatingBar)findViewById(R.id.pet_ratingBar);
        mLocationCommentEditText = (EditText)findViewById(R.id.location_comment_add_editText);

        ratingString = String.format(getResources().getString(R.string.zero_to_five),DEFAULT_RATING_VALUE);
        mLocationStarRatingBar.setOnRatingBarChangeListener(this);
        mLocationPetRatingBar.setOnRatingBarChangeListener(this);

        mLocationCommentStarScoreTextView.setText(ratingString);
        mLocationCommentPetScoreTextView.setText(ratingString);
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        /*int integerNum,pointNum;
        float floatNum = rating;

        integerNum = (int)(rating / 2);

        pointNum = floatNum>integerNum ? 5 : 0;*/


        String str = String.format(getResources().getString(R.string.zero_to_five),rating);

        if(ratingBar.equals(mLocationStarRatingBar)){

            mLocationCommentStarScoreTextView.setText(str);

        }else if(ratingBar.equals(mLocationPetRatingBar)){

            mLocationCommentPetScoreTextView.setText(str);
        }

    }
}
