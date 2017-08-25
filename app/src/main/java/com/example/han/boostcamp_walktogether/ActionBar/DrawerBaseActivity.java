package com.example.han.boostcamp_walktogether.ActionBar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.han.boostcamp_walktogether.R;
import com.example.han.boostcamp_walktogether.util.SharedPreferenceUtil;
import com.example.han.boostcamp_walktogether.util.StringKeys;
import com.example.han.boostcamp_walktogether.view.LocationListActivity;
import com.example.han.boostcamp_walktogether.view.MapActivity;
import com.example.han.boostcamp_walktogether.view.WalkDiaryActivity;
import com.example.han.boostcamp_walktogether.view.detail.LibraryActivity;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Han on 2017-07-26.
 */

public class DrawerBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 111;
    protected DrawerLayout mDrawerLayout;
    protected Toolbar mToolbar;
    protected FrameLayout mFrameLayout;
    protected ProgressBar mProgressBar;
    protected NavigationView mNavigationView;
    protected CircleImageView mNavProfileCircleImageView;
    protected TextView mNavNametextView, mNavEmailtextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.navigation_drawer);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mFrameLayout = (FrameLayout) findViewById(R.id.frame_content);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mProgressBar = (ProgressBar) findViewById(R.id.base_progress_bar);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView)findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        View hView =  mNavigationView.getHeaderView(0);

        mNavProfileCircleImageView = (CircleImageView) hView.findViewById(R.id.nav_profile_imageView);
        mNavNametextView = (TextView) hView.findViewById(R.id.nav_name_textView);
        mNavEmailtextView = (TextView) hView.findViewById(R.id.nav_email_textView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case  R.id.action_library :
                Intent intent = new Intent(this, LibraryActivity.class);
                startActivity(intent);
                break;

        }

        return super.onOptionsItemSelected(item);

    }

    protected void showProgressBar(){
        mProgressBar.setVisibility(View.VISIBLE);

    }

    protected void hideProgressBar(){

        mProgressBar.setVisibility(View.INVISIBLE);
    }

    protected void getUserProfileAndSetHeader() {
        SharedPreferenceUtil.setUserProfileSharedPreference(this, StringKeys.USER_PROFILE, MODE_PRIVATE);
        String userName = SharedPreferenceUtil.getUserProfile(StringKeys.USER_NICK_NAME);
        String userEmail = SharedPreferenceUtil.getUserProfile(StringKeys.USER_EMAIL);
        String userProfile = SharedPreferenceUtil.getUserProfile(StringKeys.USER_PROFILE_PICTURE);
        setUserProfileInNavHeader(userName,userEmail,userProfile);
    }

    protected void setUserProfileInNavHeader(String userName,String userEmail, String userProfile){

        Glide.with(this).load(userProfile).into(mNavProfileCircleImageView);
        mNavNametextView.setText(userName);
        mNavEmailtextView.setText(userEmail);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id){

            case R.id.nav_diary :
                Intent intentDiary = new Intent(this,WalkDiaryActivity.class);
                intentDiary.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentDiary);
                break;

            case R.id.nav_location_list :
                Intent intentList = new Intent(this,LocationListActivity.class);
                intentList.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentList);

                break;

            case R.id.nav_location_map :
                Intent intentMap = new Intent(this,MapActivity.class);
                intentMap.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentMap);
                break;

            case R.id.nav_location_auto :
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

        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                double latitude = place.getLatLng().latitude;
                double longtitude = place.getLatLng().longitude;
                Log.d("latitudeSearch",latitude +"");
                Log.d("longitudeSearch",longtitude +"");
                Intent intent = new Intent(this,MapActivity.class);
                intent.putExtra(StringKeys.SEARCH_LATITUDE,latitude);
                intent.putExtra(StringKeys.SEARCH_LONGITUDE,longtitude);
                startActivity(intent);

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
}
