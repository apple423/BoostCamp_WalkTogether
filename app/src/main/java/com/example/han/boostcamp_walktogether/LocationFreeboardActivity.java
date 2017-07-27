package com.example.han.boostcamp_walktogether;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.han.boostcamp_walktogether.ActionBar.BackButtonActionBarActivity;
import com.example.han.boostcamp_walktogether.Adapters.LocationCommentAdapter;
import com.example.han.boostcamp_walktogether.Adapters.LocationFreeboardAdapter;

/**
 * Created by Han on 2017-07-27.
 */

public class LocationFreeboardActivity extends BackButtonActionBarActivity {


    GridLayoutManager mGridlayoutManager;
    RecyclerView mLocationFreeboardRecycelerView;
    LocationFreeboardAdapter mLocationFreeboardAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_location_freeboard, null, false);
        mFrameLayout.addView(contentView, 0);
        mTextView.setText("게시판");

        mLocationFreeboardRecycelerView = (RecyclerView)findViewById(R.id.location_freeboard_recyclerView);

        mGridlayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);

        mLocationFreeboardAdapter = new LocationFreeboardAdapter();

        mLocationFreeboardRecycelerView.setLayoutManager(mGridlayoutManager);
        mLocationFreeboardRecycelerView.setAdapter(mLocationFreeboardAdapter);


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


        }
        return super.onOptionsItemSelected(item);
    }
}
