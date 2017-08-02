package com.example.han.boostcamp_walktogether;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.han.boostcamp_walktogether.ActionBar.DrawerBaseActivity;
import com.example.han.boostcamp_walktogether.helper.FirebaseHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kakao.auth.Session;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

/**
 * Created by Han on 2017-07-25.
 */

public class MapActivity extends DrawerBaseActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener{


    private Intent mLoginIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_map, mFrameLayout, false);
        mDrawerLayout.addView(contentView, 0);



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mLoginIntent = new Intent(this,LoginActivity.class);
        mLoginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }
    public void onMapReady(GoogleMap googleMap) {
        LatLng sydney = new LatLng(-33.852, 151.211);
        Marker marker = googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney"));

        googleMap.setOnMarkerClickListener(this);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

       Intent intent  = new Intent(this,LocationActivity.class);
        startActivity(intent);
        return false;
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);

        this.getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder alterDialogBuilder = new AlertDialog.Builder(this)
                .setTitle("종료하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
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

        switch (id){
            case R.id.action_sign_out :
                AlertDialog.Builder alterDialogBuilder = new AlertDialog.Builder(this)
                        .setTitle("로그아웃 하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(FirebaseHelper.signInState()!=null) {
                                    FirebaseHelper.signOut();
                                    startActivity(mLoginIntent);
                                }

                                else if(Session.getCurrentSession().isOpened()){

                                    UserManagement.requestLogout(new LogoutResponseCallback() {
                                        @Override
                                        public void onCompleteLogout() {
                                            startActivity(mLoginIntent);
                                        }
                                    });
                                }
                                //startActivity(mLoginIntent);

                            }
                        })
                        .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alterDialogBuilder.create();
                alertDialog.show();

                //startActivity(mLoginIntent);

        }

        return super.onOptionsItemSelected(item);


    }
}
