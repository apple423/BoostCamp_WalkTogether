package com.example.han.boostcamp_walktogether.Adapters;

import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.han.boostcamp_walktogether.interfaces.LocationFreeboardAddInterface;
import com.example.han.boostcamp_walktogether.R;

import java.util.ArrayList;

/**
 * Created by Han on 2017-07-28.
 */

public class LocationFreeboardAddPictureAdapter extends RecyclerView.Adapter {

    private LocationFreeboardAddInterface locationFreeboardAddInterface;
    private ArrayList<Uri> mImageUriArrayList;
    private String[] filePathColumn = { MediaStore.Images.Media.DATA };

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
        mLocationFreeboardAddPicutreImageView.setImageURI(imageUri);

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
