package com.example.han.boostcamp_walktogether.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.han.boostcamp_walktogether.R;
import com.example.han.boostcamp_walktogether.data.CommentDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Han on 2017-07-27.
 */
// 장소별 코멘트들을 보여주기 위한 RecyclerView 어댑터
public class LocationCommentAdapter extends RecyclerView.Adapter<LocationCommentAdapter.LocationCommentViewHolder> {


    private ArrayList<CommentDTO> mCommentArrayList;
    private Context mContext;
    private Resources mResources;

    public LocationCommentAdapter(Context mContext, Resources resources) {
        this.mContext = mContext;
        mResources = resources;
    }

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
        if(commentDTO.getUser_profile().length()==0){
            Glide.with(mContext).load(R.mipmap.ic_launcher).into(holder.mLocationUserImageView);
        }
        else{

            Glide.with(mContext).load(commentDTO.getUser_profile()).into(holder.mLocationUserImageView);
        }
        //holder.mLocationUserImageView.setImageResource(R.mipmap.ic_launcher);
        holder.mLocationContentTextView.setText(commentDTO.getComment());

        String dateString = new SimpleDateFormat(mResources.getString(R.string.comment_submit_time)
                , Locale.KOREA)
                .format(commentDTO.getDate());

        holder.mLocationCommentDate.setText(dateString);



    }

    @Override
    public int getItemCount() {

        if(mCommentArrayList==null) return 0;

        return mCommentArrayList.size();
    }

    public class LocationCommentViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView mLocationUserImageView;
        private TextView mLocationUserNameTextView;
        private TextView mLocationContentTextView;
        private TextView mLocationHeartTextView;
        private TextView mLocationPetTextView;
        private TextView mLocationCommentDate;

        public LocationCommentViewHolder(View itemView) {
            super(itemView);
            mLocationUserImageView = (CircleImageView) itemView.findViewById(R.id.location_comment_user);
            mLocationUserNameTextView = (TextView) itemView.findViewById(R.id.location_commnet_user_name);
            mLocationContentTextView = (TextView) itemView.findViewById(R.id.location_comment_content);
            mLocationHeartTextView = (TextView) itemView.findViewById(R.id.location_comment_heart_textView);
            mLocationPetTextView = (TextView) itemView.findViewById(R.id.location_comment_pet_textView);
            mLocationCommentDate = (TextView) itemView.findViewById(R.id.location_comment_time_textView);

        }
    }
}
