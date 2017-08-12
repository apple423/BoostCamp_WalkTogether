package com.example.han.boostcamp_walktogether.view;

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
import com.example.han.boostcamp_walktogether.R;
import com.example.han.boostcamp_walktogether.data.CommentAveragePointDTO;
import com.example.han.boostcamp_walktogether.data.ParkRowDTO;
import com.example.han.boostcamp_walktogether.util.RetrofitUtil;
import com.example.han.boostcamp_walktogether.util.StringKeys;

import org.parceler.Parcels;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private TextView mHeartTextView;
    private TextView mPetTextView;
    private int mParkKey;
    private String mLocationName;
    private RetrofitUtil retrofitUtil = RetrofitUtil.retrofit.create(RetrofitUtil.class);

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
        mHeartTextView = (TextView)findViewById(R.id.location_favorite_score);
        mPetTextView = (TextView)findViewById(R.id.location_pet_score);

        mLocationComment.setOnClickListener(onClickListener);
        mLocationFreeboard.setOnClickListener(onClickListener);
        Parcelable passedParcelLocationData = getIntent()
                .getParcelableExtra(StringKeys.LOCATION_INTENT_KEY);

        ParkRowDTO data = Parcels.unwrap(passedParcelLocationData);

        Log.d("chkPassedData",data.getName() + " "+data.getAddress());
        mLocationTextView.setText(data.getName());

        if(data.getImage_url().length()==0){
            mLocationImageView.setImageResource(R.drawable.park_default_picture);
        }else{
            Glide.with(this).load(data.getImage_url()).into(mLocationImageView);
        }

        mLocationAddressTextView.setText(data.getAddress());

        mParkKey = data.getPark_key();
        mLocationName = data.getName();
        Call<CommentAveragePointDTO> commentAveragePointDTOCall = retrofitUtil.getAveragePoint(mParkKey);
        commentAveragePointDTOCall.enqueue(commentAvaragePointCallback);

    }

    Button.OnClickListener onClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();

            switch (id){

                case R.id.location_button_location_comment :

                    Intent intentLocationComment = new Intent(mContext,LocationCommentActivity.class);
                    intentLocationComment.putExtra(StringKeys.LOCATION_ID_KEY,mParkKey);
                    intentLocationComment.putExtra(StringKeys.LOCATION_NAME,mLocationName);
                    startActivity(intentLocationComment);
                    break;

                case R.id.location_button_location_freeboard :
                    Intent intentLocationFreeboard = new Intent(mContext,LocationFreeboardActivity.class);
                    intentLocationFreeboard.putExtra(StringKeys.LOCATION_ID_KEY,mParkKey);

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

    Callback<CommentAveragePointDTO> commentAvaragePointCallback =  new Callback<CommentAveragePointDTO>() {
        @Override
        public void onResponse(Call<CommentAveragePointDTO> call, Response<CommentAveragePointDTO> response) {
            if(response.isSuccessful()){

                    Log.d("SuccessAverage","yes");
                    CommentAveragePointDTO commentAveragePointDTO = response.body();
                if(commentAveragePointDTO.getAvgPet()!= 0.0 && commentAveragePointDTO.getAvgStar()!= 0.0) {
                    mHeartTextView.setText(String.valueOf(commentAveragePointDTO.getAvgStar()));
                    mPetTextView.setText(String.valueOf(commentAveragePointDTO.getAvgPet()));
                }



            }else{

                Log.d("SuccessAverage","no");
            }

        }

        @Override
        public void onFailure(Call<CommentAveragePointDTO> call, Throwable t) {

            Log.d("SuccessAverage","fali  "+t.getMessage());

        }
    };
}
