package com.example.han.boostcamp_walktogether.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.han.boostcamp_walktogether.R;
import com.example.han.boostcamp_walktogether.data.WalkDiaryDTO;
import com.example.han.boostcamp_walktogether.data.WalkDiaryImageDTO;
import com.example.han.boostcamp_walktogether.interfaces.OnClickWalkDiaryDeleteInterface;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import me.wangyuwei.flipshare.FlipShareView;
import me.wangyuwei.flipshare.ShareItem;

/**
 * Created by Han on 2017-08-15.
 */

public class WalkDiaryAdapter extends RecyclerView.Adapter<WalkDiaryAdapter.WalkDiaryViewHolder> {

    private ArrayList<WalkDiaryDTO> walkDiaryDTOArrayList;
    private ArrayList<WalkDiaryImageDTO> walkDiaryImageDTOArrayList;
    private Context mContext;
    private Resources mResouces;
    private OnClickWalkDiaryDeleteInterface mOnClickWalkDiaryDeleteInterface;

    public WalkDiaryAdapter(Context context, Resources resources,
                            OnClickWalkDiaryDeleteInterface onClickWalkDiaryDeleteInterface){
        mContext =context;
        mResouces = resources;
        mOnClickWalkDiaryDeleteInterface = onClickWalkDiaryDeleteInterface;

    }


    @Override
    public WalkDiaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.walk_diary_recycler,parent,false);
        return new WalkDiaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WalkDiaryViewHolder holder, int position) {

        WalkDiaryDTO walkDiaryDTO = walkDiaryDTOArrayList.get(position);
        WalkDiaryImageDTO walkDiaryImageDTO = walkDiaryImageDTOArrayList.get(position);

        long time = walkDiaryDTO.getDate().getTime() - walkDiaryDTO.getWalk_time();
        Date date = new Date(time);
        String dateString = new SimpleDateFormat(mResouces.getString(R.string.walk_diary_time_title_format)
                , Locale.KOREA)
                .format(date);
        holder.mDateTextView.setText(dateString);

        Glide.with(mContext).load(mResouces.getString(R.string.http)
                +walkDiaryImageDTO.getImage_url())
                .into(holder.mMapMoveImageView);

        holder.mContentTextView.setText(walkDiaryDTO.getContent());
        String walkingTime = convertSecondsToHMmSs(walkDiaryDTO.getWalk_time());
        holder.mWalkingTime.setText(walkingTime);
        String walkingDistance = covertDistance(walkDiaryDTO.getWalk_distance());
        holder.mWalkingDistance.setText(walkingDistance);


    }


    public void setWalkDiaryDTOArrayLists(ArrayList<WalkDiaryDTO> walkDiaryDTOArrayList,
                                         ArrayList<WalkDiaryImageDTO> walkDiaryImageDTOArrayList) {
        this.walkDiaryDTOArrayList = walkDiaryDTOArrayList;
        this.walkDiaryImageDTOArrayList = walkDiaryImageDTOArrayList;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        if(walkDiaryDTOArrayList ==null || walkDiaryDTOArrayList.size()==0) return 0;

        return walkDiaryDTOArrayList.size();
    }

    public class WalkDiaryViewHolder extends RecyclerView.ViewHolder{

        private TextView mDateTextView;
        private ImageView mMapMoveImageView;
        private TextView mContentTextView;
        private TextView mWalkingTime;
        private TextView mWalkingDistance;
        private ImageButton mDeleteImageButton;
        private ImageButton mShareImageButton;

        public WalkDiaryViewHolder(View itemView) {
            super(itemView);

            mDateTextView = (TextView) itemView.findViewById(R.id.walk_diary_time);
            mMapMoveImageView = (ImageView) itemView.findViewById(R.id.walk_diary_move_map);
            mContentTextView = (TextView) itemView.findViewById(R.id.walk_diary_content_textView);
            mWalkingTime = (TextView) itemView.findViewById(R.id.walk_diary_walking_time_textView);
            mWalkingDistance = (TextView) itemView.findViewById(R.id.walk_diary_walking_distance_textView);
            mShareImageButton = (ImageButton) itemView.findViewById(R.id.walk_diary_share_imageButton);
            mDeleteImageButton = (ImageButton) itemView.findViewById(R.id.walk_diary_delete_imageButton);


            mShareImageButton.setOnClickListener(onClickShareImageListener);
            mDeleteImageButton.setOnClickListener(onClickDeleteImageListener);

        }


        View.OnClickListener onClickShareImageListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlipShareView share = new FlipShareView.Builder((Activity) mContext,mShareImageButton)
                        .addItem(new ShareItem("SMS"))
                        .addItem(new ShareItem("KAKAO"))
                        .setBackgroundColor(0x60000000)
                        .create();

                share.setOnFlipClickListener(new FlipShareView.OnFlipClickListener() {
                    @Override
                    public void onItemClick(int position) {

                        switch (position){

                            case 0 : mOnClickWalkDiaryDeleteInterface.onClickShareSMSButton(getAdapterPosition());
                                break;

                            case 1 : mOnClickWalkDiaryDeleteInterface.onClickShareKakaoButton(getAdapterPosition());
                                break;

                        }
                    }

                    @Override
                    public void dismiss() {

                    }
                });


            }
        };

        View.OnClickListener onClickDeleteImageListener = new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                mOnClickWalkDiaryDeleteInterface.onClickDeleteButton(getAdapterPosition());
            }
        };

        public ImageView getmMapMoveImageView(){

            return mMapMoveImageView;
        }


    }



    public  String convertSecondsToHMmSs(long seconds) {
        long mileSecond = seconds/1000;
        long s = mileSecond  % 60 ;
        long m = (mileSecond  / 60) % 60 ;
        long h = (mileSecond  / (60 * 60)) % 24;
        String timeFormat = mResouces.getString(R.string.walk_diary_walking_time_format);
        return String.format(timeFormat, h,m,s);
    }

    public String covertDistance(float distance){

        float distanceKM = distance/1000;
        String distanceFormat = mResouces.getString(R.string.walk_diary_walking_distance_format);
        return String.format(distanceFormat,distanceKM);
    }



}
