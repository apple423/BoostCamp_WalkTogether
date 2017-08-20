package com.example.han.boostcamp_walktogether.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.han.boostcamp_walktogether.R;
import com.example.han.boostcamp_walktogether.data.FreeboardCommentDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Han on 2017-08-18.
 */

public class LocationFreeboardCommentAdapter extends RecyclerView.Adapter<LocationFreeboardCommentAdapter.LocationFreeboardCommentViewHolder> {

    private Context mContext;
    private Resources mResources;
    private ArrayList<FreeboardCommentDTO> freeboardCommentDTOArrayList;

    public void setFreeboardCommentDTOArrayList(ArrayList<FreeboardCommentDTO> freeboardCommentDTOArrayList) {
        this.freeboardCommentDTOArrayList = freeboardCommentDTOArrayList;
        notifyDataSetChanged();
    }

    public LocationFreeboardCommentAdapter(Context context,Resources resources) {
        this.mContext = context;
        this.mResources = resources;
    }
    @Override
    public LocationFreeboardCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.freeboard_comment_recycler,parent,false);

        return new LocationFreeboardCommentViewHolder(view);

    }

    @Override
    public void onBindViewHolder(LocationFreeboardCommentViewHolder holder, int position) {

        FreeboardCommentDTO freeboardCommentDTO = freeboardCommentDTOArrayList.get(position);

        holder.mUserNameTextView.setText(freeboardCommentDTO.getUser_name());

        Glide.with(mContext).load(freeboardCommentDTO.getUser_profile()).into(holder.mUserProfileImageView);

        holder.mCommentTextView.setText(freeboardCommentDTO.getComment());
        String dateString = new SimpleDateFormat(mResources.getString(R.string.comment_submit_time)
                , Locale.KOREA)
                .format(freeboardCommentDTO.getDate());

        holder.mDateTextView.setText(dateString);
    }

    @Override
    public int getItemCount() {
        if(freeboardCommentDTOArrayList == null || freeboardCommentDTOArrayList.size()==0) return 0;
        return freeboardCommentDTOArrayList.size();
    }

    public class LocationFreeboardCommentViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView mUserProfileImageView;
        private TextView mUserNameTextView;
        private TextView mDateTextView;
        private TextView mCommentTextView;


        public LocationFreeboardCommentViewHolder(View itemView) {
            super(itemView);


            mUserProfileImageView = (CircleImageView)itemView.findViewById(R.id.location_freeboard_comment_user_imageView);
            mUserNameTextView = (TextView)itemView.findViewById(R.id.location_freeboard_comment_user_name);
            mDateTextView = (TextView) itemView.findViewById(R.id.location_freeboard_comment_time_textView);
            mCommentTextView = (TextView) itemView.findViewById(R.id.location_freeboard_comment_content);
        }
    }
}
