package com.example.han.boostcamp_walktogether;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.han.boostcamp_walktogether.ActionBar.BackButtonActionBarActivity;
import com.example.han.boostcamp_walktogether.ActionBar.DrawerBaseActivity;
import com.example.han.boostcamp_walktogether.data.ParkDataFromFirebaseDTO;

import org.parceler.Parcels;

/**
 * Created by Han on 2017-07-26.
 */

public class LocationActivity extends BackButtonActionBarActivity {

    private Button mLocationComment;
    private Button mLocationFreeboard;
    private Context mContext;
    private ImageView mLocationImageView;
    private TextView mLocationTextView;
    private TextView mLocationAddressTextView;
    private String mLocationID;
    private final String LOCATION_ID_KEY ="LOCATION_ID";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext =this;

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_location, null, false);
        mFrameLayout.addView(contentView, 0);
        mTextView.setText(getResources().getString(R.string.location_activity_title));

        mLocationComment = (Button)findViewById(R.id.location_button_location_comment);
        mLocationFreeboard = (Button)findViewById(R.id.location_button_location_freeboard);
        mLocationImageView = (ImageView)findViewById(R.id.location_imageView);
        mLocationTextView = (TextView)findViewById(R.id.location_textView);
        mLocationAddressTextView = (TextView)findViewById(R.id.location_address_textView);

        mLocationComment.setOnClickListener(onClickListener);
        mLocationFreeboard.setOnClickListener(onClickListener);
        Parcelable passedParcelLocationData = getIntent()
                .getParcelableExtra(getResources().getString(R.string.location_intent_key));

        ParkDataFromFirebaseDTO data = Parcels.unwrap(passedParcelLocationData);

        Log.d("chkPassedData",data.getName() + " "+data.getAddress());
        mLocationTextView.setText(data.getName());
        Glide.with(this).load(data.getImageURL()).into(mLocationImageView);
        mLocationAddressTextView.setText(data.getAddress());
        mLocationID = data.getUid();

    }

    Button.OnClickListener onClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();

            switch (id){

                case R.id.location_button_location_comment :

                    Intent intentLocationComment = new Intent(mContext,LocationCommentActivity.class);
                    intentLocationComment.putExtra(LOCATION_ID_KEY,mLocationID);
                    startActivity(intentLocationComment);
                    break;

                case R.id.location_button_location_freeboard :
                    Intent intentLocationFreeboard = new Intent(mContext,LocationFreeboardActivity.class);
                    intentLocationFreeboard.putExtra(LOCATION_ID_KEY,mLocationID);
                    startActivity(intentLocationFreeboard);
                    break;

            }

        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case android.R.id.home :
                onBackPressed();
                return true;


        }
        return super.onOptionsItemSelected(item);
    }
}
