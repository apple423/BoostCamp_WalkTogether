package com.example.han.boostcamp_walktogether;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.example.han.boostcamp_walktogether.ActionBar.DrawerBaseActivity;

/**
 * Created by Han on 2017-07-25.
 */

public class LoginActivity extends DrawerBaseActivity implements OnClickProfileImageButtonClick{

    private static final int PICK_FROM_GALLERY = 100;
    private Button mSignInButton;
    private Button mSignUpButton;
    private SignUpDialog mSignUpDialog;
    private RequestBuilder<Drawable> mRequestBuilder;
    private SendImageViewToDialog mSendImageViewToDialog;
    private ImageView mProfilePictureFromDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_login, mFrameLayout, false);
        mDrawerLayout.addView(contentView, 0);


        mSignInButton = (Button)findViewById(R.id.sign_in_button);
        mSignUpButton = (Button) findViewById(R.id.sign_up_button);

        mSignUpDialog = new SignUpDialog(this,this);
        mSendImageViewToDialog = mSignUpDialog;


        final Intent intent  = new Intent(this,MapActivity.class);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(intent);

            }
        });

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSignUpDialog.show();
            }
        });

    }

    @Override
    public void onClickPlusButton() {

        Log.i("Test plus Button","ok");

        try {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);

            } else{

                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, PICK_FROM_GALLERY);

            }
        } catch (Exception e) {

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {

            mProfilePictureFromDialog = mSendImageViewToDialog.sendImageView();
            Glide.with(this).load(data.getData()).into(mProfilePictureFromDialog);



        }
}




}
