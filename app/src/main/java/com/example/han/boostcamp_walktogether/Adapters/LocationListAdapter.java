package com.example.han.boostcamp_walktogether.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.han.boostcamp_walktogether.R;
import com.example.han.boostcamp_walktogether.data.CommentAveragePointDTO;
import com.example.han.boostcamp_walktogether.data.ParkRowDTO;

import java.util.ArrayList;

/**
 * Created by Han on 2017-08-12.
 */

public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.LocationListViewHolder> {



    private ArrayList<ParkRowDTO> parkRowDTOArrayList;
    private ArrayList<CommentAveragePointDTO> commentAveragePointDTOArrayList;
    private Context mContext;

    public LocationListAdapter (Context context){
        mContext = context;

    }
    public void setDataArrayList(ArrayList<ParkRowDTO> parkRowDTOs, ArrayList<CommentAveragePointDTO> commentAveragePointDTOs){

        parkRowDTOArrayList = parkRowDTOs;
        commentAveragePointDTOArrayList = commentAveragePointDTOs;
        notifyDataSetChanged();

    }

    @Override
    public LocationListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_list_recycler,parent,false);
        return new LocationListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LocationListViewHolder holder, int position) {
        ParkRowDTO parkRowData = parkRowDTOArrayList.get(position);
        CommentAveragePointDTO commentAveragePointDTO = commentAveragePointDTOArrayList.get(position);
        if(parkRowData.getImage_url().length()==0){

            holder.mPicutureImageView.setImageResource(R.drawable.park_default_picture);
        }

        else{

            Glide.with(mContext).load(parkRowData.getImage_url()).into(holder.mPicutureImageView);

        }

        holder.mLocationTitleTextView.setText(parkRowData.getName());
        holder.mLocationAddressTextView.setText(parkRowData.getAddress());
        holder.mLocationHeartTextView.setText(Float.toString(commentAveragePointDTO.getAvgStar()));
        holder.mLocationPetTextView.setText(Float.toString(commentAveragePointDTO.getAvgPet()));
        holder.mLocationDistanceTextView.setText(Double.toString(parkRowData.getDistance()));
    }

    @Override
    public int getItemCount() {
        if(parkRowDTOArrayList ==null) return 0;

        return parkRowDTOArrayList.size();
    }

    public class LocationListViewHolder extends RecyclerView.ViewHolder{
        ImageView mPicutureImageView;
        TextView mLocationTitleTextView;
        TextView mLocationAddressTextView;
        TextView mLocationHeartTextView;
        TextView mLocationPetTextView;
        TextView mLocationDistanceTextView;


        public LocationListViewHolder(View itemView) {
            super(itemView);

            mPicutureImageView = (ImageView)itemView.findViewById(R.id.location_list_picture);
            mLocationTitleTextView = (TextView)itemView.findViewById(R.id.location_list_title);
            mLocationAddressTextView = (TextView)itemView.findViewById(R.id.location_list_address);
            mLocationHeartTextView = (TextView) itemView.findViewById(R.id.location_list_favorite_score);
            mLocationPetTextView = (TextView)itemView.findViewById(R.id.location_list_pet_score);
            mLocationDistanceTextView = (TextView)itemView.findViewById(R.id.location_list_distance_score);


        }
    }
}
