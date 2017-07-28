package com.example.han.boostcamp_walktogether;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.example.han.boostcamp_walktogether.ActionBar.BackButtonActionBarActivity;
import com.example.han.boostcamp_walktogether.Adapters.LocationFreeboardAddPictureAdapter;

/**
 * Created by Han on 2017-07-28.
 */

public class LocationFreeboardAddActivity extends BackButtonActionBarActivity{

    RecyclerView pictureRecyclerView;
    LinearLayoutManager linearLayoutManager;
    LocationFreeboardAddPictureAdapter locationFreeboardAddPictureAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_location_freeboard_add, null, false);
        mFrameLayout.addView(contentView, 0);
        mTextView.setText("장소 게시판 글작성");

        pictureRecyclerView = (RecyclerView)findViewById(R.id.location_freeboard_add_picture_recyclerView);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        locationFreeboardAddPictureAdapter = new LocationFreeboardAddPictureAdapter();

        pictureRecyclerView.setLayoutManager(linearLayoutManager);
        pictureRecyclerView.setAdapter(locationFreeboardAddPictureAdapter);

    }
}
