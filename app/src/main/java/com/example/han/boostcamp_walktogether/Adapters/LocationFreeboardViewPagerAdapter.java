package com.example.han.boostcamp_walktogether.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.han.boostcamp_walktogether.R;

/**
 * Created by Han on 2017-07-28.
 */

public class LocationFreeboardViewPagerAdapter extends PagerAdapter {

    ImageView imageView;
    TextView textView;
    LayoutInflater mLayoutInflater;
    Resources mResources;

    public LocationFreeboardViewPagerAdapter(LayoutInflater layoutInflater, Resources resources){

        mLayoutInflater = layoutInflater;
        mResources = resources;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = mLayoutInflater
                .inflate(R.layout.location_freeboard_picture_viewpager,container,false);

        imageView = (ImageView)view.findViewById(R.id.location_freeboard_picture_imageView_viewPager);
        imageView.setImageResource(R.mipmap.ic_launcher);
        textView = (TextView) view.findViewById(R.id.location_freeboard_picture_textView_viewPager);

        String count = mResources.getString(R.string.viewPager_picture_count);

        textView.setText(String.format(count,position+1,getCount()));


        container.addView(view);

        return view;


    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view== ((FrameLayout)object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((FrameLayout)object);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
    }

    @Override
    public void startUpdate(ViewGroup container) {
        super.startUpdate(container);
    }
}
