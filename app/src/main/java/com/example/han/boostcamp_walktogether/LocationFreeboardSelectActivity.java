package com.example.han.boostcamp_walktogether;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.example.han.boostcamp_walktogether.ActionBar.BackButtonActionBarActivity;
import com.example.han.boostcamp_walktogether.Adapters.LocationFreeboardViewPagerAdapter;

/**
 * Created by Han on 2017-07-28.
 */

public class LocationFreeboardSelectActivity extends BackButtonActionBarActivity{


    private ViewPager mLocationPicutreViewPager;
    private LocationFreeboardViewPagerAdapter locationFreeboardViewPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_location_freeboard_select,mFrameLayout,false);
        mFrameLayout.addView(contentView, 0);
        mTextView.setText(getResources().getString(R.string.location_freeboard_select_activity_title));

        mLocationPicutreViewPager = (ViewPager)findViewById(R.id.location_freeboard_viewPager);
        locationFreeboardViewPagerAdapter = new LocationFreeboardViewPagerAdapter(getLayoutInflater(),getResources());
        mLocationPicutreViewPager.setAdapter(locationFreeboardViewPagerAdapter);


    }
}
