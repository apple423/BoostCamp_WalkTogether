package com.example.han.boostcamp_walktogether.view;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.han.boostcamp_walktogether.ActionBar.DrawerBaseActivity;
import com.example.han.boostcamp_walktogether.Adapters.LocationListAdapter;
import com.example.han.boostcamp_walktogether.R;
import com.example.han.boostcamp_walktogether.data.CommentAveragePointDTO;
import com.example.han.boostcamp_walktogether.data.ParkRowDTO;
import com.example.han.boostcamp_walktogether.interfaces.OnClickLocationListInterface;
import com.example.han.boostcamp_walktogether.util.ComparatorUtil;
import com.example.han.boostcamp_walktogether.util.RetrofitUtil;
import com.example.han.boostcamp_walktogether.util.StringKeys;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Headers;
import okhttp3.internal.http2.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Han on 2017-08-12.
 */

public class LocationListActivity extends DrawerBaseActivity implements
        GoogleApiClient.ConnectionCallbacks,
        OnClickLocationListInterface
{

    private static final String TAG = LocationListActivity.class.getSimpleName();
    private Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastKnownLocation;
    private boolean mLocationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private RetrofitUtil retrofitUtil = RetrofitUtil.retrofit.create(RetrofitUtil.class);
    private ArrayList<ParkRowDTO> mParkRowDTOArrayList;
    private ArrayList<CommentAveragePointDTO> mAveragePointDTOArrayList;
    private LinearLayoutManager linearLayoutManager;
    private LocationListAdapter locationListAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_location_list, mFrameLayout, false);
        mDrawerLayout.addView(contentView, 0);

        mContext = this;
        mAveragePointDTOArrayList = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.location_list_recyclerView);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        locationListAdapter = new LocationListAdapter(this, this);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(locationListAdapter);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .build();
        mGoogleApiClient.connect();

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

            Log.d("my Location lat list",Double.toString(mLastKnownLocation.getLatitude()));
            Log.d("my Location lng list",Double.toString(mLastKnownLocation.getLongitude()));


            // RetrofitUtil retrofitUtil = RetrofitUtil.retrofit.create(RetrofitUtil.class);

            // 현재 위치를 기반으로 서버에 주변 공원정보들을 요청한다.
            Call<ArrayList<ParkRowDTO>> parkRowDTOListCall =
                    retrofitUtil.getNearestPark(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude());
            parkRowDTOListCall.enqueue(parkRowDataListCallback);


        } else {
            Log.d("CurrentLocation is null", "Current location is null. Using defaults.");
        }
    }

    Callback<ArrayList<ParkRowDTO>> parkRowDataListCallback = new Callback<ArrayList<ParkRowDTO>>() {
        @Override
        public void onResponse(Call<ArrayList<ParkRowDTO>> call, Response<ArrayList<ParkRowDTO>> response) {
            if(response.isSuccessful()){

                // 정보를 받아서 리스트에 저장
                ArrayList<ParkRowDTO> parkRowDTOList = response.body();
                mParkRowDTOArrayList = parkRowDTOList;
                // Log.d("checkList",parkRowDTOList.size());
                //Log.d("RowSize",parkRowDTOList.size() + "man");
                for(ParkRowDTO parkRowData : parkRowDTOList){
                    /*LatLng latLng = new LatLng(parkRowData.getLatitude(),parkRowData.getLongitude());
                    Log.d("checklocation1km", String.valueOf(parkRowData.getLatitude()) + parkRowData.getLongitude());*/
                    int park_key = parkRowData.getPark_key();


                    Call<CommentAveragePointDTO> commentAvaragePointDTOListCall = retrofitUtil.getAveragePoint(park_key);
                    commentAvaragePointDTOListCall.enqueue(commentAveragePointDTOCallback);

                }

            }else{
                Log.d("ErroringetNearest",response.errorBody().toString());
            }
        }

        @Override
        public void onFailure(Call<ArrayList<ParkRowDTO>> call, Throwable t) {

        }
    };


    Callback<CommentAveragePointDTO> commentAveragePointDTOCallback = new Callback<CommentAveragePointDTO>() {
        @Override
        public void onResponse(Call<CommentAveragePointDTO> call, Response<CommentAveragePointDTO> response) {
            if(response.isSuccessful()){
                CommentAveragePointDTO commentAveragePointDTO = response.body();


                mAveragePointDTOArrayList.add(commentAveragePointDTO);


                // 공원 정보와 공원의 평가점수 리스트가 같을시 어뎁터에 보낸다.
                if(mParkRowDTOArrayList.size() == mAveragePointDTOArrayList.size()){

                    Collections.sort(mAveragePointDTOArrayList,ComparatorUtil.commentAveragePointDTOComparator);
                    locationListAdapter.setDataArrayList(mParkRowDTOArrayList,mAveragePointDTOArrayList);


                }

            }

        }

        @Override
        public void onFailure(Call<CommentAveragePointDTO> call, Throwable t) {

        }
    };

    @Override
    public void onConnected(Bundle connectionHint) {
        // Build the map.
        getDeviceLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Play services connection suspended");
    }

    @Override
    public void onClickList(int position) {
        Intent intent = new Intent(mContext,LocationActivity.class);
        Parcelable parcelableParkRowData = Parcels.wrap(mParkRowDTOArrayList.get(position));
        intent.putExtra(StringKeys.LOCATION_INTENT_KEY,parcelableParkRowData);
        startActivity(intent);

    }
}
