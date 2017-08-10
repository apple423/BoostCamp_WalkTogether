package com.example.han.boostcamp_walktogether.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.han.boostcamp_walktogether.data.ParkFreeboardDTO;
import com.example.han.boostcamp_walktogether.helper.FirebaseHelper;
import com.example.han.boostcamp_walktogether.util.RetrofitUtil;
import com.example.han.boostcamp_walktogether.util.StringKeys;
import com.example.han.boostcamp_walktogether.view.detail.LocationFreeboardAddActivity;
import com.example.han.boostcamp_walktogether.view.detail.LocationFreeboardSelectActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Han on 2017-07-27.
 */

public class LocationFreeboardActivity extends BackButtonActionBarActivity
                                        implements OnClickLocationFreeboard{
    private GridLayoutManager mGridlayoutManager;
    private RecyclerView mLocationFreeboardRecycelerView;
    private LocationFreeboardAdapter mLocationFreeboardAdapter;
    private int mParKey;
    private DatabaseReference databaseReference;
    private List<FreeboardDTO> mParkFreeboardList;
    private List<FreeboardImageDTO> mParkFreeboardImageList;

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
        mParkFreeboardImageList = new ArrayList<>();



        //TODO 7. 파이어베이스에서 게시판의 목록을 가져오는 대신 서버에서 가져오도록 구현해야한다.
        //databaseReference = FirebaseHelper.getParkFreeboardDataReferences(mLocationID);
      // Log.d("Location ID",mLocationID);
       // databaseReference.addValueEventListener(valueEventListener);

        Call<List<FreeboardDTO>> getfreeboardInParkCall = retrofitUtil.getAllFreeboard(mParKey);
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
                startActivity(locationFreeboardAddIntent);


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickBoard(int position) {
        Intent locationFreeboardSelectIntent = new Intent(this,LocationFreeboardSelectActivity.class);
        locationFreeboardSelectIntent.putExtra(StringKeys.LOCATION_ID_KEY,mParKey);
        locationFreeboardSelectIntent.putExtra(StringKeys.LOCATION_FREEBOARD_KEY,mParkFreeboardList.get(position).getFreeboard_key());
        startActivity(locationFreeboardSelectIntent);

    }

    Callback<List<FreeboardDTO>> getFreeboardInParkCallback = new Callback<List<FreeboardDTO>>() {
        @Override
        public void onResponse(Call<List<FreeboardDTO>> call, Response<List<FreeboardDTO>> response) {
            if(response.isSuccessful()){

                List<FreeboardDTO> freeboardList= response.body();

                mParkFreeboardList = freeboardList;
                Log.d("send_freeboard", "gogogogoo");
                mLocationFreeboardAdapter.setParkList(mParkFreeboardList);
               for(FreeboardDTO data : freeboardList){
                    int freeboardKey = data.getFreeboard_key();

                Call<FreeboardImageDTO> getFreeboardImageCall = retrofitUtil.getOneImageFreeboard(mParKey,freeboardKey);
                getFreeboardImageCall.enqueue(getFreeboardImageCallback);
                }
                // mLocationFreeboardAdapter.setParkList(freeboardList);
                /*Call<List<FreeboardImageDTO>> getFreeboardImageCall = retrofitUtil.getImagesFreeboard(mParKey);
                getFreeboardImageCall.enqueue(getFreeboardImageCallback);*/
            }


        }

        @Override
        public void onFailure(Call<List<FreeboardDTO>> call, Throwable t) {

        }
    };

    Callback<FreeboardImageDTO> getFreeboardImageCallback = new Callback<FreeboardImageDTO>() {


        @Override
        public void onResponse(Call<FreeboardImageDTO> call, Response<FreeboardImageDTO> response) {
            if (response.isSuccessful()) {

                FreeboardImageDTO freeboardImageData = response.body();

                mParkFreeboardImageList.add(freeboardImageData);
                //Log.d("mParkImageListSize",String.valueOf(mParkFreeboardImageList.size()));
                if (mParkFreeboardImageList.size() == mParkFreeboardList.size()) {
                    mLocationFreeboardAdapter.setParkImageList(mParkFreeboardImageList);
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


/*    // 파이어베이스에서 정보를 가져 온 후에 처리
    ValueEventListener valueEventListener = new ValueEventListener(){

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            mParkFreeboardList.clear();


            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                ParkFreeboardDTO parkFreeboard = snapshot.getValue(ParkFreeboardDTO.class);
                mParkFreeboardArrayList.add(parkFreeboard);


            }

            mLocationFreeboardAdapter.setParkArrayList(mParkFreeboardArrayList);
            hideProgressBar();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };*/
}
