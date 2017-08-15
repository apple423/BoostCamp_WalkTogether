package com.example.han.boostcamp_walktogether.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.han.boostcamp_walktogether.R;
import com.example.han.boostcamp_walktogether.data.FreeboardDTO;
import com.example.han.boostcamp_walktogether.data.FreeboardImageDTO;

import java.util.ArrayList;

/**
 * Created by Han on 2017-07-27.
 */
// 장소별 게시판의 게시글들을 보여주기 위한 RecyclerView의 어댑터
public class LocationFreeboardAdapter extends RecyclerView.Adapter<LocationFreeboardAdapter.LocationFreeboardViewHolder> {

    private OnClickLocationFreeboard mOnClickLocationFreeboard;
    private ArrayList<FreeboardDTO> mParkFreeboardList;
    private ArrayList<FreeboardImageDTO> mParkFreeboardImageList;
    private Context mContext;

    public LocationFreeboardAdapter(Context context,OnClickLocationFreeboard onClickLocationFreeboard){

        mContext = context;
       mOnClickLocationFreeboard = onClickLocationFreeboard;

    }

    public void setParkListAndImage(ArrayList<FreeboardDTO> list, ArrayList<FreeboardImageDTO> listImage){
        mParkFreeboardList = list;
        mParkFreeboardImageList = listImage;
        notifyDataSetChanged();

    }

//    public void setParkImageList(ArrayList<FreeboardImageDTO> listimage){
//        mParkFreeboardImageList = listimage;
//        notifyDataSetChanged();
//
//    }

    @Override
    public LocationFreeboardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_freeboard_recycler,parent,false);


        return new LocationFreeboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LocationFreeboardViewHolder holder, int position) {

        FreeboardDTO data = mParkFreeboardList.get(position);
        FreeboardImageDTO imageData = mParkFreeboardImageList.get(position);


        holder.mUserNameTextView.setText(data.getUser_name());
        holder.mTitleTextview.setText(data.getTitle());

            //TODO 9. null 처리리
          // Glide.with(mContext).load(data.getUser_profie()).into(mProfileImageView);

            if(data.getUser_profile()!=null){
                Glide.with(mContext).load(data.getUser_profile()).into(holder.mProfileImageView);
            }
            else{
                holder.mProfileImageView.setImageResource(R.mipmap.ic_launcher);
            }


            if(!imageData.getImage().equals("empty")) {
                Glide.with(mContext).load("http://" + imageData.getImage()).into(holder.mPicutreImageView);
            }
            else{

                holder.mPicutreImageView.setImageResource(R.drawable.park_default_picture);

            }


        }

    @Override
    public int getItemCount() {
        if(mParkFreeboardImageList == null) return 0;

        return mParkFreeboardImageList.size();
    }


    public class LocationFreeboardViewHolder extends RecyclerView.ViewHolder{
        private TextView mTitleTextview;
        private TextView mUserNameTextView;
        private ImageView mPicutreImageView;
        private ImageView mProfileImageView;
        private CardView mLocationFreeboardCardView;

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
                    mOnClickLocationFreeboard.onClickBoard(getAdapterPosition());
                }
            };

            mLocationFreeboardCardView.setOnClickListener(onClickListener);

        }
    }


}
