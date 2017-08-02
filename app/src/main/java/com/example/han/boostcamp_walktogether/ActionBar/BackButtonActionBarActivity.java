package com.example.han.boostcamp_walktogether.ActionBar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.boostcamp_walktogether.R;

/**
 * Created by Han on 2017-07-26.
 */



public class BackButtonActionBarActivity  extends AppCompatActivity{
    protected Toolbar mToolbar;
    protected FrameLayout mFrameLayout;
    protected TextView mTextView;
    protected ImageView mImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.app_bar_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFrameLayout = (FrameLayout) findViewById(R.id.frame_content);
        mTextView = (TextView)findViewById(R.id.toolbar_textView);
        mImageView = (ImageView)findViewById(R.id.toolbar_heart_imageView);

    }



}
