package com.example.han.boostcamp_walktogether.view.detail;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.han.boostcamp_walktogether.ActionBar.BackButtonActionBarActivity;
import com.example.han.boostcamp_walktogether.Adapters.LocationFreeboardViewPagerAdapter;
import com.example.han.boostcamp_walktogether.R;
import com.example.han.boostcamp_walktogether.data.FreeboardDTO;
import com.example.han.boostcamp_walktogether.data.FreeboardImageDTO;
import com.example.han.boostcamp_walktogether.util.RetrofitUtil;
import com.example.han.boostcamp_walktogether.util.StringKeys;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

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


    private ViewPager mLocationPicutreViewPager;
    private LocationFreeboardViewPagerAdapter locationFreeboardViewPagerAdapter;
    private DatabaseReference databaseReference;
    private String mLocationID;
    private int mLocationKey;
    private int mLocationFreeboardKey;
    private TextView mUserNameTextView;
    private TextView mTitleTextView;
    private TextView mTimeTextView;
    private TextView mContentTextView;
    private FreeboardDTO mFreeboardDTO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_location_freeboard_select,mFrameLayout,false);
        mFrameLayout.addView(contentView, 0);
        mTextView.setText(getResources().getString(R.string.location_freeboard_select_activity_title));

        mUserNameTextView = (TextView)findViewById(R.id.location_freeboard_select_user_detail);
        mTitleTextView = (TextView)findViewById(R.id.location_freeboard_select_title_detail);
        mTimeTextView = (TextView)findViewById(R.id.location_freeboard_select_time_detail);
        mContentTextView = (TextView) findViewById(R.id.location_freeboard_select_content);
        mLocationPicutreViewPager = (ViewPager)findViewById(R.id.location_freeboard_viewPager);

        locationFreeboardViewPagerAdapter = new LocationFreeboardViewPagerAdapter(this,getLayoutInflater(),getResources());
        mLocationPicutreViewPager.setAdapter(locationFreeboardViewPagerAdapter);
        //mLocationID = getIntent().getStringExtra(StringKeys.LOCATION_ID_KEY);
        //mLocationFreeboardKey=getIntent().getStringExtra(StringKeys.LOCATION_FREEBOARD_KEY);
        RetrofitUtil retrofitUtil = RetrofitUtil.retrofit.create(RetrofitUtil.class);

        Parcelable parcelable = getIntent().getParcelableExtra(StringKeys.LOCATION_FREEBOARD_PARCELABLE);
        mFreeboardDTO = Parcels.unwrap(parcelable);
        mLocationFreeboardKey = mFreeboardDTO.getFreeboard_key();
        mLocationKey = mFreeboardDTO.getPark_key();

        mUserNameTextView.setText(mFreeboardDTO.getUser_name());
        mTitleTextView.setText(mFreeboardDTO.getTitle());
        String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).format(mFreeboardDTO.getDate());
        mTimeTextView.setText(dateString);
        mContentTextView.setText(mFreeboardDTO.getContent());

        Call<ArrayList<FreeboardImageDTO>> imageArrayListCall = retrofitUtil.getImagesFreeboard(mLocationKey,mLocationFreeboardKey);
        imageArrayListCall.enqueue(imageArrayListCallback);

    }


    Callback<ArrayList<FreeboardImageDTO>> imageArrayListCallback = new Callback<ArrayList<FreeboardImageDTO>>() {
        @Override
        public void onResponse(Call<ArrayList<FreeboardImageDTO>> call, Response<ArrayList<FreeboardImageDTO>> response) {
            if(response.isSuccessful()){

                locationFreeboardViewPagerAdapter.setImageArrayList(response.body());
            }

        }

        @Override
        public void onFailure(Call<ArrayList<FreeboardImageDTO>> call, Throwable t) {

        }
    };



}
