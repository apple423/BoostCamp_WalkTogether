package com.example.han.boostcamp_walktogether.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.boostcamp_walktogether.R;

/**
 * Created by Han on 2017-07-27.
 */

public class LocationCommentAdapter extends RecyclerView.Adapter<LocationCommentAdapter.LocationCommentViewHolder> {

    private ImageView mLocationUserImageView;
    private TextView mLocationUserNameTextView;
    private TextView mLocationContentTextView;
    private TextView mLocationHeartTextView;
    private TextView mLocationPetTextView;


    @Override
    public LocationCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_comment_recycler,parent,false);

        return new LocationCommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LocationCommentViewHolder holder, int position) {



    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class LocationCommentViewHolder extends RecyclerView.ViewHolder{


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
