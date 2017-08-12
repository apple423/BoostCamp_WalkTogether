package com.example.han.boostcamp_walktogether.view.detail;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.han.boostcamp_walktogether.ActionBar.BackButtonActionBarActivity;
import com.example.han.boostcamp_walktogether.Adapters.LocationFreeboardAddPictureAdapter;
import com.example.han.boostcamp_walktogether.R;
import com.example.han.boostcamp_walktogether.data.FreeboardDTO;
import com.example.han.boostcamp_walktogether.data.FreeboardImageDTO;
import com.example.han.boostcamp_walktogether.helper.FirebaseHelper;
import com.example.han.boostcamp_walktogether.util.RetrofitUtil;
import com.example.han.boostcamp_walktogether.util.SharedPreferenceUtil;
import com.example.han.boostcamp_walktogether.util.StringKeys;
import com.example.han.boostcamp_walktogether.view.LocationFreeboardActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.UploadTask;
import com.kakao.auth.ErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.network.response.ResponseBody;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.helper.log.Logger;

import org.parceler.Parcels;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.han.boostcamp_walktogether.util.StringKeys.USER_EMAIL;
import static com.example.han.boostcamp_walktogether.util.StringKeys.USER_NICK_NAME;
import static com.example.han.boostcamp_walktogether.util.StringKeys.USER_PROFILE;
import static com.example.han.boostcamp_walktogether.util.StringKeys.USER_PROFILE_PICTURE;

/**
 * Created by Han on 2017-07-28.
 */

public class LocationFreeboardAddActivity extends BackButtonActionBarActivity{

