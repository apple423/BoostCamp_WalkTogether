package com.example.han.boostcamp_walktogether.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.han.boostcamp_walktogether.R;

/**
 * Created by Han on 2017-08-18.
 */

public class LocationFreeboardCommentDetailAdapter extends RecyclerView.Adapter<LocationFreeboardCommentDetailAdapter.LocationFreeboardCommentDetailViewHolder>{


    @Override
    public LocationFreeboardCommentDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.freeboard_comment_recycler,parent,false);

        return new LocationFreeboardCommentDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LocationFreeboardCommentDetailViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public class LocationFreeboardCommentDetailViewHolder extends RecyclerView.ViewHolder{


        public LocationFreeboardCommentDetailViewHolder(View itemView) {
            super(itemView);
        }
    }
}
