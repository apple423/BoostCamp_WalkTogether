package com.example.han.boostcamp_walktogether.view;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.example.han.boostcamp_walktogether.ActionBar.DrawerBaseActivity;
import com.example.han.boostcamp_walktogether.Adapters.WalkDiaryAdapter;
import com.example.han.boostcamp_walktogether.service.LocationUpdateService;
import com.example.han.boostcamp_walktogether.R;
import com.example.han.boostcamp_walktogether.data.WalkDiaryDTO;
import com.example.han.boostcamp_walktogether.data.WalkDiaryImageDTO;
import com.example.han.boostcamp_walktogether.interfaces.OnClickWalkDiaryButtonInterface;
import com.example.han.boostcamp_walktogether.interfaces.OnClickWalkDiaryDeleteInterface;
import com.example.han.boostcamp_walktogether.util.RetrofitUtil;
import com.example.han.boostcamp_walktogether.util.SharedPreferenceUtil;
import com.example.han.boostcamp_walktogether.util.StringKeys;
import com.example.han.boostcamp_walktogether.widget.NoDataFragment;
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
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.SocialObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.network.response.ResponseBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import me.wangyuwei.flipshare.FlipShareView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.han.boostcamp_walktogether.util.StringKeys.USER_EMAIL;
import static com.example.han.boostcamp_walktogether.util.StringKeys.USER_PROFILE;

/**
 * Created by Han on 2017-08-15.
 */

