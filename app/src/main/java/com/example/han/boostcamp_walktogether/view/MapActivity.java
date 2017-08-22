package com.example.han.boostcamp_walktogether.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.han.boostcamp_walktogether.ActionBar.DrawerBaseActivity;
import com.example.han.boostcamp_walktogether.R;
import com.example.han.boostcamp_walktogether.data.ParkRowDTO;
import com.example.han.boostcamp_walktogether.helper.FirebaseHelper;
import com.example.han.boostcamp_walktogether.util.RetrofitUtil;
import com.example.han.boostcamp_walktogether.util.StringKeys;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kakao.auth.Session;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Han on 2017-07-25.
 */

public class MapActivity extends DrawerBaseActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerClickListener
{
    private static final String TAG = MapActivity.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private final LatLng mDefaultLocation = new LatLng(37.405666, 127.114268);
    private static final int DEFAULT_ZOOM = 15;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private RetrofitUtil retrofitUtil = RetrofitUtil.retrofit.create(RetrofitUtil.class);
    private GoogleMap mMap;
    private Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastKnownLocation;
    private ArrayList<Marker> mLocationMarkerArrayList;
    private ArrayList<ParkRowDTO> mLocationRowDTOArrayList;
    private boolean mLocationPermissionGranted;
    private CameraPosition mCameraPosition;
    // 처음 맵을 불렀을때만 현재 위치로 이동하고 다시 부를땐 그 자리에 있도록 하기 위함
    private boolean isFisrtMapLoad = true;
    private Button mLocationNextButton;
    private TextView mLocationTitleTextView;
    private ImageButton mMyLocationButton;
    private Intent mIntent;
    private double searchLatitude;
    private double searchLongtitude;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_map, mFrameLayout, false);
        mDrawerLayout.addView(contentView, 0);

        mLocationMarkerArrayList = new ArrayList<>();
        mLocationRowDTOArrayList = new ArrayList<>();
        mIntent = new Intent(this, LocationActivity.class);
        mContext = this;

        mLocationNextButton = (Button)findViewById(R.id.map_next_button);
        mLocationTitleTextView = (TextView)findViewById(R.id.map_title_textView);
        mMyLocationButton = (ImageButton)findViewById(R.id.map_current_location);

        mLocationNextButton.setOnClickListener(onClickNextButton);
        mMyLocationButton.setOnClickListener(onClickMyLocationButton);
        mLocationNextButton.setEnabled(false);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,
                        this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();


        searchLatitude = getIntent().getDoubleExtra(StringKeys.SEARCH_LATITUDE,0);
        searchLongtitude = getIntent().getDoubleExtra(StringKeys.SEARCH_LONGITUDE,0);
        Log.d("latitudeSearchInMap",searchLatitude +"");
        Log.d("longitudeSearchInMap",searchLongtitude +"");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {

        Log.d(TAG, "Play services connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.d(TAG, "Play services connection suspended");
    }

    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener(this);


        Log.d("PermissionGranted Ready", Boolean.toString(mLocationPermissionGranted));

        //getDeviceLocation();

        if(isFisrtMapLoad && searchLatitude==0 && searchLongtitude ==0) {
            getDeviceLocation(); // 현재 위치 알아오고 이동
            isFisrtMapLoad = false;

        }

        else if(isFisrtMapLoad && searchLatitude!=0 && searchLongtitude!=0){
            getCurrentLocation(searchLatitude,searchLongtitude);
            // 인텐트로 넘어온 해당 좌표로 이동
            isFisrtMapLoad = false;

        }


    }


    View.OnClickListener onClickNextButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            startActivity(mIntent);
        }
    };

    View.OnClickListener onClickMyLocationButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

           getDeviceLocation();

        }
    };

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


    // 현재 위치를 알아오고 카메라를 이동하고 주변 장소를 가져오는 함수
    private void getDeviceLocation() {

        for(Marker marker : mLocationMarkerArrayList){
            marker.remove();
        }
        mLocationMarkerArrayList.clear();

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


        if (mCameraPosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else if (mLastKnownLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
            Log.d("my Location lat",Double.toString(mLastKnownLocation.getLatitude()));
            Log.d("my Location lng",Double.toString(mLastKnownLocation.getLongitude()));


            showProgressBar();
            // 현재 위치를 기반으로 서버에 주변 공원정보들을 요청한다.
            Call<ArrayList<ParkRowDTO>> parkRowDTOListCall =
            retrofitUtil.getNearestPark(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude());
            parkRowDTOListCall.enqueue(parkRowDataListCallback);


        } else {
            Log.d("CurrentLocation is null", "Current location is null. Using defaults.");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    public void getCurrentLocation(double latitude, double longitude){

        for(Marker marker : mLocationMarkerArrayList){
            marker.remove();
        }
        mLocationMarkerArrayList.clear();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(latitude, longitude), DEFAULT_ZOOM));
        Call<ArrayList<ParkRowDTO>> parkRowDTOListCall =
                retrofitUtil.getNearestPark(latitude,longitude);
        parkRowDTOListCall.enqueue(parkRowDataListCallback);


    }

    // Location Permission 퍼미션을 얻은 후에
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        //updateLocationUI();
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

    public void redirectLoginActivity() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }

    DialogInterface.OnClickListener onClickSignOutNo = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    };


    // 마커 선택 후에 title을 클릭시
    @Override
    public void onInfoWindowClick(Marker marker) {

        ParkRowDTO data = (ParkRowDTO) marker.getTag();
        Parcels.wrap(data);
        Intent intent = new Intent(this, LocationActivity.class);
        intent.putExtra(StringKeys.LOCATION_INTENT_KEY,Parcels.wrap(data));
        startActivity(intent);

    }
    // 주변 정보를 얻어 온 후에 처리를 위한 콜백 리스너
    Callback<ArrayList<ParkRowDTO>> parkRowDataListCallback = new Callback<ArrayList<ParkRowDTO>>() {
        @Override
        public void onResponse(Call<ArrayList<ParkRowDTO>> call, Response<ArrayList<ParkRowDTO>> response) {
            if(response.isSuccessful()){

                // 정보를 받아서 리스트에 저장
                Log.d("ParkRowStatus",response.code() +"");
                if(response.code() != 500) {
                    ArrayList<ParkRowDTO> parkRowDTOList = response.body();
                    mLocationRowDTOArrayList = parkRowDTOList;
                    // Log.d("checkList",parkRowDTOList.size());
                    for (ParkRowDTO parkRowData : parkRowDTOList) {
                        LatLng latLng = new LatLng(parkRowData.getLatitude(), parkRowData.getLongitude());
                        Log.d("checklocation1km", String.valueOf(parkRowData.getLatitude()) + parkRowData.getLongitude());
                        // 맵에 마커를 추가하고 마커에 해당 공원정보에 대한 객체를 넣어준다.
                        Marker marker = mMap.addMarker(new MarkerOptions().title(parkRowData.getName()).position(latLng));

                        marker.setTag(parkRowData);


                        mLocationMarkerArrayList.add(marker);

                    }
                }

                hideProgressBar();
            }else{

                Toast toast = Toast.makeText(mContext,getResources().getString(R.string.no_location),Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.NO_GRAVITY,0,0);
                toast.show();

            }
        }

        @Override
        public void onFailure(Call<ArrayList<ParkRowDTO>> call, Throwable t) {

            Log.d("ParkRowStatus",t.getMessage());


        }
    };


    @Override
    public boolean onMarkerClick(Marker marker) {
        mLocationNextButton.setEnabled(true);
        ParkRowDTO data = (ParkRowDTO) marker.getTag();
        mLocationTitleTextView.setText(data.getName());
        Parcels.wrap(data);
        //Intent intent = new Intent(this, LocationActivity.class);
        mIntent.putExtra(StringKeys.LOCATION_INTENT_KEY,Parcels.wrap(data));
        //startActivity(intent);
        return false;
    }
}
