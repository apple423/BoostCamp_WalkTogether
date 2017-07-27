package com.example.han.boostcamp_walktogether.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.boostcamp_walktogether.R;

/**
 * Created by Han on 2017-07-27.
 */

public class LocationCommentAdapter extends RecyclerView.Adapter<LocationCommentAdapter.LocationCommentViewHolder> {

    private TextView mLocationTitleTextview;


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
        return 0;
    }

    public class LocationCommentViewHolder extends RecyclerView.ViewHolder{


        public LocationCommentViewHolder(View itemView) {
            super(itemView);

        }
    }
}
