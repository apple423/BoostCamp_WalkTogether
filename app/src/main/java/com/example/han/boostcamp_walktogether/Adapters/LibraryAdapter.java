package com.example.han.boostcamp_walktogether.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.boostcamp_walktogether.R;

/**
 * Created by Han on 2017-08-24.
 */

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.LibraryViewHolder> {


    String[] titles;
    String[] urls;
    String[] copyrights;
    String[] licenses;

    public void setLibrary(String[] titles, String[] urls, String[] copyrights, String[] licenses){
        this.titles = titles;
        this.urls = urls;
        this.copyrights = copyrights;
        this.licenses = licenses;
    }
    @Override
    public LibraryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.library_recycler,parent,false);
        return new LibraryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LibraryViewHolder holder, int position) {
        holder.mTitle.setText(titles[position]);
        holder.mUrl.setText(urls[position]);
        holder.mCopyright.setText(copyrights[position]);
        holder.mLicense.setText(licenses[position]);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    public class LibraryViewHolder extends RecyclerView.ViewHolder{

        TextView mTitle;
        TextView mUrl;
        TextView mCopyright;
        TextView mLicense;

        public LibraryViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView)itemView.findViewById(R.id.library_title_textView);
            mUrl = (TextView)itemView.findViewById(R.id.library_url_textView);
            mCopyright = (TextView) itemView.findViewById(R.id.library_copyright_textView);
            mLicense = (TextView) itemView.findViewById(R.id.library_license_textView);

        }
    }
}
