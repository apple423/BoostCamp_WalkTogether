package com.example.han.boostcamp_walktogether.view;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.han.boostcamp_walktogether.ActionBar.DrawerBaseActivity;
import com.example.han.boostcamp_walktogether.Adapters.WalkDiaryAdapter;
import com.example.han.boostcamp_walktogether.LocationUpdateService;
import com.example.han.boostcamp_walktogether.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Han on 2017-08-15.
 */

public class WalkDiaryActivity extends DrawerBaseActivity implements OnMapReadyCallback{

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int DEFAULT_ZOOM = 15;
    private Button mStartWalkingButton, mStopWalkingButton;
    private LocationUpdateService mLocationUpdateService;
    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;

    private RecyclerView mDiaryRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private WalkDiaryAdapter mWalkDiaryAdapter;

    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;
    private GoogleApiClient mGoogleApiClient;
    private OnMapReadyCallback onMapReadyCallback;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_walk_diary, mFrameLayout, false);
        mDrawerLayout.addView(contentView, 0);

        onMapReadyCallback = this;
        mStartWalkingButton = (Button)findViewById(R.id.walk_start_button);
        mStopWalkingButton = (Button)findViewById(R.id.walk_stop_button);
        mDiaryRecyclerView = (RecyclerView)findViewById(R.id.walk_diary_recyclerView);

        mLinearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mWalkDiaryAdapter = new WalkDiaryAdapter();
        mDiaryRecyclerView.setLayoutManager(mLinearLayoutManager);
        mDiaryRecyclerView.setAdapter(mWalkDiaryAdapter);
        //mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mLocationUpdateService =new LocationUpdateService(this);

        mStartWalkingButton.setOnClickListener(onClickButtonListener);
        mStopWalkingButton.setOnClickListener(onClickButtonListener);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();

        mMapFragment = SupportMapFragment.newInstance();
        //mMapFragment.getMapAsync(onMapReadyCallback);

    }

    View.OnClickListener onClickButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();


            switch (id){

                case R.id.walk_start_button :

                    mStartWalkingButton.setVisibility(View.GONE);
                    mStopWalkingButton.setVisibility(View.VISIBLE);
                    mDiaryRecyclerView.setVisibility(View.GONE);

                    android.support.v4.app.FragmentTransaction fragmentTransaction =
                            getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.walk_diary_frame, mMapFragment);
                    fragmentTransaction.commit();
                    mMapFragment.getMapAsync(onMapReadyCallback);



                    break;

                case R.id.walk_stop_button :

                    android.support.v4.app.FragmentTransaction fragmentTransactionStop =
                            getSupportFragmentManager().beginTransaction();
                    fragmentTransactionStop.remove(mMapFragment);
                    fragmentTransactionStop.commit();

                    mLocationUpdateService.stopLocationUpdates();
                    mDiaryRecyclerView.setVisibility(View.VISIBLE);
                    mStartWalkingButton.setVisibility(View.VISIBLE);
                    mStopWalkingButton.setVisibility(View.GONE);
            }


        }
    };


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getDeviceLocation();
        mLocationUpdateService.setmMap(mMap);
        mLocationUpdateService.createLocationRequest();
        mLocationUpdateService.startLocationUpdates();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationUpdateService.stopLocationUpdates();
    }

    private void getDeviceLocation() {


        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if (mLocationPermissionGranted) {
            // LoactionService를 통해 현재 위치를 얻어온다.
            mLastKnownLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
        }


        if (mLastKnownLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));


        }
    }
}
