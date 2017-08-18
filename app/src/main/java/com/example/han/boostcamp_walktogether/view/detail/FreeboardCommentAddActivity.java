package com.example.han.boostcamp_walktogether.view.detail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.example.han.boostcamp_walktogether.ActionBar.BackButtonActionBarActivity;
import com.example.han.boostcamp_walktogether.Adapters.LocationFreeboardCommentDetailAdapter;
import com.example.han.boostcamp_walktogether.R;

/**
 * Created by Han on 2017-08-18.
 */

public class FreeboardCommentAddActivity extends BackButtonActionBarActivity {

    RecyclerView mCommentRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_location_freeboard_comment, null, false);
        mFrameLayout.addView(contentView, 0);
        mTextView.setText(getResources().getString(R.string.location_freeboard_comment_title));

        mCommentRecyclerView = (RecyclerView)findViewById(R.id.location_freeboard_comment_detail_recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        LocationFreeboardCommentDetailAdapter locationFreeboardCommentDetailAdapter = new LocationFreeboardCommentDetailAdapter();

        mCommentRecyclerView.setLayoutManager(linearLayoutManager);
        mCommentRecyclerView.setAdapter(locationFreeboardCommentDetailAdapter);



    }
}
