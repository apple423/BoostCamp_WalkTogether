package com.example.han.boostcamp_walktogether.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.han.boostcamp_walktogether.R;

/**
 * Created by Han on 2017-08-18.
 */

public class LocationFreeboardCommentAdapter extends RecyclerView.Adapter<LocationFreeboardCommentAdapter.LocationFreeboardCommentViewHolder> {


    @Override
    public LocationFreeboardCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.freeboard_comment_recycler,parent,false);

        return new LocationFreeboardCommentViewHolder(view);

    }

    @Override
    public void onBindViewHolder(LocationFreeboardCommentViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class LocationFreeboardCommentViewHolder extends RecyclerView.ViewHolder{


        public LocationFreeboardCommentViewHolder(View itemView) {
            super(itemView);
        }
    }
}