    private static final int PICK_IMAGE_MULTIPLE = 100;
    private static final int PUSH_ADD_BUTTON = 101;
    RetrofitUtil retrofitUtil = RetrofitUtil.retrofit.create(RetrofitUtil.class);
    private Context mContext;
    private Activity mActivity;
    private RecyclerView pictureRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private LocationFreeboardAddPictureAdapter locationFreeboardAddPictureAdapter;
    private int mParkKey;
    private EditText mLocationFreeboardAddTitleEditText;
    private EditText mLocationFreeboardAddContentEditText;
    private ImageView mLocationFreeboardAddPicutreImageView;
    private Button mLocationFreeboardAddButton;
    private boolean isFirstAddPicture = true;
    private ArrayList<Uri> mArrayUri;
    private ArrayList<String> mDownLoadUrlPicture;
    private File mFile;
    private int mFreeboardKey;
    private ArrayList<FreeboardDTO> mParkFreeboardList;
    private ArrayList<FreeboardImageDTO> mParkFreeboardImageList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_location_freeboard_add, null, false);
        mFrameLayout.addView(contentView, 0);
        mTextView.setText(getResources().getString(R.string.location_freeboard_add_activity_title));

        mArrayUri = new ArrayList<Uri>();
        mDownLoadUrlPicture = new ArrayList<>();
        mParkFreeboardImageList = new ArrayList<>();
        mContext = this;
        mActivity = this;

        mLocationFreeboardAddTitleEditText = (EditText) findViewById(R.id.location_freeboard_add_title_editText);
        mLocationFreeboardAddContentEditText = (EditText) findViewById(R.id.location_freeboard_add_content_editText);
        mLocationFreeboardAddPicutreImageView = (ImageView)findViewById(R.id.location_freeboard_add_picture_imageView);
        mLocationFreeboardAddButton = (Button) findViewById(R.id.location_freeboard_add_button);
        pictureRecyclerView = (RecyclerView)findViewById(R.id.location_freeboard_add_picture_recyclerView);

        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        locationFreeboardAddPictureAdapter = new LocationFreeboardAddPictureAdapter();
        pictureRecyclerView.setLayoutManager(linearLayoutManager);
        pictureRecyclerView.setAdapter(locationFreeboardAddPictureAdapter);

        mParkKey = getIntent().getIntExtra(StringKeys.LOCATION_ID_KEY,0);
        mLocationFreeboardAddPicutreImageView.setOnClickListener(onClickAddImageViewListener);
        mLocationFreeboardAddButton.setOnClickListener(onClickAddButton);


    }

    // + 이미지뷰를 클릭시 갤러리에서 사진을 선택하도록
    View.OnClickListener onClickAddImageViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            try {
                if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_IMAGE_MULTIPLE);

                } else {

                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    galleryIntent.setType("image/*");
                    startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"),PICK_IMAGE_MULTIPLE);

                }
            } catch (Exception e) {

            }

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK) {

            if(data.getClipData()!=null) {

                ClipData mClipData = data.getClipData();

                for (int i = 0; i < mClipData.getItemCount(); i++) {

                    ClipData.Item item = mClipData.getItemAt(i);
                    Uri uri = item.getUri();
                    mArrayUri.add(uri);
                    Log.d("chkUri", uri.toString());

                }


            }else if(data.getData()!=null){

                Uri uri = data.getData();
                mArrayUri.add(uri);

            }

            locationFreeboardAddPictureAdapter.setImageUriArrayList(mArrayUri);
        }

    }
    // 등록버튼 클릭시 게시물을 등록
    View.OnClickListener onClickAddButton = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            showProgressBar();
            // freeboardDTO에 게시글 정보 추가
            FreeboardDTO dto = getFreeboardDTO();
            Call<FreeboardDTO> freeboardAddCall  = retrofitUtil.postFreeboard(mParkKey,dto);
            freeboardAddCall.enqueue(addFreeboardCallback);

        }
    };


    private FreeboardDTO getFreeboardDTO() {
        FreeboardDTO dto = new FreeboardDTO();
        // TODO 9. 파이어베이스 user에서 정보를 가져올 수 있기 때문에 처음 로그인 시 shraedPreference에 저장해서 쓰는 방법을 구현할 예정
        // 추후에 카카오톡 로그인을 파이어베이스 user에 추가하면 더 관리가 쉬워질 것이라 생각합니다.
        SharedPreferenceUtil.setKaKaoCheckSharedPreference(mContext,USER_PROFILE,MODE_PRIVATE);
        String userID = SharedPreferenceUtil.getUserProfile(USER_EMAIL);
        String userProfileImageURL = SharedPreferenceUtil.getUserProfile(USER_PROFILE_PICTURE);
        String userNickName = SharedPreferenceUtil.getUserProfile(USER_NICK_NAME);
        String title = mLocationFreeboardAddTitleEditText.getText().toString();
        String content = mLocationFreeboardAddContentEditText.getText().toString();

        dto.setUser_name(userNickName);
        dto.setUser_id(userID);
        dto.setUser_profie(userProfileImageURL);
        dto.setTitle(title);
        dto.setContent(content);
        return dto;
    }

    Callback<ResponseBody> addPictureCallback = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if(response.isSuccessful()){
                hideProgressBar();
                Call<ArrayList<FreeboardDTO>> getfreeboardInParkCall = retrofitUtil.getAllFreeboard(mParkKey);
                getfreeboardInParkCall.enqueue(getFreeboardInParkCallback);
                Log.d("imageUploadSuccess",response.message());
                Log.d("imageUploadSuccess","yesman");

            }
            else{

                Log.d("resonseMessage",response.message());
            }

        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {

            hideProgressBar();
            Call<ArrayList<FreeboardDTO>> getfreeboardInParkCall = retrofitUtil.getAllFreeboard(mParkKey);
            getfreeboardInParkCall.enqueue(getFreeboardInParkCallback);

            Log.d("imageUploadFail",t.getMessage());

        }
    };

   Callback<FreeboardDTO> addFreeboardCallback = new Callback<FreeboardDTO>() {
        @Override
        public void onResponse(Call<FreeboardDTO> call, Response<FreeboardDTO> response) {
            if(response.isSuccessful()){

                // 서버에서 응답을 가져와 사진추가의 파라메터로 사용하기 위해 넣은다.
                mFreeboardKey = response.body().getInsertId();
                if(mArrayUri.size()!=0) {
                    for (Uri uri : mArrayUri) {

                        addPicture(uri);

                    }

                }else{

                    FreeboardImageDTO freeboardImageDTO = new FreeboardImageDTO();
                    freeboardImageDTO.setFreeboard_key(mFreeboardKey);
                    freeboardImageDTO.setPark_key(mParkKey);
                    freeboardImageDTO.setImage("empty");
                    Call<FreeboardImageDTO> addEmptyImageCall = retrofitUtil.postEmptyImage(freeboardImageDTO);
                    addEmptyImageCall.enqueue(new Callback<FreeboardImageDTO>() {
                        @Override
                        public void onResponse(Call<FreeboardImageDTO> call, Response<FreeboardImageDTO> response) {
                            if(response.isSuccessful()){

                                Call<ArrayList<FreeboardDTO>> getfreeboardInParkCall = retrofitUtil.getAllFreeboard(mParkKey);
                                getfreeboardInParkCall.enqueue(getFreeboardInParkCallback);

                            }


                        }

                        @Override
                        public void onFailure(Call<FreeboardImageDTO> call, Throwable t) {

                        }
                    });

                    hideProgressBar();

                }

            }
        }

        @Override
        public void onFailure(Call<FreeboardDTO> call, Throwable t) {
            Log.d("insertID_fail",t.getMessage());

        }
    };

    private void addPicture(Uri uri) {

        String imagePath = getPathFromURI(uri);
        mFile = new File(imagePath);
        RequestBody reqFile = RequestBody.create(MediaType.parse("image*//*"), mFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", mFile.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), mFile.getName());
        Call<ResponseBody> addPicutreResult = retrofitUtil.postFreeboardImgage(mParkKey, mFreeboardKey, body, name);
        addPicutreResult.enqueue(addPictureCallback);
    }

    // gallery에서 가져온 사진을 String Path로 변환
    private String getPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    Callback<ArrayList<FreeboardDTO>> getFreeboardInParkCallback = new Callback<ArrayList<FreeboardDTO>>() {
        @Override
        public void onResponse(Call<ArrayList<FreeboardDTO>> call, Response<ArrayList<FreeboardDTO>> response) {
            if(response.isSuccessful()){

                ArrayList<FreeboardDTO> freeboardList= response.body();

                mParkFreeboardList = freeboardList;
                Log.d("send_freeboard", "gogogogoo");
                //mLocationFreeboardAdapter.setParkList(mParkFreeboardList);
                for(FreeboardDTO data : freeboardList){
                    int freeboardKey = data.getFreeboard_key();

                    Call<FreeboardImageDTO> getFreeboardImageCall = retrofitUtil.getOneImageFreeboard(mParkKey,freeboardKey);
                    getFreeboardImageCall.enqueue(getFreeboardImageCallback);
                }
            }

        }

        @Override
        public void onFailure(Call<ArrayList<FreeboardDTO>> call, Throwable t) {

        }
    };

    Callback<FreeboardImageDTO> getFreeboardImageCallback = new Callback<FreeboardImageDTO>() {


        @Override
        public void onResponse(Call<FreeboardImageDTO> call, Response<FreeboardImageDTO> response) {
            if (response.isSuccessful()) {

                FreeboardImageDTO freeboardImageData = response.body();
                Log.d("send_Image_adapter", freeboardImageData.getImage());
                mParkFreeboardImageList.add(freeboardImageData);
                if (mParkFreeboardImageList.size() == mParkFreeboardList.size()) {
                    // LocationFreeboardActivity에 데이터 전달
                    Log.d("successInSetResult",mParkFreeboardList.get(0).getTitle());
                    resultToLocationActivity();
                  //  Log.d("send_Image_adapter", "gogogogoo");
            }
            }

            else{

                Log.d("send_Image_adapter", "fail");
            }
        }

        @Override
        public void onFailure(Call<FreeboardImageDTO> call, Throwable t) {

            Log.d("send_Image_adapter",t.getMessage());
            Log.d("send_Image_adapter", "gogogogoofail");

        }
    };

    private void resultToLocationActivity() {
        Intent intent = new Intent();
        intent.putExtra("parkList", Parcels.wrap( mParkFreeboardList));
        intent.putExtra("parkImageList", Parcels.wrap( mParkFreeboardImageList));
        mActivity.setResult(RESULT_OK, intent);
        finish();
    }


}
