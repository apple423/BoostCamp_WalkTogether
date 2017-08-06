package com.example.han.boostcamp_walktogether.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.han.boostcamp_walktogether.R;
import com.example.han.boostcamp_walktogether.data.ParkFreeboardDTO;

import java.util.ArrayList;

/**
 * Created by Han on 2017-07-27.
 */

public class LocationFreeboardAdapter extends RecyclerView.Adapter<LocationFreeboardAdapter.LocationFreeboardViewHolder> {


    private CardView mLocationFreeboardCardView;
    private OnClickLocationFreeboard mOnClickLocationFreeboard;
    private TextView mTitleTextview;
    private TextView mUserNameTextView;
    private ImageView mPicutreImageView;
    private ImageView mProfileImageView;
    private ArrayList<ParkFreeboardDTO> mParkFreeboardArrayList;
    private Context mContext;

    public LocationFreeboardAdapter(Context context,OnClickLocationFreeboard onClickLocationFreeboard, ArrayList<ParkFreeboardDTO> parkFreeboardArrayList){

        mContext = context;
       mOnClickLocationFreeboard = onClickLocationFreeboard;
        mParkFreeboardArrayList = parkFreeboardArrayList;

    }

    public void setParkArrayList(ArrayList<ParkFreeboardDTO> list){
        mParkFreeboardArrayList = list;
        notifyDataSetChanged();

    }

    @Override
    public LocationFreeboardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_freeboard_recycler,parent,false);


        return new LocationFreeboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LocationFreeboardViewHolder holder, int position) {
        ParkFreeboardDTO data = mParkFreeboardArrayList.get(position);
        mUserNameTextView.setText(data.getName());
        mTitleTextview.setText(data.getTitle());
        Glide.with(mContext).load(data.getProfileImageURL()).into(mProfileImageView);
        Glide.with(mContext).load(data.getImageArrayList().get(0)).into(mPicutreImageView);

    }

    @Override
    public int getItemCount() {
        if(mParkFreeboardArrayList.size() == 0) return 0;

        return mParkFreeboardArrayList.size();
    }


    public class LocationFreeboardViewHolder extends RecyclerView.ViewHolder{


        public LocationFreeboardViewHolder(View itemView) {
            super(itemView);
            mLocationFreeboardCardView = (CardView)itemView.findViewById(R.id.location_freeboard_cardView);
            mTitleTextview = (TextView)itemView.findViewById(R.id.location_freeboard_title);
            mUserNameTextView = (TextView) itemView.findViewById(R.id.location_freeboard_user_name);
            mProfileImageView = (ImageView)itemView.findViewById(R.id.location_freeboard_user);
            mPicutreImageView=(ImageView)itemView.findViewById(R.id.location_freeboard_picture);

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
