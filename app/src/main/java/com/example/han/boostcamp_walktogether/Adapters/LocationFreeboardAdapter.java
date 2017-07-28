package com.example.han.boostcamp_walktogether.Adapters;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.han.boostcamp_walktogether.R;

/**
 * Created by Han on 2017-07-27.
 */

public class LocationFreeboardAdapter extends RecyclerView.Adapter<LocationFreeboardAdapter.LocationFreeboardViewHolder> {


    CardView mLocationFreeboardCardView;
    OnClickLocationFreeboard mOnClickLocationFreeboard;

    public LocationFreeboardAdapter(OnClickLocationFreeboard onClickLocationFreeboard){

       mOnClickLocationFreeboard = onClickLocationFreeboard;

    }

    @Override
    public LocationFreeboardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_freeboard_recycler,parent,false);


        return new LocationFreeboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LocationFreeboardViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 8;
    }


    public class LocationFreeboardViewHolder extends RecyclerView.ViewHolder{


        public LocationFreeboardViewHolder(View itemView) {
            super(itemView);
            mLocationFreeboardCardView = (CardView)itemView.findViewById(R.id.location_freeboard_cardView);

            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickLocationFreeboard.onClickBoard();
                }
            };

            mLocationFreeboardCardView.setOnClickListener(onClickListener);

        }
    }


}
