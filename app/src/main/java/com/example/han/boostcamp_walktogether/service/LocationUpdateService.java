package com.example.han.boostcamp_walktogether.service;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Han on 2017-08-15.
 */

public class LocationUpdateService {
    private static final int DEFAULT_ZOOM = 12;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private ArrayList<Location> mArrayListLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private Context mContext;
    private boolean mLocationPermissionGranted;
    private GoogleMap mMap;
    private PolylineOptions mPolylineOptions;
    private ArrayList<Polyline> mPolylineArrayList;
    private ArrayList<LatLng> mLatLngArrayList;
    private boolean distanceFlag;
    private Location mPrevLocation, mNewLocation;
    private float mDistance;

    public void setmMap(GoogleMap mMap) {
        this.mMap = mMap;
    }

    public LocationUpdateService(Context context){

        mContext = context;
        mDistance = 0;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mArrayListLocation = new ArrayList<>();
        mPolylineArrayList = new ArrayList<>();
        //mPolylineOptions = new PolylineOptions();


    }
    // 권한체크 후 지속적으로 현재 위치를 알아 오는 콜백등록
    public void startLocationUpdates() {
        mPolylineOptions = new PolylineOptions();
        if (ContextCompat.checkSelfPermission(mContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions((Activity) mContext,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if(mLocationPermissionGranted) {
            //resetPolyLine();

            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback,
                    null /* Looper */);
        }
    }

    public void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2000);  // 2초에 한번
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    LocationCallback mLocationCallback = new LocationCallback(){

        @Override
        public void onLocationResult(LocationResult locationResult) {

            mArrayListLocation.add(locationResult.getLastLocation());
            if(distanceFlag){

                mPrevLocation = locationResult.getLastLocation();
                distanceFlag = false;
            }
            else{

                mNewLocation = locationResult.getLastLocation();
                distanceFlag = true;
            }

            if(mPrevLocation !=null && mNewLocation !=null){

                if(distanceFlag){
                    mDistance += mPrevLocation.distanceTo(mNewLocation);
                }
                else{
                    mDistance += mNewLocation.distanceTo(mPrevLocation);
                }

            }

            LatLng latLng = new LatLng(locationResult.getLastLocation().getLatitude(),
                    locationResult.getLastLocation().getLongitude());
            mPolylineOptions.add(latLng);
            mPolylineOptions.color(Color.RED);
            mPolylineOptions.width(15);
            mPolylineArrayList.add(mMap.addPolyline(mPolylineOptions));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        }
    };

    public void stopLocationUpdates() {
        Log.d("stopLocationUpdates","yes");

        /*int arrayListHalfPoint = mPolylineArrayList.size()/2;
        Polyline polyline = mPolylineArrayList.get(arrayListHalfPoint);
        List<LatLng> latLngs = polyline.getPoints();
        LatLng latLng = latLngs.get(0);*/

        mDistance = 0;
        resetPolyLine(); //폴리라인제거
        if(mMap !=null){
            mMap.clear(); // 지도 초기화
        }

       mFusedLocationClient.removeLocationUpdates(mLocationCallback); //콜백해제

    }

    public void resetPolyLine(){


        for(Polyline polyline : mPolylineArrayList){
            Log.d("removePolyline",polyline.toString());
            polyline.remove();
        }
        mPolylineArrayList.clear();

    }

    public ArrayList<Location> getmArrayListLocation(){
        Log.d("ArrayListInService",mArrayListLocation.get(0).getLatitude() + "");
        return  mArrayListLocation;
    }

    public float getDistance(){

        return mDistance;
    }

    public LatLng getMiddleLatLng(){
        int arrayListHalfPoint = mArrayListLocation.size()/2;
        Location location = mArrayListLocation.get(arrayListHalfPoint);
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        return latLng;

    }


}

