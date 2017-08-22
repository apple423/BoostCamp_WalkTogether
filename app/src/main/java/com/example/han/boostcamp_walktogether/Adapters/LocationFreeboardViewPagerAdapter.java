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

import com.bumptech.glide.Glide;
import com.example.han.boostcamp_walktogether.R;
import com.example.han.boostcamp_walktogether.data.FreeboardImageDTO;

import java.util.ArrayList;

/**
 * Created by Han on 2017-07-28.
 */
// 장소별 게시판의 게시글을 선택한 후 게시글의 사진들을 보여주기 위한 ViewPager의 어댑터
public class LocationFreeboardViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private ImageView imageView;
    private TextView textView;
    private LayoutInflater mLayoutInflater;
    private Resources mResources;
    private ArrayList<FreeboardImageDTO> imageArrayList;


    public LocationFreeboardViewPagerAdapter(Context context, LayoutInflater layoutInflater, Resources resources){

        mContext = context;
        mLayoutInflater = layoutInflater;
        mResources = resources;
    }

    public void setImageArrayList(ArrayList<FreeboardImageDTO> list){
        imageArrayList = list;
        notifyDataSetChanged();

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = mLayoutInflater
                .inflate(R.layout.location_freeboard_picture_viewpager,container,false);

        FreeboardImageDTO freeboardImageDTO = imageArrayList.get(position);
        String imageURL = freeboardImageDTO.getImage();
        if(imageURL.equals("empty")){
            view.setVisibility(View.GONE);
        }

        imageView = (ImageView)view.findViewById(R.id.location_freeboard_picture_imageView_viewPager);
        //imageView.setImageResource(R.mipmap.ic_launcher);

        Glide.with(mContext).load("http://"+imageURL).placeholder(R.drawable.placeholder).into(imageView);
        textView = (TextView) view.findViewById(R.id.location_freeboard_picture_textView_viewPager);

        String count = mResources.getString(R.string.viewPager_picture_count);

        textView.setText(String.format(count,position+1,getCount()));


        container.addView(view);

        return view;


    }

    @Override
    public int getCount() {

        if(imageArrayList==null) return 0;
        return imageArrayList.size();
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
