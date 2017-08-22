package com.example.han.boostcamp_walktogether.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.han.boostcamp_walktogether.R;
import com.example.han.boostcamp_walktogether.data.FreeboardCommentDTO;
import com.example.han.boostcamp_walktogether.data.FreeboardDTO;
import com.example.han.boostcamp_walktogether.data.FreeboardImageDTO;
import com.example.han.boostcamp_walktogether.interfaces.OnClickFreeboardInterface;
import com.ldoublem.thumbUplib.ThumbUpView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Han on 2017-07-27.
 */
// 장소별 게시판의 게시글들을 보여주기 위한 RecyclerView의 어댑터
public class LocationFreeboardAdapter extends RecyclerView.Adapter<LocationFreeboardAdapter.LocationFreeboardViewHolder> {

    private OnClickFreeboardInterface mOnClickFreeboardInterface;
    private ArrayList<FreeboardDTO> mParkFreeboardList;
    private ArrayList<FreeboardImageDTO> mParkFreeboardImageList;
    private ArrayList<FreeboardDTO> mFreeboardLikeList;
    private ArrayList<FreeboardDTO> mFreeboardUserLikeList;
    private ArrayList<FreeboardCommentDTO> mFreeboardCommentList;
    private Context mContext;
    private Resources mResources;
    private boolean mLike;

    public LocationFreeboardAdapter(Context context, OnClickFreeboardInterface onClickFreeboardInterface, Resources resources){

        mContext = context;
       mOnClickFreeboardInterface = onClickFreeboardInterface;
        mResources =resources;

    }

    public void setParkListAndImage(ArrayList<FreeboardDTO> list, ArrayList<FreeboardImageDTO> listImage
    ,ArrayList<FreeboardDTO> freeboardLikeList, ArrayList<FreeboardDTO> freeboardLikeUserList,
                                    ArrayList<FreeboardCommentDTO> freeboardCommentList){
        mParkFreeboardList = list;
        mParkFreeboardImageList = listImage;
        mFreeboardLikeList = freeboardLikeList;
        mFreeboardUserLikeList = freeboardLikeUserList;
        mFreeboardCommentList = freeboardCommentList;
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
    public void onBindViewHolder(final LocationFreeboardViewHolder holder, final int position) {

        FreeboardDTO data = mParkFreeboardList.get(position);
        FreeboardImageDTO imageData = mParkFreeboardImageList.get(position);
        FreeboardDTO likeData = mFreeboardLikeList.get(position);
        FreeboardDTO userLikeData = mFreeboardUserLikeList.get(position);
        FreeboardCommentDTO commentDTO = mFreeboardCommentList.get(position);


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
                Glide.with(mContext).load("http://" + imageData.getImage()).placeholder(R.drawable.placeholder).into(holder.mPicutreImageView);
            }
            else{

                Glide.with(mContext).load(R.drawable.park_default_picture).placeholder(R.drawable.placeholder).into(holder.mPicutreImageView);
                //holder.mPicutreImageView.setImageResource(R.drawable.park_default_picture);

            }

            holder.mContentTextView.setText(data.getContent());



        if(userLikeData.getLike_count() == 0){

            holder.mLikeImageView.setUnlike();
            holder.mLikeCountTextView.setTextColor(Color.BLACK);

        }
        else{
            holder.mLikeImageView.setLike();
            holder.mLikeCountTextView.setTextColor(mResources.getColor(R.color.redText));
        }

        holder.mLikeCountTextView.setText(String.valueOf(likeData.getLike_count()));

        holder.mCommentCountTextView.setText(String.valueOf(commentDTO.getComment_count()));



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
        private CircleImageView mProfileImageView;
        private CardView mLocationFreeboardCardView;
        private TextView mContentTextView;
        private ThumbUpView mLikeImageView;
        private TextView mLikeCountTextView;
        private ImageView mCommmentImageView;
        private TextView mCommentCountTextView;

        public LocationFreeboardViewHolder(View itemView) {
            super(itemView);
            mLocationFreeboardCardView = (CardView)itemView.findViewById(R.id.location_freeboard_cardView);
            mTitleTextview = (TextView)itemView.findViewById(R.id.location_freeboard_title);
            mUserNameTextView = (TextView) itemView.findViewById(R.id.location_freeboard_user_name);
            mProfileImageView = (CircleImageView) itemView.findViewById(R.id.location_freeboard_user);
            mPicutreImageView=(ImageView)itemView.findViewById(R.id.location_freeboard_picture);
            mContentTextView = (TextView) itemView.findViewById(R.id.location_freeboard_content);
            mLikeImageView = (ThumbUpView) itemView.findViewById(R.id.location_freeboard_like_imageView);
            mLikeCountTextView = (TextView) itemView.findViewById(R.id.location_freeboard_like_textView);
            mCommmentImageView = (ImageView) itemView.findViewById(R.id.location_freeboard_comment_imageView);
            mCommentCountTextView = (TextView) itemView.findViewById(R.id.location_freeboard_comment_textView);


            mLocationFreeboardCardView.setOnClickListener(onClickListener);
            mCommmentImageView.setOnClickListener(onClickListener);
            mLikeImageView.setOnThumbUp(onThumbUp);

        }


        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();

                switch (id){
                    case R.id.location_freeboard_cardView :
                        mOnClickFreeboardInterface.onClickBoard(getAdapterPosition());
                        break;

                    case R.id.location_freeboard_comment_imageView :
                        mOnClickFreeboardInterface.onClickComment(getAdapterPosition());

                        break;

                }

            }
        };

        ThumbUpView.OnThumbUp onThumbUp =    new ThumbUpView.OnThumbUp() {
            @Override
            public void like(boolean like) {

                mOnClickFreeboardInterface.onClickLike(mLikeCountTextView,getAdapterPosition(),like);

            }

        };


            }


}


