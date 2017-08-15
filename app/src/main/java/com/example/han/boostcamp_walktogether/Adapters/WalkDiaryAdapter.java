package com.example.han.boostcamp_walktogether.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.han.boostcamp_walktogether.R;

/**
 * Created by Han on 2017-08-15.
 */

public class WalkDiaryAdapter extends RecyclerView.Adapter {


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.walk_diary_recycler,parent,false);
        return new WalkDiaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class WalkDiaryViewHolder extends RecyclerView.ViewHolder{

        public WalkDiaryViewHolder(View itemView) {
            super(itemView);
        }
    }
}
