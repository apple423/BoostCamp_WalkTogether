package com.example.han.boostcamp_walktogether.view;

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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.han.boostcamp_walktogether.ActionBar.DrawerBaseActivity;
import com.example.han.boostcamp_walktogether.R;
import com.example.han.boostcamp_walktogether.data.ParkDataFromFirebaseDTO;
import com.example.han.boostcamp_walktogether.helper.FirebaseHelper;
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
import com.kakao.auth.Session;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Created by Han on 2017-07-25.
 */

public class MapActivity extends DrawerBaseActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener {
    private static final String TAG = MapActivity.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private final LatLng mDefaultLocation = new LatLng(37.405666, 127.114268);
    private static final int DEFAULT_ZOOM = 15;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private GoogleMap mMap;
    private Intent mLoginIntent;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastKnownLocation;
    ArrayList<ParkDataFromFirebaseDTO> seoulParkArrayList;
    ArrayList<ParkDataFromFirebaseDTO> sungnamParkArrayList;

    private boolean mLocationPermissionGranted;
    private CameraPosition mCameraPosition;
    private boolean isFisrtMapLoad = true;

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

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,
                        this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();

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

    /**
     * Handles failure to connect to the Google Play services client.
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        // Refer to the reference doc for ConnectionResult to see what error codes might
        // be returned in onConnectionFailed.
        Log.d(TAG, "Play services connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    /**
     * Handles suspension of the connection to the Google Play services client.
     */
    @Override
    public void onConnectionSuspended(int cause) {
        Log.d(TAG, "Play services connection suspended");
    }

    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;


        seoulParkArrayList = FirebaseHelper.getAllSeoulParkDataAndSetMarker(mMap);
        sungnamParkArrayList = FirebaseHelper.getAllSungnamParkDataAndSetMarker(mMap);
        //mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        Log.d("PermissionGranted Ready", Boolean.toString(mLocationPermissionGranted));
        updateLocationUI();

        if(isFisrtMapLoad) {
            getDeviceLocation();
            isFisrtMapLoad = false;
        }

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));


    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
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

                //startActivity(mLoginIntent);

        }

        return super.onOptionsItemSelected(item);


    }


    private void updateLocationUI() {
        if (mMap == null) {

            return;
        }
        Log.d("PermissionGranted in UI", Boolean.toString(mLocationPermissionGranted));
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        Log.d("PermissionGranted CHK", Boolean.toString(mLocationPermissionGranted));
        if (mLocationPermissionGranted) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {

            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mLastKnownLocation = null;
        }
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
            mLastKnownLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
            // Log.d("myLocation",mLastKnownLocation.toString());
        }


        if (mCameraPosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else if (mLastKnownLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
        } else {
            Log.d("CurrentLocation is null", "Current location is null. Using defaults.");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

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
        updateLocationUI();
    }

    DialogInterface.OnClickListener onClickSignOutYes = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (FirebaseHelper.signInState() != null) {
                FirebaseHelper.signOut();
                redirectLoginActivity();
            } else if (Session.getCurrentSession().isOpened()) {

                UserManagement.requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        redirectLoginActivity();
                    }


                });
            }
            //startActivity(mLoginIntent);

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


    @Override
    public void onInfoWindowClick(Marker marker) {

        ParkDataFromFirebaseDTO data = (ParkDataFromFirebaseDTO) marker.getTag();
        Parcels.wrap(data);
        Intent intent = new Intent(this, LocationActivity.class);
        intent.putExtra(getResources().getString(R.string.location_intent_key),Parcels.wrap(data));
        startActivity(intent);

    }
}
