package com.example.han.boostcamp_walktogether.view;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.han.boostcamp_walktogether.ActionBar.DrawerBaseActivity;
import com.example.han.boostcamp_walktogether.Adapters.WalkDiaryAdapter;
import com.example.han.boostcamp_walktogether.LocationUpdateService;
import com.example.han.boostcamp_walktogether.R;
import com.example.han.boostcamp_walktogether.util.SharedPreferenceUtil;
import com.example.han.boostcamp_walktogether.util.StringKeys;
import com.example.han.boostcamp_walktogether.widget.WalkDiaryAddDialog;
import com.example.han.boostcamp_walktogether.widget.WalkDiaryInfoInMapDialog;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileOutputStream;

import static com.example.han.boostcamp_walktogether.util.StringKeys.USER_EMAIL;
import static com.example.han.boostcamp_walktogether.util.StringKeys.USER_PROFILE;

/**
 * Created by Han on 2017-08-15.
 */

public class WalkDiaryActivity extends DrawerBaseActivity implements OnMapReadyCallback{

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    private static final int DEFAULT_ZOOM = 13;
    private Button mStartWalkingButton, mStopWalkingButton;
    private LocationUpdateService mLocationUpdateService;
    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    private Context mContext;
    private String userEmail;
    private android.app.FragmentManager mFragmentManager;
    private FragmentManager mSupportFragmentManager;


    private RecyclerView mDiaryRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private WalkDiaryAdapter mWalkDiaryAdapter;

    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;
    private GoogleApiClient mGoogleApiClient;
    private OnMapReadyCallback onMapReadyCallback;

   // private WalkDiaryAddDialog walkDiaryAddDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_walk_diary, mFrameLayout, false);
        mDrawerLayout.addView(contentView, 0);

        mContext = this;
        onMapReadyCallback = this;
        mSupportFragmentManager = getSupportFragmentManager();
        mFragmentManager = getFragmentManager();

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
        SharedPreferenceUtil.setUserProfileSharedPreference(mContext,USER_PROFILE,MODE_PRIVATE);
        userEmail = SharedPreferenceUtil.getUserProfile(USER_EMAIL);
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
                            mSupportFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.walk_diary_frame, mMapFragment);
                    fragmentTransaction.commit();
                    mMapFragment.getMapAsync(onMapReadyCallback);

                    WalkDiaryInfoInMapDialog walkDiaryInfoInMapDialog = WalkDiaryInfoInMapDialog.newInstance();
                    walkDiaryInfoInMapDialog.show(mFragmentManager, StringKeys.WALK_DIARY_INFO_DIALOG);

                    break;

                case R.id.walk_stop_button :

                    mMap.moveCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM));
                    mMap.snapshot(snapshotReadyCallback);
                    android.support.v4.app.FragmentTransaction fragmentTransactionStop =
                            mSupportFragmentManager.beginTransaction();
                    fragmentTransactionStop.remove(mMapFragment);
                    fragmentTransactionStop.commit();
                    //mMap.snapshot(snapshotReadyCallback);

                    mLocationUpdateService.stopLocationUpdates();
                    mDiaryRecyclerView.setVisibility(View.VISIBLE);
                    mStartWalkingButton.setVisibility(View.VISIBLE);
                    mStopWalkingButton.setVisibility(View.GONE);
            }


        }
    };

    @Override
    public void onBackPressed() {
        if(mDiaryRecyclerView.isShown()){
            super.onBackPressed();
        }
        else{

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getDeviceLocation();
        mLocationUpdateService.setmMap(mMap);
        mLocationUpdateService.createLocationRequest();
        mLocationUpdateService.startLocationUpdates();
        //mMap.snapshot(snapshotReadyCallback);
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

    GoogleMap.SnapshotReadyCallback snapshotReadyCallback = new GoogleMap.SnapshotReadyCallback() {
        Bitmap bitmap;

        @Override
        public void onSnapshotReady(Bitmap snapshot) {
            // TODO Auto-generated method stub
            bitmap = snapshot;
            try {
                showProgressBar();
                File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator
                        +"walktogether");

                boolean success;
                if(!folder.exists()){

                    success =folder.mkdir();

                    if(success){
                        Log.d("DirectoryCreate","yes");
                    }else{
                        Log.d("DirectoryCreate","no");
                    }
                }/*
                Log.d("FilePath",Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator
                        +"walktogether/");*/
               /* Log.d("FilePath",Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator
                        );*/
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator
                        + "walktogether/"
                        + "my_walk_"
                        + System.currentTimeMillis()
                        + ".png");

                Log.d("FileString",file.toString());
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.close();
                Uri imageUri = Uri.parse("file://"+file);

                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imageUri));


                // 일지등록 다이얼로그 생성하고 띄워준다.
                WalkDiaryAddDialog walkDiaryAddDialog = WalkDiaryAddDialog.newInstance(imageUri,userEmail);
                walkDiaryAddDialog.show(mFragmentManager,StringKeys.WALK_DIARY_ADD_DIALOG);

                hideProgressBar();
                Log.d("SuccessImageStore","yes");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

}
