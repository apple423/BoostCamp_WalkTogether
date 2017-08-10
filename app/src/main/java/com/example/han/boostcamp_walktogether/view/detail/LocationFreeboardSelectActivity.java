package com.example.han.boostcamp_walktogether.view.detail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.han.boostcamp_walktogether.ActionBar.BackButtonActionBarActivity;
import com.example.han.boostcamp_walktogether.Adapters.LocationFreeboardViewPagerAdapter;
import com.example.han.boostcamp_walktogether.R;
import com.example.han.boostcamp_walktogether.data.FreeboardSelectedDTO;
import com.example.han.boostcamp_walktogether.helper.FirebaseHelper;
import com.example.han.boostcamp_walktogether.util.StringKeys;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

/**
 * Created by Han on 2017-07-28.
 */

public class LocationFreeboardSelectActivity extends BackButtonActionBarActivity{


    private ViewPager mLocationPicutreViewPager;
    private LocationFreeboardViewPagerAdapter locationFreeboardViewPagerAdapter;
    private DatabaseReference databaseReference;
    private String mLocationID;
    private String mLocationFreeboardKey;
    private TextView mUserNameTextView;
    private TextView mTitleTextView;
    private TextView mTimeTextView;
    private TextView mContentTextView;

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
        mLocationID = getIntent().getStringExtra(StringKeys.LOCATION_ID_KEY);
        mLocationFreeboardKey=getIntent().getStringExtra(StringKeys.LOCATION_FREEBOARD_KEY);
       // databaseReference = FirebaseHelper.getParkFreeboardSelectedDataReferences(mLocationID,mLocationFreeboardKey);
        //databaseReference.addValueEventListener(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener(){


        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            FreeboardSelectedDTO data = dataSnapshot.getValue(FreeboardSelectedDTO.class);
            mUserNameTextView.setText(data.getName());
            mTitleTextView.setText(data.getTitle());
            mTimeTextView.setText(data.getTime());
            mContentTextView.setText(data.getContent());
            locationFreeboardViewPagerAdapter.setImageArrayList(data.getImageArrayList());

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };



}
