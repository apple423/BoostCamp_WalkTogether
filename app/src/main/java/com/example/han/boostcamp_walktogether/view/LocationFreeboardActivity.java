package com.example.han.boostcamp_walktogether.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
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
import android.widget.TextView;

import com.example.han.boostcamp_walktogether.ActionBar.BackButtonActionBarActivity;
import com.example.han.boostcamp_walktogether.Adapters.LocationFreeboardAdapter;
import com.example.han.boostcamp_walktogether.interfaces.OnClickFreeboardInterface;
import com.example.han.boostcamp_walktogether.R;
import com.example.han.boostcamp_walktogether.data.FreeboardDTO;
import com.example.han.boostcamp_walktogether.data.FreeboardImageDTO;
import com.example.han.boostcamp_walktogether.util.ComparatorUtil;
import com.example.han.boostcamp_walktogether.util.RetrofitUtil;
import com.example.han.boostcamp_walktogether.util.SharedPreferenceUtil;
import com.example.han.boostcamp_walktogether.util.StringKeys;
import com.example.han.boostcamp_walktogether.view.detail.LocationFreeboardAddActivity;
import com.example.han.boostcamp_walktogether.view.detail.LocationFreeboardSelectActivity;
import com.kakao.network.response.ResponseBody;

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
                                        implements OnClickFreeboardInterface {

    private static final int PUSH_ADD_BUTTON = 101;
    private static final int PUSH_SELECT_ACTIVITY = 102;
    private GridLayoutManager mGridlayoutManager;
    private RecyclerView mLocationFreeboardRecycelerView;
    private LocationFreeboardAdapter mLocationFreeboardAdapter;
    private int mParKey;
    private String mUserEmail;
    private ArrayList<FreeboardDTO> mParkFreeboardList;
    private ArrayList<FreeboardImageDTO> mParkFreeboardImageList;
    private ArrayList<FreeboardDTO> mFreeboardLikeList;
    private ArrayList<FreeboardDTO> mFreeboardUserLikeList;
    private FreeboardDTO mFreeboardUserLikeSendDTO;

    private final RetrofitUtil retrofitUtil = RetrofitUtil.retrofit.create(RetrofitUtil.class);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_location_freeboard, null, false);
        mFrameLayout.addView(contentView, 0);
        mTextView.setText(getResources().getString(R.string.location_freeboard_activity_title));

        mLocationFreeboardRecycelerView = (RecyclerView) findViewById(R.id.location_freeboard_recyclerView);

        mGridlayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);


        mLocationFreeboardAdapter = new LocationFreeboardAdapter(this, this, getResources());
        mLocationFreeboardRecycelerView.setLayoutManager(mGridlayoutManager);
        mLocationFreeboardRecycelerView.setAdapter(mLocationFreeboardAdapter);

        SharedPreferenceUtil.setUserProfileSharedPreference(this, StringKeys.USER_PROFILE, MODE_PRIVATE);
        mUserEmail = SharedPreferenceUtil.getUserProfile(StringKeys.USER_EMAIL);
        mParKey = getIntent().getIntExtra(StringKeys.LOCATION_ID_KEY, 0);
        mParkFreeboardList = new ArrayList<>();
        mParkFreeboardImageList = new ArrayList<>();
        mFreeboardLikeList = new ArrayList<>();
        mFreeboardUserLikeList = new ArrayList<>();
        mFreeboardUserLikeSendDTO = new FreeboardDTO();

        Call<ArrayList<FreeboardDTO>> getfreeboardInParkCall = retrofitUtil.getAllFreeboard(mParKey);
        getfreeboardInParkCall.enqueue(getFreeboardInParkCallback);
        // showProgressBar();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.writemenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_writing:

                Intent locationFreeboardAddIntent = new Intent(this, LocationFreeboardAddActivity.class);
                locationFreeboardAddIntent.putExtra(StringKeys.LOCATION_ID_KEY, mParKey);
                locationFreeboardAddIntent.putExtra(StringKeys.USER_EMAIL, mUserEmail);
                startActivityForResult(locationFreeboardAddIntent, PUSH_ADD_BUTTON);


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PUSH_ADD_BUTTON && resultCode == RESULT_OK) {
            Log.d("successInResult", "yes");
            Parcelable freeboardListParcelabe = data.getParcelableExtra(StringKeys.PARK_LIST);
            Parcelable freeboardImageListParcelable = data.getParcelableExtra(StringKeys.PARK_IMAGE_LIST);
            Parcelable freeboardLikeListParcelabe = data.getParcelableExtra(StringKeys.PARK_LIKE_LIST);
            Parcelable freeboardUserLikeListParcelabe = data.getParcelableExtra(StringKeys.PARK_USER_LIKE);

            ArrayList<FreeboardDTO> freeboardArrayList = Parcels.unwrap(freeboardListParcelabe);
            ArrayList<FreeboardImageDTO> freeboardImageArrayList = Parcels.unwrap(freeboardImageListParcelable);
            ArrayList<FreeboardDTO> freeboardLikeArrayList = Parcels.unwrap(freeboardLikeListParcelabe);
            ArrayList<FreeboardDTO> freeboardUserLikeArrayList = Parcels.unwrap(freeboardUserLikeListParcelabe);

            mParkFreeboardList = freeboardArrayList;
            mParkFreeboardImageList = freeboardImageArrayList;
            mFreeboardLikeList = freeboardLikeArrayList;
            mFreeboardUserLikeList = freeboardUserLikeArrayList;

            //Collections.sort(mParkFreeboardImageList, ComparatorUtil.imageDTOComparator);
            mLocationFreeboardAdapter.setParkListAndImage(mParkFreeboardList, mParkFreeboardImageList
                    , mFreeboardLikeList, mFreeboardUserLikeList);

        }
        else if(requestCode == PUSH_SELECT_ACTIVITY && resultCode == RESULT_OK){
            Parcelable freeboardLikeListParcelabe = data.getParcelableExtra(StringKeys.PARK_LIKE_LIST);
            Parcelable freeboardUserLikeListParcelabe = data.getParcelableExtra(StringKeys.PARK_USER_LIKE);
            ArrayList<FreeboardDTO> freeboardLikeArrayList = Parcels.unwrap(freeboardLikeListParcelabe);
            ArrayList<FreeboardDTO> freeboardUserLikeArrayList = Parcels.unwrap(freeboardUserLikeListParcelabe);
            mFreeboardLikeList = freeboardLikeArrayList;
            mFreeboardUserLikeList = freeboardUserLikeArrayList;
            mLocationFreeboardAdapter.setParkListAndImage(mParkFreeboardList, mParkFreeboardImageList
                    , mFreeboardLikeList, mFreeboardUserLikeList);

        }


    }

    @Override
    public void onClickBoard(int position) {
        Intent locationFreeboardSelectIntent = new Intent(this, LocationFreeboardSelectActivity.class);
        locationFreeboardSelectIntent.putExtra(StringKeys.LOCATION_ID_KEY, mParKey);
        locationFreeboardSelectIntent.putExtra(StringKeys.LOCATION_FREEBOARD_KEY, mParkFreeboardList.get(position).getFreeboard_key());
        locationFreeboardSelectIntent.putExtra(StringKeys.LOCATION_FREEBOARD_PARCELABLE, Parcels.wrap(mParkFreeboardList.get(position)));
        //startActivity(locationFreeboardSelectIntent);
        startActivityForResult(locationFreeboardSelectIntent,PUSH_SELECT_ACTIVITY);

    }

    @Override
    public String onClickLike(TextView textView, int position, boolean like) {

        if (like) {
            String countString = textView.getText().toString();
            int count = Integer.valueOf(countString);
            Log.d("countValue", count + "");
            textView.setText(String.valueOf(count + 1));
            textView.setTextColor(getResources().getColor(R.color.redText));
            Call<FreeboardDTO> freeboardLikeDTOCall = retrofitUtil.postLike(mParkFreeboardList.get(position)
                    .getFreeboard_key(), mUserEmail, mFreeboardUserLikeSendDTO);
            freeboardLikeDTOCall.enqueue(new Callback<FreeboardDTO>() {
                @Override
                public void onResponse(Call<FreeboardDTO> call, Response<FreeboardDTO> response) {
                    if (response.isSuccessful()) {

                        Log.d("LikePostSuccess", "tetete");
                    }

                }

                @Override
                public void onFailure(Call<FreeboardDTO> call, Throwable t) {

                }
            });

            return String.valueOf(count + 1);
        } else {

            String countString = textView.getText().toString();
            int count = Integer.valueOf(countString);
            Log.d("countValue", count + "");
            textView.setText(String.valueOf(count - 1));
            textView.setTextColor(Color.BLACK);
            Call<com.kakao.network.response.ResponseBody> freeboardLikeDeleteCall = retrofitUtil.deleteLike(mParkFreeboardList.get(position)
                    .getFreeboard_key(), mUserEmail);
            freeboardLikeDeleteCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {

                        Log.d("DeleteLikeSuccess", "gsasga");

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

            return String.valueOf(count - 1);
        }

    }


    Callback<ArrayList<FreeboardDTO>> getFreeboardInParkCallback = new Callback<ArrayList<FreeboardDTO>>() {
        @Override
        public void onResponse(Call<ArrayList<FreeboardDTO>> call, Response<ArrayList<FreeboardDTO>> response) {
            if (response.isSuccessful()) {

                ArrayList<FreeboardDTO> freeboardList = response.body();

                mParkFreeboardList = freeboardList;

                new AsyncArrayList().execute();

                for (FreeboardDTO data : mParkFreeboardList) {
                    int freeboardKey = data.getFreeboard_key();

                    Call<FreeboardDTO> getFreeboardLikeCall = retrofitUtil.getLikeCount(freeboardKey);
                    getFreeboardLikeCall.enqueue(getFreeboardLikeCallback);

                    Call<FreeboardDTO> getFreeboardUserLikeCall = retrofitUtil.getUserPushLike(freeboardKey, mUserEmail);
                    getFreeboardUserLikeCall.enqueue(getFreeboardLikeUserCallback);

                    Call<FreeboardImageDTO> getFreeboardImageCall = retrofitUtil.getOneImageFreeboard(mParKey, freeboardKey);
                    getFreeboardImageCall.enqueue(getFreeboardImageCallback);
                }
            }

        }

        @Override
        public void onFailure(Call<ArrayList<FreeboardDTO>> call, Throwable t) {

        }
    };

    Callback<FreeboardDTO> getFreeboardLikeCallback = new Callback<FreeboardDTO>() {
        @Override
        public void onResponse(Call<FreeboardDTO> call, Response<FreeboardDTO> response) {
            if (response.isSuccessful()) {

                mFreeboardLikeList.add(response.body());

            }
        }

        @Override
        public void onFailure(Call<FreeboardDTO> call, Throwable t) {

        }
    };


    Callback<FreeboardDTO> getFreeboardLikeUserCallback = new Callback<FreeboardDTO>() {
        @Override
        public void onResponse(Call<FreeboardDTO> call, Response<FreeboardDTO> response) {
            if (response.isSuccessful()) {

                mFreeboardUserLikeList.add(response.body());

            }

        }

        @Override
        public void onFailure(Call<FreeboardDTO> call, Throwable t) {

        }
    };

    Callback<FreeboardImageDTO> getFreeboardImageCallback = new Callback<FreeboardImageDTO>() {


        @Override
        public void onResponse(Call<FreeboardImageDTO> call, Response<FreeboardImageDTO> response) {
            if (response.isSuccessful()) {

                FreeboardImageDTO freeboardImageData = response.body();
                Log.d("send_Image_adapter", freeboardImageData.getImage());
                mParkFreeboardImageList.add(freeboardImageData);

            } else {

                Log.d("send_Image_adapter", "fail");
            }
        }

        @Override
        public void onFailure(Call<FreeboardImageDTO> call, Throwable t) {

            Log.d("send_Image_adapter", t.getMessage());
            Log.d("send_Image_adapter", "gogogogoofail");

        }
    };

    public class AsyncArrayList extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            while(true) {
               // Log.d("asynctaskGo","yes");
                if (mParkFreeboardImageList.size() == mParkFreeboardList.size()
                        && mParkFreeboardImageList.size() == mFreeboardLikeList.size()
                        && mFreeboardLikeList.size() == mFreeboardUserLikeList.size()
                        && mFreeboardUserLikeList.size() == mParkFreeboardList.size()) {

                    Collections.sort(mParkFreeboardImageList, ComparatorUtil.imageDTOComparator);
                    Collections.sort(mFreeboardLikeList, ComparatorUtil.likeDTOComparator);
                    Collections.sort(mFreeboardUserLikeList, ComparatorUtil.likeDTOComparator);
                    /*mLocationFreeboardAdapter.setParkListAndImage(mParkFreeboardList, mParkFreeboardImageList
                            , mFreeboardLikeList, mFreeboardUserLikeList);*/
                    Log.d("asynctaskFinish","yes");
                    return null;
                }

            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("asynctaskPostFinish","yes");
            mLocationFreeboardAdapter.setParkListAndImage(mParkFreeboardList, mParkFreeboardImageList
                    , mFreeboardLikeList, mFreeboardUserLikeList);
        }
    }
}
