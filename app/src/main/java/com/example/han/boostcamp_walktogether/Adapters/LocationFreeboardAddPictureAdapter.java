package com.example.han.boostcamp_walktogether.Adapters;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.han.boostcamp_walktogether.interfaces.LocationFreeboardAddInterface;
import com.example.han.boostcamp_walktogether.R;

import java.util.ArrayList;

/**
 * Created by Han on 2017-07-28.
 */
// 장소별 게시판의 게시물 등록시 추가한 사진들을 보여주기 위한 RecyclerView 어댑터
public class LocationFreeboardAddPictureAdapter extends RecyclerView.Adapter {

    private LocationFreeboardAddInterface locationFreeboardAddInterface;
    private ArrayList<Uri> mImageUriArrayList;
    private Context mContext;


    public LocationFreeboardAddPictureAdapter(Context mContext) {
        this.mContext = mContext;
    }

    ImageView mLocationFreeboardAddPicutreImageView;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_freeboard_add_picture_recycler,parent,false);

        return new LocationFreeboardPictureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Uri imageUri = mImageUriArrayList.get(position);
        Log.d("count item",Integer.toString(getItemCount()));
        //mLocationFreeboardAddPicutreImageView.setImageURI(imageUri);
        Glide.with(mContext).load(imageUri).into(mLocationFreeboardAddPicutreImageView);

    }

    @Override
    public int getItemCount() {

        if(mImageUriArrayList == null) return 0;
        return mImageUriArrayList.size();
    }

    public void setImageUriArrayList(ArrayList<Uri> arrayList){

        mImageUriArrayList = arrayList;
        notifyDataSetChanged();

    }

    public class LocationFreeboardPictureViewHolder extends RecyclerView.ViewHolder{


        public LocationFreeboardPictureViewHolder(View itemView) {
            super(itemView);
            mLocationFreeboardAddPicutreImageView = (ImageView)itemView
                    .findViewById(R.id.location_freeboard_add_picture_recyclerView_imageView);


        }
    }
}
