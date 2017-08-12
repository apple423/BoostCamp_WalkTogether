package com.example.han.boostcamp_walktogether.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.boostcamp_walktogether.R;
import com.example.han.boostcamp_walktogether.data.CommentDTO;

import java.util.ArrayList;

/**
 * Created by Han on 2017-07-27.
 */
// 장소별 코멘트들을 보여주기 위한 RecyclerView 어댑터
public class LocationCommentAdapter extends RecyclerView.Adapter<LocationCommentAdapter.LocationCommentViewHolder> {


    private ArrayList<CommentDTO> mCommentArrayList;

    public void setmCommentArrayList(ArrayList<CommentDTO> mCommentArrayList) {
        this.mCommentArrayList = mCommentArrayList;
        notifyDataSetChanged();
    }

    @Override
    public LocationCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_comment_recycler,parent,false);

        return new LocationCommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LocationCommentViewHolder holder, int position) {

        CommentDTO commentDTO = mCommentArrayList.get(position);
        holder.mLocationUserNameTextView.setText(commentDTO.getUser_name());
        holder.mLocationHeartTextView.setText(String.valueOf(commentDTO.getStar_point()));
        holder.mLocationPetTextView.setText(String.valueOf(commentDTO.getPet_point()));
        holder.mLocationUserImageView.setImageResource(R.mipmap.ic_launcher);
        holder.mLocationContentTextView.setText(commentDTO.getComment());



    }

    @Override
    public int getItemCount() {

        if(mCommentArrayList==null) return 0;

        return mCommentArrayList.size();
    }

    public class LocationCommentViewHolder extends RecyclerView.ViewHolder{

        private ImageView mLocationUserImageView;
        private TextView mLocationUserNameTextView;
        private TextView mLocationContentTextView;
        private TextView mLocationHeartTextView;
        private TextView mLocationPetTextView;

        public LocationCommentViewHolder(View itemView) {
            super(itemView);
            mLocationUserImageView = (ImageView)itemView.findViewById(R.id.location_comment_user);
            mLocationUserNameTextView = (TextView) itemView.findViewById(R.id.location_commnet_user_name);
            mLocationContentTextView = (TextView) itemView.findViewById(R.id.location_comment_content);
            mLocationHeartTextView = (TextView) itemView.findViewById(R.id.location_comment_heart_textView);
            mLocationPetTextView = (TextView) itemView.findViewById(R.id.location_comment_pet_textView);

        }
    }
}
