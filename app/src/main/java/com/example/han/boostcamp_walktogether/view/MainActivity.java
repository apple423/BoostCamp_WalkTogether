package com.example.han.boostcamp_walktogether.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.han.boostcamp_walktogether.ActionBar.DrawerBaseActivity;
import com.example.han.boostcamp_walktogether.Adapters.LocationRecentCommentAdapter;
import com.example.han.boostcamp_walktogether.R;
import com.example.han.boostcamp_walktogether.data.RecentCommentDTO;
import com.example.han.boostcamp_walktogether.helper.FirebaseHelper;
import com.example.han.boostcamp_walktogether.interfaces.OnClickRecentCommentInterface;
import com.example.han.boostcamp_walktogether.util.RetrofitUtil;
import com.example.han.boostcamp_walktogether.util.SharedPreferenceUtil;
import com.example.han.boostcamp_walktogether.util.StringKeys;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.crash.FirebaseCrash;
import com.kakao.auth.Session;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * Created by Han on 2017-08-12.
 */

public class MainActivity extends DrawerBaseActivity implements View.OnClickListener, OnClickRecentCommentInterface{

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 111;
    private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION =3;
    private CardView mCardViewMap;
    private CardView mCardViewList;
    private Button mButtonSearchDetail, mButtonWalkDiary;
    private Context mContext;
    private RecyclerView mRecentCommentRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private LocationRecentCommentAdapter locationRecentCommentAdapter;
    private ArrayList<RecentCommentDTO> mRecentCommentDTOArrayList;
    private ImageView mRefreshComment;
    private RetrofitUtil retrofitUtil = RetrofitUtil.retrofit.create(RetrofitUtil.class);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_main, mFrameLayout, false);
        mDrawerLayout.addView(contentView, 0);
        mContext = this;

        mCardViewMap = (CardView)findViewById(R.id.cardView_map_search);
        mCardViewList = (CardView)findViewById(R.id.cardView_list_search);
        mButtonSearchDetail = (Button)findViewById(R.id.search_detail);
        mRecentCommentRecyclerView = (RecyclerView)findViewById(R.id.recent_review_recyclerView);
        mButtonSearchDetail = (Button)findViewById(R.id.search_detail);
        mButtonWalkDiary = (Button)findViewById(R.id.walk_diary_button);
        mRefreshComment = (ImageView)findViewById(R.id.recent_review_refresh_imageView);

        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        locationRecentCommentAdapter = new LocationRecentCommentAdapter(this,this);
        mRecentCommentRecyclerView.setLayoutManager(linearLayoutManager);
        mRecentCommentRecyclerView.setAdapter(locationRecentCommentAdapter);



        mButtonSearchDetail.setOnClickListener(this);
        mCardViewMap.setOnClickListener(this);
        mCardViewList.setOnClickListener(this);
        mButtonWalkDiary.setOnClickListener(this);
        mRefreshComment.setOnClickListener(this);
        //FirebaseCrash.report(new Exception("My first Android non-fatal error"));

        getUserProfileAndSetHeader();

        Call<ArrayList<RecentCommentDTO>> recentCommentDTOCall = retrofitUtil.getRecentComment();
        recentCommentDTOCall.enqueue(recentCommentDTOCallback);
        writePermissionCheckAndRequest();
        locationPermissionCheckAndRequest();

    }


    @Override
    public void onBackPressed() {

        // 네비게이션 드로워가 열려 있을때
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){

            mDrawerLayout.closeDrawer(Gravity.LEFT);

        }
        else {
            AlertDialog.Builder alterDialogBuilder = new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.finish_prefer))
                    .setPositiveButton(getResources().getString(R.string.finish_yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.finish_no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = alterDialogBuilder.create();
            alertDialog.show();

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_sign_out:
                AlertDialog.Builder alterDialogBuilder = new AlertDialog.Builder(this)
                        .setTitle(getResources().getString(R.string.logout_prefer))
                        .setPositiveButton(getResources().getString(R.string.logout_yes), onClickSignOutYes)
                        .setNegativeButton(getResources().getString(R.string.logout_no), onClickSignOutNo);

                AlertDialog alertDialog = alterDialogBuilder.create();
                alertDialog.show();


        }


        return super.onOptionsItemSelected(item);

    }
    // 로그아웃 확인을 눌렀을 시
    DialogInterface.OnClickListener onClickSignOutYes = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            // 페이스북과 파이어베이스의 경우
            if (FirebaseHelper.signInState() != null) {
                FirebaseHelper.signOut();
                redirectLoginActivity();
            }
            // 카카오톡의 경우
            else if (Session.getCurrentSession().isOpened()) {

                UserManagement.requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        redirectLoginActivity();
                    }


                });
            }

        }
    };

    DialogInterface.OnClickListener onClickSignOutNo = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    };

    public void redirectLoginActivity() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.cardView_map_search :
                Intent intent = new Intent(mContext, MapActivity.class);
                mContext.startActivity(intent);
                break;

            case R.id.cardView_list_search :
                Intent intentLocationList = new Intent(mContext, LocationListActivity.class);
                mContext.startActivity(intentLocationList);
                break;

            case R.id.search_detail :


                try {
                    Intent intentDetail =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(this);
                    startActivityForResult(intentDetail, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
                break;

            case R.id.walk_diary_button :

                Intent intentWalkDiary = new Intent(mContext,WalkDiaryActivity.class);
                startActivity(intentWalkDiary);
                break;


            case R.id.recent_review_refresh_imageView :

                Call<ArrayList<RecentCommentDTO>> recentCommentCall = retrofitUtil.getRecentComment();
                recentCommentCall.enqueue(recentCommentDTOCallback);

                break;
        }


    }

    Callback<ArrayList<RecentCommentDTO>> recentCommentDTOCallback = new Callback<ArrayList<RecentCommentDTO>>() {
        @Override
        public void onResponse(Call<ArrayList<RecentCommentDTO>> call, Response<ArrayList<RecentCommentDTO>> response) {
            if(response.isSuccessful()){

                ArrayList<RecentCommentDTO> recentCommentDTOArrayList = response.body();
                mRecentCommentDTOArrayList = recentCommentDTOArrayList;
                locationRecentCommentAdapter.setCommentDTOArrayList(recentCommentDTOArrayList);

            }


        }

        @Override
        public void onFailure(Call<ArrayList<RecentCommentDTO>> call, Throwable t) {

        }
    };

    // autocompletePlace로 부터 결과를 얻어 온 후 맵 엑티비티로 전환
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                double latitude = place.getLatLng().latitude;
                double longtitude = place.getLatLng().longitude;
                Log.d("latitudeSearch",latitude +"");
                Log.d("longitudeSearch",longtitude +"");
                Intent intent = new Intent(mContext,MapActivity.class);
                intent.putExtra(StringKeys.SEARCH_LATITUDE,latitude);
                intent.putExtra(StringKeys.SEARCH_LONGITUDE,longtitude);
                mContext.startActivity(intent);

                //Log.i(TAG, "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
               // Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled operation.
            }
        }
    }

    public void writePermissionCheckAndRequest(){

        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions((Activity) mContext,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    public void locationPermissionCheckAndRequest(){

        if (ContextCompat.checkSelfPermission(mContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions((Activity) mContext,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    // 최근 장소리뷰를 눌렀을시 해당 장소 리뷰 액티비티로 전환
    @Override
    public void onClickRecentComment(int position) {
        Intent intent = new Intent(mContext,LocationCommentActivity.class);
        intent.putExtra(StringKeys.LOCATION_ID_KEY,mRecentCommentDTOArrayList.get(position).getPark_key());
        intent.putExtra(StringKeys.LOCATION_NAME,mRecentCommentDTOArrayList.get(position).getName());
        startActivity(intent);

    }
}
