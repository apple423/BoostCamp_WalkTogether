package com.example.han.boostcamp_walktogether.widget;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.han.boostcamp_walktogether.R;
import com.example.han.boostcamp_walktogether.data.WalkDiaryDTO;
import com.example.han.boostcamp_walktogether.data.WalkDiaryImageDTO;
import com.example.han.boostcamp_walktogether.interfaces.OnClickWalkDiaryButtonInterface;
import com.example.han.boostcamp_walktogether.util.RetrofitUtil;
import com.example.han.boostcamp_walktogether.util.SharedPreferenceUtil;
import com.example.han.boostcamp_walktogether.util.StringKeys;
import com.kakao.network.response.ResponseBody;

import org.parceler.Parcels;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Han on 2017-08-16.
 */

public class WalkDiaryAddDialog extends DialogFragment {

    private int mDiaryKey;
    private Uri mUri;
    private File mFile;
    private String mUserEmail;
    private ImageView mMoveMapImageView;
    private EditText mContentEditText;
    private Button mWalkDiaryAddButton;
    private Button mWalkDiaryCancelButton;
    private ArrayList<WalkDiaryDTO> mWalkDiaryDTOArrayList;
    private ArrayList<WalkDiaryImageDTO> mWalkDiaryImageDTOArrayList;
    private OnClickWalkDiaryButtonInterface onClickWalkDiaryButtonInterface;
    private long mWalkTime;
    private float mWalkDistance;
    private final RetrofitUtil mRetrofitUtil = RetrofitUtil.retrofit.create(RetrofitUtil.class);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        onClickWalkDiaryButtonInterface = (OnClickWalkDiaryButtonInterface) context;

    }

    public static WalkDiaryAddDialog newInstance(Uri uri, String email, long time, float distance) {

        Bundle args = new Bundle();
        args.putParcelable(StringKeys.IMAGE_FILE_URI, Parcels.wrap(uri));
        args.putString(StringKeys.USER_EMAIL,email);
        args.putLong(StringKeys.WALK_DIARY_TAKING_TIME,time);
        args.putFloat(StringKeys.WALK_DIARY_DISTANCE,distance);
        WalkDiaryAddDialog fragment = new WalkDiaryAddDialog();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parcelable pacelableFile = getArguments().getParcelable(StringKeys.IMAGE_FILE_URI);
        String email = getArguments().getString(StringKeys.USER_EMAIL);
        mUri = Parcels.unwrap(pacelableFile);
        mUserEmail = email;
        mWalkTime = getArguments().getLong(StringKeys.WALK_DIARY_TAKING_TIME);
        mWalkDistance = getArguments().getFloat(StringKeys.WALK_DIARY_DISTANCE);
        mWalkDiaryImageDTOArrayList = new ArrayList<>();



    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_walk_diary_add,container,false);

        String str = mUri.toString().substring(7);
        Log.d("SubString",str);
        mFile = new File(str);
        Log.d("FileStringInDialog",mFile.toString());
        mMoveMapImageView = (ImageView) view.findViewById(R.id.walk_diary_add_map_move);
        mContentEditText = (EditText) view.findViewById(R.id.walk_diary_add_editText);
        mWalkDiaryAddButton = (Button) view.findViewById(R.id.walk_diary_add_post_button);
        mWalkDiaryCancelButton = (Button) view.findViewById(R.id.walk_diary_add_cancel_button);

        mWalkDiaryAddButton.setOnClickListener(onClickAddButton);
        mWalkDiaryCancelButton.setOnClickListener(onClickCancelButton);

        Glide.with(this)
                .load(mUri)
                .into(mMoveMapImageView);



        return view;


    }

    View.OnClickListener onClickAddButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            addWalkDiaryContent();

        }
    };


    View.OnClickListener onClickCancelButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            deleteImage();
        }
    };

    private void deleteImage() {
        mFile.delete();
        getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, mUri));
        dismiss();
    }


    public void addWalkDiaryContent(){

        WalkDiaryDTO walkDiaryDTO = new WalkDiaryDTO();
        walkDiaryDTO.setContent(mContentEditText.getText().toString());
        walkDiaryDTO.setUser_email(mUserEmail);
        walkDiaryDTO.setWalk_time(mWalkTime);
        walkDiaryDTO.setWalk_distance(mWalkDistance);

        Call<WalkDiaryDTO> walkDirayDTOCall = mRetrofitUtil.postWalkDiary(walkDiaryDTO);
        walkDirayDTOCall.enqueue(walkDiaryDTOCallback);

    }

    private void addPicture(int diaryKey,String userEmail) {

        Log.d("FileName",mFile.getName());
        Log.d("FiletoStringInDialog",mFile.toString());
        RequestBody reqFile = RequestBody.create(MediaType.parse("image*//*"), mFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", mFile.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), mFile.getName());
        Call<ResponseBody> postMapImageCall = mRetrofitUtil.postWalkDiaryMapImage(diaryKey,userEmail, body, name);
        postMapImageCall.enqueue(postMapImageCallback);
    }

    Callback<WalkDiaryDTO> walkDiaryDTOCallback = new Callback<WalkDiaryDTO>() {
        @Override
        public void onResponse(Call<WalkDiaryDTO> call, Response<WalkDiaryDTO> response) {

            if(response.isSuccessful()){
                mDiaryKey = response.body().getInsertId();

                addPicture(mDiaryKey,mUserEmail);

            }else{

                Log.d("WalkDiaryBad",response.message());
            }


        }

        @Override
        public void onFailure(Call<WalkDiaryDTO> call, Throwable t) {

            Log.d("WalkDiaryAddFail",t.getMessage());

        }
    };

    Callback<ResponseBody> postMapImageCallback = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if(response.isSuccessful()){

                Toast.makeText(getActivity(),
                        getResources().getString(R.string.walk_diary_add_success),
                        Toast.LENGTH_SHORT).show();
                Call<ArrayList<WalkDiaryDTO>> getWalkDiaryCall = mRetrofitUtil.getUsersWalkDiary(mUserEmail);
                getWalkDiaryCall.enqueue(getWalkDiaryCallback);

                      }

        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {

        }
    };


    Callback<ArrayList<WalkDiaryDTO>> getWalkDiaryCallback = new Callback<ArrayList<WalkDiaryDTO>>() {
        @Override
        public void onResponse(Call<ArrayList<WalkDiaryDTO>> call, Response<ArrayList<WalkDiaryDTO>> response) {
            if(response.isSuccessful()){

                mWalkDiaryDTOArrayList = response.body();
                Call<ArrayList<WalkDiaryImageDTO>> getWalkDiaryImageCall = mRetrofitUtil.getUserWalkDiaryImages(mUserEmail);
                getWalkDiaryImageCall.enqueue(getWalkDiaryImageCallback);
            }

        }

        @Override
        public void onFailure(Call<ArrayList<WalkDiaryDTO>> call, Throwable t) {

        }
    };

    Callback<ArrayList<WalkDiaryImageDTO>> getWalkDiaryImageCallback  = new Callback<ArrayList<WalkDiaryImageDTO>>() {
        @Override
        public void onResponse(Call<ArrayList<WalkDiaryImageDTO>> call, Response<ArrayList<WalkDiaryImageDTO>> response) {
            if(response.isSuccessful()){

                mWalkDiaryImageDTOArrayList = response.body();
                onClickWalkDiaryButtonInterface.onClickAddDiaryButton(mWalkDiaryDTOArrayList,mWalkDiaryImageDTOArrayList);
                deleteImage();
            }

        }

        @Override
        public void onFailure(Call<ArrayList<WalkDiaryImageDTO>> call, Throwable t) {

        }
    };
}
