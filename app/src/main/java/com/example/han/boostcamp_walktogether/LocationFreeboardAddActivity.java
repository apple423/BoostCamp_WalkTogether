package com.example.han.boostcamp_walktogether;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.han.boostcamp_walktogether.ActionBar.BackButtonActionBarActivity;
import com.example.han.boostcamp_walktogether.Adapters.LocationFreeboardAddPictureAdapter;
import com.example.han.boostcamp_walktogether.helper.FirebaseHelper;
import com.example.han.boostcamp_walktogether.util.SharedPreferenceUtil;
import com.example.han.boostcamp_walktogether.util.StringKeys;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.UploadTask;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.helper.log.Logger;

import java.util.ArrayList;

import static com.example.han.boostcamp_walktogether.util.StringKeys.KAKAO_DATA_SEND_CHECK;
import static com.example.han.boostcamp_walktogether.util.StringKeys.KAKAO_SHARED_PREFERENCES;

/**
 * Created by Han on 2017-07-28.
 */

public class LocationFreeboardAddActivity extends BackButtonActionBarActivity{

    private static final int PICK_IMAGE_MULTIPLE = 100;

    private Context mContext;
    private RecyclerView pictureRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private LocationFreeboardAddPictureAdapter locationFreeboardAddPictureAdapter;
    private String mLocationID;
    private EditText mLocationFreeboardAddTitleEditText;
    private EditText mLocationFreeboardAddContentEditText;
    private ImageView mLocationFreeboardAddPicutreImageView;
    private Button mLocationFreeboardAddButton;
    private boolean isFirstAddPicture = true;
    private ArrayList<Uri> mArrayUri;
    private ArrayList<String> mDownLoadUrlPicture;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_location_freeboard_add, null, false);
        mFrameLayout.addView(contentView, 0);
        mTextView.setText(getResources().getString(R.string.location_freeboard_add_activity_title));

        mArrayUri = new ArrayList<Uri>();
        mDownLoadUrlPicture = new ArrayList<>();
        mContext = this;
        mLocationFreeboardAddTitleEditText = (EditText) findViewById(R.id.location_freeboard_add_title_editText);
        mLocationFreeboardAddContentEditText = (EditText) findViewById(R.id.location_freeboard_add_content_editText);
        mLocationFreeboardAddPicutreImageView = (ImageView)findViewById(R.id.location_freeboard_add_picture_imageView);
        mLocationFreeboardAddButton = (Button) findViewById(R.id.location_freeboard_add_button);
        pictureRecyclerView = (RecyclerView)findViewById(R.id.location_freeboard_add_picture_recyclerView);

        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        locationFreeboardAddPictureAdapter = new LocationFreeboardAddPictureAdapter();
        pictureRecyclerView.setLayoutManager(linearLayoutManager);
        pictureRecyclerView.setAdapter(locationFreeboardAddPictureAdapter);

        mLocationID = getIntent().getStringExtra(StringKeys.LOCATION_ID_KEY);
        mLocationFreeboardAddPicutreImageView.setOnClickListener(onClickAddImageViewListener);
        mLocationFreeboardAddButton.setOnClickListener(onClickAddButton);


    }

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

/*
                    Intent intent = new Intent();
                    intent.setType("image*//*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);*/
                }
            } catch (Exception e) {

            }

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK) {

            if(data.getClipData()!=null) {


                ClipData mClipData = data.getClipData();

                for (int i = 0; i < mClipData.getItemCount(); i++) {

                    ClipData.Item item = mClipData.getItemAt(i);
                    Uri uri = item.getUri();
                    mArrayUri.add(uri);
                    Log.d("chkUri", uri.toString());

                }
                locationFreeboardAddPictureAdapter.setImageUriArrayList(mArrayUri);


            }else if(data.getData()!=null){

                Uri uri = data.getData();
                mArrayUri.add(uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    View.OnClickListener onClickAddButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(mArrayUri.size()!=0) {
                for (int i = 0; i<mArrayUri.size(); i++){
                    UploadTask uploadTask = FirebaseHelper.uploadFreeboardPictures(mArrayUri.get(i),mLocationID);
                    uploadTask.addOnSuccessListener(onSuccessListener);

                }
            }



        }
    };


    MeResponseCallback kakaoMeResponseCallback = new MeResponseCallback() {
        @Override
        public void onFailure(ErrorResult errorResult) {
            String message = "failed to get user info. msg=" + errorResult;
            Logger.d(message);

            ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
            if (result == ErrorCode.CLIENT_ERROR_CODE) {
                Toast.makeText(mContext, "정보가져오기 실패", Toast.LENGTH_SHORT).show();

            }
        }

        @Override
        public void onSessionClosed(ErrorResult errorResult) {
//                redirectLoginActivity();
        }

        @Override
        public void onSuccess(UserProfile userProfile) {

                String title = mLocationFreeboardAddTitleEditText.getText().toString();
                String content = mLocationFreeboardAddContentEditText.getText().toString();
                FirebaseHelper.sendFreeboardwithKaKao(userProfile,mLocationID,title,content,mDownLoadUrlPicture);

        }

        @Override
        public void onNotSignedUp() {

        }
    };

    OnSuccessListener<UploadTask.TaskSnapshot> onSuccessListener = new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
            mDownLoadUrlPicture.add(taskSnapshot.getDownloadUrl().toString());
            if(mArrayUri.size() == mDownLoadUrlPicture.size()){


                FirebaseUser user = FirebaseHelper.signInState();
                if(user!=null){
                    String userName = user.getDisplayName();


                }
                else if(Session.getCurrentSession().isOpened()){
                    UserManagement.requestMe(kakaoMeResponseCallback);
                    finish();

                }

            }

        }
    };
}
