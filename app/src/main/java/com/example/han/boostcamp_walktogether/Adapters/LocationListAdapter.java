package com.example.han.boostcamp_walktogether.Adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.han.boostcamp_walktogether.R;
import com.example.han.boostcamp_walktogether.data.CommentAveragePointDTO;
import com.example.han.boostcamp_walktogether.data.ParkListDTO;
import com.example.han.boostcamp_walktogether.data.ParkRowDTO;
import com.example.han.boostcamp_walktogether.interfaces.OnClickLocationListInterface;
import com.example.han.boostcamp_walktogether.util.ComparatorUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Han on 2017-08-12.
 */

public class LocationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private ArrayList<ParkListDTO> parkListDTOArrayList;
    private Context mContext;
    private OnClickLocationListInterface mOnClickLocationListInterface;

    public LocationListAdapter (Context context,OnClickLocationListInterface onClickLocationListInterface){
        mContext = context;
        mOnClickLocationListInterface = onClickLocationListInterface;
    }

    public void setParkArrayList(ArrayList<ParkListDTO> parkArrayList){

        parkListDTOArrayList = parkArrayList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == 0){

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_list_top_recycler,parent,false);

            return new LocationListTopViewHolder(view);

        }
        else{

            View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_list_recycler,parent,false);
            return new LocationListViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof LocationListTopViewHolder){


        }

        else if(holder instanceof LocationListViewHolder){
            LocationListViewHolder locationListViewHolder = (LocationListViewHolder) holder;
/*
            ParkRowDTO parkRowData = parkRowDTOArrayList.get(position);
            CommentAveragePointDTO commentAveragePointDTO = commentAveragePointDTOArrayList.get(position-1);*/
            ParkListDTO parkListDTO = parkListDTOArrayList.get(position-1);

            if(parkListDTO.getImage_url().length()==0){

                //holder.mPicutureImageView.setImageResource(R.drawable.park_default_picture);
                Glide.with(mContext).load(R.drawable.park_default_picture).into(locationListViewHolder.mPicutureImageView);
            }

            else{

                Glide.with(mContext).load(parkListDTO.getImage_url())
                        .placeholder(R.drawable.placeholder)
                        .into(locationListViewHolder.mPicutureImageView);

            }

            locationListViewHolder.mLocationTitleTextView.setText(parkListDTO.getName());
            locationListViewHolder.mLocationAddressTextView.setText(parkListDTO.getAddress());
            locationListViewHolder.mLocationHeartTextView.setText(Float.toString(parkListDTO.getAvgStar()));
            locationListViewHolder.mLocationPetTextView.setText(Float.toString(parkListDTO.getAvgPet()));
            locationListViewHolder.mLocationDistanceTextView.setText(Double.toString(parkListDTO.getDistance()));

        }


    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        if(parkListDTOArrayList ==null) return 0;

        return parkListDTOArrayList.size();
    }

    public class LocationListViewHolder extends RecyclerView.ViewHolder{
        private ImageView mPicutureImageView;
        private TextView mLocationTitleTextView;
        private TextView mLocationAddressTextView;
        private TextView mLocationHeartTextView;
        private TextView mLocationPetTextView;
        private TextView mLocationDistanceTextView;
        private CardView mLocationListCardView;
        private Button mLocationFreeboardButton, mLocationCommentButton;



        public LocationListViewHolder(View itemView) {
            super(itemView);

            mPicutureImageView = (ImageView)itemView.findViewById(R.id.location_list_picture);
            mLocationTitleTextView = (TextView)itemView.findViewById(R.id.location_list_title);
            mLocationAddressTextView = (TextView)itemView.findViewById(R.id.location_list_address);
            mLocationHeartTextView = (TextView) itemView.findViewById(R.id.location_list_favorite_score);
            mLocationPetTextView = (TextView)itemView.findViewById(R.id.location_list_pet_score);
            mLocationDistanceTextView = (TextView)itemView.findViewById(R.id.location_list_distance_score);
            mLocationListCardView = (CardView)itemView.findViewById(R.id.location_list_cardView);
            mLocationFreeboardButton = (Button) itemView.findViewById(R.id.location_list_freeboard_button);
            mLocationCommentButton = (Button) itemView.findViewById(R.id.location_list_comment_button);



           // mLocationListCardView.setOnClickListener(onClickCardViewListener);
            mLocationFreeboardButton.setOnClickListener(onClickFreeboardButtonListener);
            mLocationCommentButton.setOnClickListener(onClickCommentButtonListener);

        }
        View.OnClickListener onClickCardViewListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mOnClickLocationListInterface.onClickList(getAdapterPosition());

            }
        };

        View.OnClickListener onClickFreeboardButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mOnClickLocationListInterface.onClickLocationFreeboard(getAdapterPosition());

            }
        };

        View.OnClickListener onClickCommentButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mOnClickLocationListInterface.onClickLocationComment(getAdapterPosition());

            }
        };


    }

    public class LocationListTopViewHolder extends RecyclerView.ViewHolder{

        private TextView mLocationListTopNear;
        private TextView mLocationListTopFav;
        private TextView mLocationListTopAnimal;

        public LocationListTopViewHolder(View itemView) {
            super(itemView);

            mLocationListTopNear = (TextView)itemView.findViewById(R.id.location_list_top_near);
            mLocationListTopFav = (TextView) itemView.findViewById(R.id.location_list_top_fav);
            mLocationListTopAnimal = (TextView) itemView.findViewById(R.id.location_list_top_animal);
            mLocationListTopNear.setTextColor(getColor(mContext,R.color.colorAccent));
            mLocationListTopNear.setOnClickListener(onClickNearListener);
            mLocationListTopFav.setOnClickListener(onClickFavListener);
            mLocationListTopAnimal.setOnClickListener(onClickAnimalListener);

        }


        View.OnClickListener onClickNearListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(parkListDTOArrayList, ComparatorUtil.sortByDistanceLocationList);
                setParkArrayList(parkListDTOArrayList);
                mLocationListTopNear.setTextColor(getColor(mContext,R.color.colorAccent));
                mLocationListTopFav.setTextColor(getColor(mContext,R.color.colorPrimaryDark));
                mLocationListTopAnimal.setTextColor(getColor(mContext,R.color.colorPrimaryDark));

            }
        };


        View.OnClickListener onClickFavListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Collections.sort(parkListDTOArrayList, ComparatorUtil.sortByFavInLocationList);
                setParkArrayList(parkListDTOArrayList);
                mLocationListTopNear.setTextColor(getColor(mContext,R.color.colorPrimaryDark));
                mLocationListTopFav.setTextColor(getColor(mContext,R.color.colorAccent));
                mLocationListTopAnimal.setTextColor(getColor(mContext,R.color.colorPrimaryDark));
            }
        };

        View.OnClickListener onClickAnimalListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(parkListDTOArrayList,ComparatorUtil.sortByAnimalLocationList);
                setParkArrayList(parkListDTOArrayList);
                mLocationListTopNear.setTextColor(getColor(mContext,R.color.colorPrimaryDark));
                mLocationListTopFav.setTextColor(getColor(mContext,R.color.colorPrimaryDark));
                mLocationListTopAnimal.setTextColor(getColor(mContext,R.color.colorAccent));

            }
        };

    }


    public int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= Build.VERSION_CODES.M) {
            return ContextCompat.getColor(context,id);
        } else {
            return context.getResources().getColor(id);
        }
    }


}
