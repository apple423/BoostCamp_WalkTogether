package com.example.han.boostcamp_walktogether.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.han.boostcamp_walktogether.R;

/**
 * Created by Han on 2017-07-28.
 */

public class LocationFreeboardAddPictureAdapter extends RecyclerView.Adapter {

    ImageView mLocationFreeboardAddPicutreImageView;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_freeboard_add_picture_recycler,parent,false);
        return new LocationFreeboardPictureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class LocationFreeboardPictureViewHolder extends RecyclerView.ViewHolder{


        public LocationFreeboardPictureViewHolder(View itemView) {
            super(itemView);
            mLocationFreeboardAddPicutreImageView = (ImageView)itemView
                    .findViewById(R.id.location_freeboard_add_picture_recyclerView);


        }
    }
}