public class WalkDiaryActivity extends DrawerBaseActivity implements OnMapReadyCallback, OnClickWalkDiaryButtonInterface,
        OnClickWalkDiaryDeleteInterface{

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    private static final int DEFAULT_ZOOM = 14;
    private static final int SNAPSHOT_ZOOM = 12;
    private Button mStartWalkingButton, mStopWalkingButton;
    private LocationUpdateService mLocationUpdateService;
    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    private Context mContext;
    private String userEmail;
    private android.app.FragmentManager mFragmentManager;
    private FragmentManager mSupportFragmentManager;
    private long startMiliSec, endMiliSec, mLongWalkTime;
    private float mWalkDistance;
    private int mDiary_key;


    private RecyclerView mDiaryRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private WalkDiaryAdapter mWalkDiaryAdapter;

    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation,mStartLocation,mEndLocation;
    private GoogleApiClient mGoogleApiClient;
    private OnMapReadyCallback onMapReadyCallback;
    private NoDataFragment mNoDataFragment;

   // private WalkDiaryAddDialog walkDiaryAddDialog;
    private ArrayList<WalkDiaryDTO> mWalkDiaryDTOArrayList;
    private ArrayList<WalkDiaryImageDTO> mWalkDiaryImageDTOArrayList;
    private final RetrofitUtil mRetrofitUtil = RetrofitUtil.retrofit.create(RetrofitUtil.class);



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
        mWalkDiaryAdapter = new WalkDiaryAdapter(this,getResources(),this);
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
        mNoDataFragment = NoDataFragment.newInstance();
        SharedPreferenceUtil.setUserProfileSharedPreference(mContext,USER_PROFILE,MODE_PRIVATE);
        userEmail = SharedPreferenceUtil.getUserProfile(USER_EMAIL);
        //mMapFragment.getMapAsync(onMapReadyCallback);

        getWalkDiaryData();

    }

    private void getWalkDiaryData() {
        showProgressBar();
        Call<ArrayList<WalkDiaryDTO>> getWalkDiaryCall = mRetrofitUtil.getUsersWalkDiary(userEmail);
        getWalkDiaryCall.enqueue(getWalkDiaryCallback);
    }

    View.OnClickListener onClickButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();


            switch (id){

                case R.id.walk_start_button :
                    startMiliSec = System.currentTimeMillis();
                    try{
                        mStartLocation =LocationServices.FusedLocationApi
                                .getLastLocation(mGoogleApiClient);

                    }catch (SecurityException e){

                        Log.e("GetCurrentLocationFail",e.getMessage());
                    }


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
                    endMiliSec = System.currentTimeMillis();
                    try{
                        mEndLocation =LocationServices.FusedLocationApi
                                .getLastLocation(mGoogleApiClient);

                    }catch (SecurityException e){

                        Log.e("GetCurrentLocationFail",e.getMessage());
                    }

                    mLongWalkTime = endMiliSec - startMiliSec;
                    LatLng latLng = mLocationUpdateService.getMiddleLatLng();
                    mWalkDistance = mLocationUpdateService.getDistance();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,DEFAULT_ZOOM));
                    mMap.snapshot(snapshotReadyCallback);
                    android.support.v4.app.FragmentTransaction fragmentTransactionStop =
                            mSupportFragmentManager.beginTransaction();
                    fragmentTransactionStop.remove(mMapFragment);
                    fragmentTransactionStop.commit();


                    //mLocationUpdateService.stopLocationUpdates();
                    mDiaryRecyclerView.setVisibility(View.VISIBLE);
                    mStartWalkingButton.setVisibility(View.VISIBLE);
                    mStopWalkingButton.setVisibility(View.GONE);
                    showProgressBar();

            }

        }
    };

    @Override
    public void onBackPressed() {
        if(mDiaryRecyclerView.isShown() || mNoDataFragment.isVisible()){
            super.onBackPressed();
        }
        else {

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
                //showProgressBar();
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

                //미디어 스캔
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imageUri));

                hideProgressBar();
                // 일지등록 다이얼로그 생성하고 띄워준다.
                WalkDiaryAddDialog walkDiaryAddDialog = WalkDiaryAddDialog.newInstance(imageUri,userEmail,mLongWalkTime,mWalkDistance);
                walkDiaryAddDialog.show(mFragmentManager,StringKeys.WALK_DIARY_ADD_DIALOG);
                mLocationUpdateService.stopLocationUpdates();
               // hideProgressBar();
                Log.d("SuccessImageStore","yes");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    Callback<ArrayList<WalkDiaryDTO>> getWalkDiaryCallback = new Callback<ArrayList<WalkDiaryDTO>>() {
        @Override
        public void onResponse(Call<ArrayList<WalkDiaryDTO>> call, Response<ArrayList<WalkDiaryDTO>> response) {
            if(response.isSuccessful()){
                if(response.body().get(0).getStatusCode()==503){
                    noDataFragmentScreen();
                }
                else {
                    mWalkDiaryDTOArrayList = response.body();
                    Call<ArrayList<WalkDiaryImageDTO>> getWalkDiaryImageCall = mRetrofitUtil.getUserWalkDiaryImages(userEmail);
                    getWalkDiaryImageCall.enqueue(getWalkDiaryImageCallback);
                    //noDataFragmentScreen();
                }

            }
            else{
                hideProgressBar();
               noDataFragmentScreen();
            }
        }

        @Override
        public void onFailure(Call<ArrayList<WalkDiaryDTO>> call, Throwable t) {
            hideProgressBar();
            noDataFragmentScreen();
        }
    };


    Callback<ArrayList<WalkDiaryImageDTO>> getWalkDiaryImageCallback  = new Callback<ArrayList<WalkDiaryImageDTO>>() {
        @Override
        public void onResponse(Call<ArrayList<WalkDiaryImageDTO>> call, Response<ArrayList<WalkDiaryImageDTO>> response) {
            if(response.isSuccessful()){

                hideProgressBar();
                mWalkDiaryImageDTOArrayList = response.body();
                mWalkDiaryAdapter.setWalkDiaryDTOArrayLists(mWalkDiaryDTOArrayList,mWalkDiaryImageDTOArrayList);

                noDataFragmentScreen();

            }else{

                noDataFragmentScreen();
            }

        }

        @Override
        public void onFailure(Call<ArrayList<WalkDiaryImageDTO>> call, Throwable t) {
            hideProgressBar();
            noDataFragmentScreen();
        }
    };

    private void noDataFragmentScreen() {
        if(mWalkDiaryAdapter.getItemCount()==0){

            mDiaryRecyclerView.setVisibility(View.GONE);

            android.support.v4.app.FragmentTransaction fragmentTransactionReplaceNoData =
                    mSupportFragmentManager.beginTransaction();
            fragmentTransactionReplaceNoData.replace(R.id.walk_diary_frame, mNoDataFragment);
            fragmentTransactionReplaceNoData.commit();
        }
        else{
            mDiaryRecyclerView.setVisibility(View.VISIBLE);
            android.support.v4.app.FragmentTransaction fragmentTransactionRemoveNodata =
                    mSupportFragmentManager.beginTransaction();
            fragmentTransactionRemoveNodata.remove(mNoDataFragment);
            fragmentTransactionRemoveNodata.commit();

        }
    }

    @Override
    public void onClickAddDiaryButton(ArrayList<WalkDiaryDTO> walkDiaryDTOs, ArrayList<WalkDiaryImageDTO> walkDiaryImageDTOs) {

        mWalkDiaryDTOArrayList = walkDiaryDTOs;
        mWalkDiaryImageDTOArrayList = walkDiaryImageDTOs;
        mWalkDiaryAdapter.setWalkDiaryDTOArrayLists(mWalkDiaryDTOArrayList,mWalkDiaryImageDTOArrayList);

    }



    @Override
    public void onClickShareSMSButton(int position) {



        WalkDiaryDTO walkDiaryDTO = mWalkDiaryDTOArrayList.get(position);
        mDiary_key = walkDiaryDTO.getDiary_key();
        String imageURL = mWalkDiaryImageDTOArrayList.get(position).getImage_url();

        WalkDiaryAdapter.WalkDiaryViewHolder holder = (WalkDiaryAdapter.WalkDiaryViewHolder)
                mDiaryRecyclerView.findViewHolderForAdapterPosition(position);
        ImageView imageView = holder.getmMapMoveImageView();
        Uri imageUri = getLocalBitmapUri(imageView);


        long time = walkDiaryDTO.getDate().getTime() - walkDiaryDTO.getWalk_time();
        Date date = new Date(time);
        String dateString = new SimpleDateFormat(this.getString(R.string.walk_diary_time_title_format)
                , Locale.KOREA)
                .format(date);
        String walkingTime =  mWalkDiaryAdapter.convertSecondsToHMmSs(walkDiaryDTO.getWalk_time());
        String walkingDistance = mWalkDiaryAdapter.covertDistance(walkDiaryDTO.getWalk_distance());

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra("sms_body", dateString + " " + walkingTime + " " + walkingDistance);
        sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        sendIntent.setType("image/*");

        startActivity(Intent.createChooser(sendIntent, "골라"));

    }

    @Override
    public void onClickShareKakaoButton(int position) {

        WalkDiaryDTO walkDiaryDTO = mWalkDiaryDTOArrayList.get(position);
        mDiary_key = walkDiaryDTO.getDiary_key();
        String imageURL = mWalkDiaryImageDTOArrayList.get(position).getImage_url();
        long time = walkDiaryDTO.getDate().getTime() - walkDiaryDTO.getWalk_time();
        Date date = new Date(time);
        String dateString = new SimpleDateFormat(this.getString(R.string.walk_diary_time_title_format)
                , Locale.KOREA)
                .format(date);

        String walkingTime =  mWalkDiaryAdapter.convertSecondsToHMmSs(walkDiaryDTO.getWalk_time());
        String walkingDistance = mWalkDiaryAdapter.covertDistance(walkDiaryDTO.getWalk_distance());

        FeedTemplate params = FeedTemplate
                .newBuilder(ContentObject.newBuilder(dateString,
                       "http://"+imageURL,
                        LinkObject.newBuilder().build())
                        .setDescrption(getResources().getString(R.string.walk_diary_time_walking_time) + " " + walkingTime
                        +"\n" + getResources().getString(R.string.walk_diary_distance) + " " + walkingDistance)

                        .build())
                .addButton(new ButtonObject("앱에서 보기", LinkObject.newBuilder().build()))
                .build();

        KakaoLinkService
                .getInstance().sendDefault(this, params, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {

            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {

            }
        });
    }



    @Override
    public void onClickDeleteButton(int position) {
        mDiary_key = mWalkDiaryImageDTOArrayList.get(position).getDiary_key();
        String imageURL = mWalkDiaryImageDTOArrayList.get(position).getImage_url();
        String [] splitImageURL = imageURL.split("/");
        String imageName = splitImageURL[4];
        mWalkDiaryDTOArrayList.remove(position);
        mWalkDiaryImageDTOArrayList.remove(position);
        mWalkDiaryAdapter.setWalkDiaryDTOArrayLists(mWalkDiaryDTOArrayList,mWalkDiaryImageDTOArrayList);

        if(mWalkDiaryAdapter.getItemCount() == 0){

            mDiaryRecyclerView.setVisibility(View.GONE);


            android.support.v4.app.FragmentTransaction fragmentTransactionReplaceNoDataInDelete =
                    mSupportFragmentManager.beginTransaction();
            fragmentTransactionReplaceNoDataInDelete.replace(R.id.walk_diary_frame, mNoDataFragment);
            fragmentTransactionReplaceNoDataInDelete.commit();

        }

        Call<ResponseBody> deleteWalkDiaryImage = mRetrofitUtil.deleteWalkDiaryImage(mDiary_key,userEmail,imageName);
        deleteWalkDiaryImage.enqueue(deleteWalkDiaryImageCallback);

    }


    Callback<ResponseBody> deleteWalkDiaryImageCallback = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            if(response.isSuccessful()){

                Call<ResponseBody> deleteWalkDiaryCall = mRetrofitUtil.deleteWalkDiary(mDiary_key,userEmail);
                deleteWalkDiaryCall.enqueue(deleteWalkDiaryCallback);


            }

        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {

        }
    };

    Callback<ResponseBody> deleteWalkDiaryCallback = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if(response.isSuccessful()){

                //getWalkDiaryData();
            }


        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {

        }
    };

    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Log.d("getDrawable",drawable.toString());
        Bitmap bmp = null;

        if (drawable instanceof GlideBitmapDrawable){
            bmp = ((GlideBitmapDrawable) imageView.getDrawable()).getBitmap();
            Log.d("getBMP",bmp.toString());
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);

            out.close();
            bmpUri = Uri.fromFile(file);
            Log.d("bmpURI",bmpUri.toString());
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, bmpUri));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
}
