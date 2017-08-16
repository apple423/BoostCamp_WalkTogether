package com.example.han.boostcamp_walktogether.widget;

import android.app.DialogFragment;
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
import com.example.han.boostcamp_walktogether.util.RetrofitUtil;
import com.example.han.boostcamp_walktogether.util.SharedPreferenceUtil;
import com.example.han.boostcamp_walktogether.util.StringKeys;
import com.kakao.network.response.ResponseBody;

import org.parceler.Parcels;

import java.io.File;
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
    private final RetrofitUtil mRetrofitUtil = RetrofitUtil.retrofit.create(RetrofitUtil.class);


    public static WalkDiaryAddDialog newInstance(Uri uri,String email) {

        Bundle args = new Bundle();
        args.putParcelable(StringKeys.IMAGE_FILE_URI, Parcels.wrap(uri));
        args.putString(StringKeys.USER_EMAIL,email);
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
                deleteImage();

            }

        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {

        }
    };
}
