package com.example.han.boostcamp_walktogether.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.han.boostcamp_walktogether.ActionBar.BackButtonActionBarActivity;
import com.example.han.boostcamp_walktogether.Adapters.LocationCommentAdapter;
import com.example.han.boostcamp_walktogether.R;
import com.example.han.boostcamp_walktogether.data.CommentDTO;
import com.example.han.boostcamp_walktogether.util.RetrofitUtil;
import com.example.han.boostcamp_walktogether.util.StringKeys;
import com.example.han.boostcamp_walktogether.view.detail.LocationCommentAddActivity;

import org.parceler.Parcels;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Han on 2017-07-27.
 */

public class LocationCommentActivity extends BackButtonActionBarActivity{

    private final static int PUSH_ADD_COMMENT_BUTTON = 102;
    private LinearLayoutManager mLinearlayoutManager;
    private RecyclerView mLocationCommentRecycelerView;
    private LocationCommentAdapter mLocationCommentAdapter;
    private TextView mLocationTitleTextView;
    private Button mAddCommentButton;
    private Intent mCommentAddIntent;
    private Context mContext;
    private int mParkKey;
    private String mLocationName;
    private LinearLayout mLinearNodata;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_location_comment, null, false);
        mFrameLayout.addView(contentView, 0);
        mTextView.setText(getResources().getString(R.string.location_comment_activity_title));


        mLocationCommentRecycelerView = (RecyclerView)findViewById(R.id.location_comment_recyclerView);
        mAddCommentButton = (Button)findViewById(R.id.location_comment_to_add_button);
        mLocationTitleTextView = (TextView)findViewById(R.id.location_comment_location_title_textView);
        mLinearNodata = (LinearLayout)findViewById(R.id.location_comment_list_noData);

        mLinearlayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mLocationCommentAdapter = new LocationCommentAdapter(this,getResources());
        mContext =this;
        mParkKey = getIntent().getIntExtra(StringKeys.LOCATION_ID_KEY,0);
        mLocationName = getIntent().getStringExtra(StringKeys.LOCATION_NAME);


        mLocationCommentRecycelerView.setLayoutManager(mLinearlayoutManager);
        mLocationCommentRecycelerView.setAdapter(mLocationCommentAdapter);
        mLocationTitleTextView.setText(mLocationName);

        mAddCommentButton.setOnClickListener(onClickListener);
        RetrofitUtil retrofitUtil = RetrofitUtil.retrofit.create(RetrofitUtil.class);
        Call<ArrayList<CommentDTO>> getAllCommentCall = retrofitUtil.getAllComment(mParkKey);
        getAllCommentCall.enqueue(getAllCommentCallback);

    }

    Button.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent commentAddIntent = new Intent(mContext,LocationCommentAddActivity.class);
            commentAddIntent.putExtra(StringKeys.LOCATION_ID_KEY,mParkKey);
            commentAddIntent.putExtra(StringKeys.LOCATION_NAME,mLocationName);
            startActivityForResult(commentAddIntent,PUSH_ADD_COMMENT_BUTTON);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PUSH_ADD_COMMENT_BUTTON && resultCode ==RESULT_OK){

            Log.d("ActivityCommentResult","yes");
            Parcelable commentArrayListParcelable = data.getParcelableExtra(StringKeys.COMMENT_LIST);
            ArrayList<CommentDTO> commentArrayList = Parcels.unwrap(commentArrayListParcelable);
            if(commentArrayList.size()!=0) {
                mLocationCommentAdapter.setmCommentArrayList(commentArrayList);
                mLocationCommentRecycelerView.setVisibility(View.VISIBLE);
                mLinearNodata.setVisibility(View.GONE);
            }
        }

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

    Callback<ArrayList<CommentDTO>> getAllCommentCallback = new Callback<ArrayList<CommentDTO>>() {
        @Override
        public void onResponse(Call<ArrayList<CommentDTO>> call, Response<ArrayList<CommentDTO>> response) {
            if(response.isSuccessful()){
                ArrayList<CommentDTO> commentArrayList = response.body();

                if(commentArrayList.size()==0){
                    mLocationCommentRecycelerView.setVisibility(View.GONE);
                    mLinearNodata.setVisibility(View.VISIBLE);

                }
                else{
                    mLocationCommentRecycelerView.setVisibility(View.VISIBLE);
                    mLinearNodata.setVisibility(View.GONE);
                    mLocationCommentAdapter.setmCommentArrayList(commentArrayList);
                }


            }else{
                mLocationCommentRecycelerView.setVisibility(View.GONE);
                mLinearNodata.setVisibility(View.VISIBLE);

            }

        }

        @Override
        public void onFailure(Call<ArrayList<CommentDTO>> call, Throwable t) {

        }
    };
}
