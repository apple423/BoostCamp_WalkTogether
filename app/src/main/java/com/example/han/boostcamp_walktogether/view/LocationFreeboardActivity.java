package com.example.han.boostcamp_walktogether.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.han.boostcamp_walktogether.ActionBar.BackButtonActionBarActivity;
import com.example.han.boostcamp_walktogether.Adapters.LocationFreeboardAdapter;
import com.example.han.boostcamp_walktogether.Adapters.OnClickLocationFreeboard;
import com.example.han.boostcamp_walktogether.R;
import com.example.han.boostcamp_walktogether.data.FreeboardDTO;
import com.example.han.boostcamp_walktogether.data.FreeboardImageDTO;
import com.example.han.boostcamp_walktogether.util.ComparatorUtil;
import com.example.han.boostcamp_walktogether.util.RetrofitUtil;
import com.example.han.boostcamp_walktogether.util.StringKeys;
import com.example.han.boostcamp_walktogether.view.detail.LocationFreeboardAddActivity;
import com.example.han.boostcamp_walktogether.view.detail.LocationFreeboardSelectActivity;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Han on 2017-07-27.
 */

public class LocationFreeboardActivity extends BackButtonActionBarActivity
                                        implements OnClickLocationFreeboard{

    private static final int PUSH_ADD_BUTTON = 101;
    private GridLayoutManager mGridlayoutManager;
    private RecyclerView mLocationFreeboardRecycelerView;
    private LocationFreeboardAdapter mLocationFreeboardAdapter;
    private int mParKey;
    private ArrayList<FreeboardDTO> mParkFreeboardList;
    private ArrayList<FreeboardImageDTO> mParkFreeboardImageList;

    private final RetrofitUtil retrofitUtil = RetrofitUtil.retrofit.create(RetrofitUtil.class);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_location_freeboard, null, false);
        mFrameLayout.addView(contentView, 0);
        mTextView.setText(getResources().getString(R.string.location_freeboard_activity_title));

        mLocationFreeboardRecycelerView = (RecyclerView)findViewById(R.id.location_freeboard_recyclerView);

        mGridlayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);


        mLocationFreeboardAdapter = new LocationFreeboardAdapter(this,this);
        mLocationFreeboardRecycelerView.setLayoutManager(mGridlayoutManager);
        mLocationFreeboardRecycelerView.setAdapter(mLocationFreeboardAdapter);

        mParKey = getIntent().getIntExtra(StringKeys.LOCATION_ID_KEY,0);
        mParkFreeboardList = new ArrayList<>();
        mParkFreeboardImageList = new ArrayList<>();

      Call<ArrayList<FreeboardDTO>> getfreeboardInParkCall = retrofitUtil.getAllFreeboard(mParKey);
        getfreeboardInParkCall.enqueue(getFreeboardInParkCallback);
       // showProgressBar();

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
                locationFreeboardAddIntent.putExtra(StringKeys.LOCATION_ID_KEY,mParKey);
                startActivityForResult(locationFreeboardAddIntent,PUSH_ADD_BUTTON);


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PUSH_ADD_BUTTON && resultCode == RESULT_OK){
            Log.d("successInResult","yes");
            Parcelable freeboardListParcelabe = data.getParcelableExtra("parkList");
            Parcelable freeboardImageListParcelable = data.getParcelableExtra("parkImageList");
            ArrayList<FreeboardDTO> freeboardArrayList = Parcels.unwrap(freeboardListParcelabe);
            ArrayList<FreeboardImageDTO> freeboardImageArrayList = Parcels.unwrap(freeboardImageListParcelable);
            //mParkFreeboardList = freeboardArrayList;
            //mParkFreeboardImageList = freeboardImageArrayList;
            //Collections.sort(mParkFreeboardImageList, ComparatorUtil.imageDTOComparator);
            mLocationFreeboardAdapter.setParkListAndImage(freeboardArrayList,freeboardImageArrayList);

        }


    }

    @Override
    public void onClickBoard(int position) {
        Intent locationFreeboardSelectIntent = new Intent(this,LocationFreeboardSelectActivity.class);
        locationFreeboardSelectIntent.putExtra(StringKeys.LOCATION_ID_KEY,mParKey);
        locationFreeboardSelectIntent.putExtra(StringKeys.LOCATION_FREEBOARD_KEY,mParkFreeboardList.get(position).getFreeboard_key());
        locationFreeboardSelectIntent.putExtra(StringKeys.LOCATION_FREEBOARD_PARCELABLE, Parcels.wrap(mParkFreeboardList.get(position)));
        startActivity(locationFreeboardSelectIntent);

    }

    Callback<ArrayList<FreeboardDTO>> getFreeboardInParkCallback = new Callback<ArrayList<FreeboardDTO>>() {
        @Override
        public void onResponse(Call<ArrayList<FreeboardDTO>> call, Response<ArrayList<FreeboardDTO>> response) {
            if(response.isSuccessful()){

                ArrayList<FreeboardDTO> freeboardList= response.body();

                mParkFreeboardList = freeboardList;
                for(FreeboardDTO freeboard : mParkFreeboardList){

                    Log.d("chkfreeboardKey",String.valueOf(freeboard.getFreeboard_key()));
                }
               // mLocationFreeboardAdapter.setParkList(mParkFreeboardList);
               for(FreeboardDTO data : mParkFreeboardList){
                    int freeboardKey = data.getFreeboard_key();

                Call<FreeboardImageDTO> getFreeboardImageCall = retrofitUtil.getOneImageFreeboard(mParKey,freeboardKey);
                getFreeboardImageCall.enqueue(getFreeboardImageCallback);
                }
            }

        }

        @Override
        public void onFailure(Call<ArrayList<FreeboardDTO>> call, Throwable t) {

        }
    };

    Callback<FreeboardImageDTO> getFreeboardImageCallback = new Callback<FreeboardImageDTO>() {


        @Override
        public void onResponse(Call<FreeboardImageDTO> call, Response<FreeboardImageDTO> response) {
            if (response.isSuccessful()) {

                FreeboardImageDTO freeboardImageData = response.body();
                Log.d("send_Image_adapter", freeboardImageData.getImage());
                mParkFreeboardImageList.add(freeboardImageData);
                if (mParkFreeboardImageList.size() == mParkFreeboardList.size()) {
                    Collections.sort(mParkFreeboardImageList, ComparatorUtil.imageDTOComparator);
                    for(FreeboardImageDTO freboardImage : mParkFreeboardImageList){
                        Log.d("chkImageFreeboard",String.valueOf(freboardImage.getFreeboard_key()));
                    }
                    mLocationFreeboardAdapter.setParkListAndImage(mParkFreeboardList,mParkFreeboardImageList);
                    Log.d("send_Image_adapter", "gogogogoo");
                }
            }

            else{

                Log.d("send_Image_adapter", "fail");
            }
        }

        @Override
        public void onFailure(Call<FreeboardImageDTO> call, Throwable t) {

            Log.d("send_Image_adapter",t.getMessage());
            Log.d("send_Image_adapter", "gogogogoofail");

        }
    };

}
