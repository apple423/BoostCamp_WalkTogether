package com.example.han.boostcamp_walktogether;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.example.han.boostcamp_walktogether.ActionBar.BackButtonActionBarActivity;

/**
 * Created by Han on 2017-07-27.
 */

public class LocationCommentActivity extends BackButtonActionBarActivity{

    RecyclerView mLocationCommentRecycelerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_location_comment, null, false);
        mFrameLayout.addView(contentView, 0);
        mTextView.setText("유저평가");

        mLocationCommentRecycelerView = (RecyclerView)findViewById(R.id.location_comment_recyclerView);

    }
}
