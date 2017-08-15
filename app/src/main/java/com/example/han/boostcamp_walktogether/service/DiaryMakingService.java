package com.example.han.boostcamp_walktogether.service;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

/**
 * Created by Han on 2017-08-15.
 */

public class DiaryMakingService extends IntentService {
    private final IBinder mBinder = new LocalBinder();
    private ArrayList<Location> mArrayListLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private Context mContext;

    public DiaryMakingService(String name) {
        super(name);
    }

    public DiaryMakingService(Context context) {
        super("DiaryMakingService");
        mContext = context;
    }

    public DiaryMakingService(){
        super("DiaryMakingService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mArrayListLocation = new ArrayList<>();
        createLocationRequest();
        startLocationUpdates();
        Log.d("onCreateService","yes");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        /*createLocationRequest();
        startLocationUpdates();*/
        Log.d("onHandleIntent","yes");

    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                null /* Looper */);
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

   LocationCallback mLocationCallback = new LocationCallback(){

       @Override
       public void onLocationResult(LocationResult locationResult) {
           //super.onLocationResult(locationResult);
           mArrayListLocation.add(locationResult.getLastLocation());

       }
   };

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        stopLocationUpdates();
        Log.d("UnBindSuccess","yes");
        return super.onUnbind(intent);

    }

    public class LocalBinder extends Binder {
       public DiaryMakingService getService() {
            // Return this instance of LocalService so clients can call public methods
            return DiaryMakingService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("BindSuccess","yes");
        return mBinder;
    }

    public ArrayList<Location> getmArrayListLocation(){
       // Log.d("ArrayListInService",mArrayListLocation.get(0).getLatitude() + "");
        return  mArrayListLocation;
    }
}
