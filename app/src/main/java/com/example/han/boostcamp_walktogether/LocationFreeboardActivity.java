package com.example.han.boostcamp_walktogether;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.han.boostcamp_walktogether.ActionBar.BackButtonActionBarActivity;
import com.example.han.boostcamp_walktogether.Adapters.LocationCommentAdapter;
import com.example.han.boostcamp_walktogether.Adapters.LocationFreeboardAdapter;
import com.example.han.boostcamp_walktogether.Adapters.OnClickLocationFreeboard;
import com.example.han.boostcamp_walktogether.data.ParkFreeboardDTO;
import com.example.han.boostcamp_walktogether.helper.FirebaseHelper;
import com.example.han.boostcamp_walktogether.util.StringKeys;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Han on 2017-07-27.
 */

public class LocationFreeboardActivity extends BackButtonActionBarActivity
                                        implements OnClickLocationFreeboard{


    private GridLayoutManager mGridlayoutManager;
    private RecyclerView mLocationFreeboardRecycelerView;
    private LocationFreeboardAdapter mLocationFreeboardAdapter;
    private String mLocationID;
    private DatabaseReference databaseReference;
    private ArrayList<ParkFreeboardDTO> mParkFreeboardArrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_location_freeboard, null, false);
        mFrameLayout.addView(contentView, 0);
        mTextView.setText(getResources().getString(R.string.location_freeboard_activity_title));

        mLocationFreeboardRecycelerView = (RecyclerView)findViewById(R.id.location_freeboard_recyclerView);

        mGridlayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);


        mLocationFreeboardAdapter = new LocationFreeboardAdapter(this,this,mParkFreeboardArrayList);
        mLocationFreeboardRecycelerView.setLayoutManager(mGridlayoutManager);
        mLocationFreeboardRecycelerView.setAdapter(mLocationFreeboardAdapter);

        mLocationID = getIntent().getStringExtra(StringKeys.LOCATION_ID_KEY);
        mParkFreeboardArrayList = new ArrayList<>();

        databaseReference = FirebaseHelper.getParkFreeboardDataReferences(mLocationID);
        Log.d("Location ID",mLocationID);
        databaseReference.addValueEventListener(valueEventListener);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.writemenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case android.R.id.home :
                onBackPressed();
                return true;

            case R.id.action_writing :

                Intent locationFreeboardAddIntent = new Intent(this,LocationFreeboardAddActivity.class);
                locationFreeboardAddIntent.putExtra(StringKeys.LOCATION_ID_KEY,mLocationID);
                startActivity(locationFreeboardAddIntent);


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickBoard() {
        Intent locationFreeboardSelectIntent = new Intent(this,LocationFreeboardSelectActivity.class);
        startActivity(locationFreeboardSelectIntent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocationFreeboardAdapter.setParkArrayList(mParkFreeboardArrayList);
    }

    ValueEventListener valueEventListener = new ValueEventListener(){

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                ParkFreeboardDTO parkFreeboard = snapshot.getValue(ParkFreeboardDTO.class);
                mParkFreeboardArrayList.add(parkFreeboard);


            }

            mLocationFreeboardAdapter.setParkArrayList(mParkFreeboardArrayList);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}
