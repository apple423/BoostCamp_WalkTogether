package com.example.han.boostcamp_walktogether;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.han.boostcamp_walktogether.ActionBar.BackButtonActionBarActivity;
import com.example.han.boostcamp_walktogether.ActionBar.DrawerBaseActivity;

/**
 * Created by Han on 2017-07-26.
 */

public class LocationActivity extends BackButtonActionBarActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_location, null, false);
        mFrameLayout.addView(contentView, 0);
        mTextView.setText("장소");


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
