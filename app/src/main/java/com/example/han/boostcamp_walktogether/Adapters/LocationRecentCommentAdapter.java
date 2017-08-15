package com.example.han.boostcamp_walktogether.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.boostcamp_walktogether.R;
import com.example.han.boostcamp_walktogether.data.CommentDTO;
import com.example.han.boostcamp_walktogether.data.RecentCommentDTO;

import java.util.ArrayList;

/**
 * Created by Han on 2017-08-13.
 * 최신 장소 리뷰들을 보여주기 위한 리사이클러 뷰 어뎁터
 */

public class LocationRecentCommentAdapter extends RecyclerView.Adapter<LocationRecentCommentAdapter.LocationRecentCommentViewHolder> {

    private ArrayList<RecentCommentDTO> commentDTOArrayList;

    public void setCommentDTOArrayList(ArrayList<RecentCommentDTO> commentDTOArrayList) {
        this.commentDTOArrayList = commentDTOArrayList;
        notifyDataSetChanged();
    }

    @Override
    public LocationRecentCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_comment_recycler,parent,false);
        return new LocationRecentCommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LocationRecentCommentViewHolder holder, int position) {
        RecentCommentDTO recentCommentDTO = commentDTOArrayList.get(position);
        holder.mCommentTitle.setText(recentCommentDTO.getName());
        holder.mCommnetAddress.setText(recentCommentDTO.getAddress());
        holder.mComentContent.setText(recentCommentDTO.getComment());
        holder.mHeartPoint.setText(Float.toString(recentCommentDTO.getStar_point()));
        holder.mPetPoint.setText(Float.toString(recentCommentDTO.getPet_point()));


    }

    @Override
    public int getItemCount() {
        if(commentDTOArrayList == null) return 0;
        return commentDTOArrayList.size();
    }

    public class LocationRecentCommentViewHolder extends RecyclerView.ViewHolder{
        TextView mCommentTitle;
        TextView mCommnetAddress;
        TextView mComentContent;
        TextView mHeartPoint;
        TextView mPetPoint;


        public LocationRecentCommentViewHolder(View itemView) {
            super(itemView);
            mComentContent = (TextView)itemView.findViewById(R.id.location_recent_comment_content);
            mCommnetAddress = (TextView)itemView.findViewById(R.id.location_recent_comment_address);
            mCommentTitle = (TextView)itemView.findViewById(R.id.location_recent_comment_title);
            mHeartPoint = (TextView)itemView.findViewById(R.id.location_recent_comment_heart_textView);
            mPetPoint = (TextView)itemView.findViewById(R.id.location_recent_comment_pet_textView);


        }
    }
}
