package com.example.han.boostcamp_walktogether;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.han.boostcamp_walktogether.helper.FirebaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.content.ContentValues.TAG;

/**
 * Created by Han on 2017-07-31.
 */

public class SignUpDialog extends Dialog implements SendImageViewToDialog{

    private FirebaseAuth mAuth;
    private Context context;
    private EditText mEmailEditText, mPasswordEditText, mNickNameEditText;
    private Button mSignUpRequestButton;
    private ProgressBar mProgressBar;
    private ImageView mAddProfilePicutreImageView;
    private ImageView mProfilePictureImageView;
    private OnClickProfileImageButtonClick onClickProfileImageButtonClick;

    


    public SignUpDialog(@NonNull Context context, OnClickProfileImageButtonClick onClickProfileImageButtonClick) {
        super(context);
        this.context = context;
        this.onClickProfileImageButtonClick = onClickProfileImageButtonClick;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);
        setContentView(R.layout.dialog_signup);

        mEmailEditText = (EditText)findViewById(R.id.sign_up_email_editText);
        mPasswordEditText = (EditText)findViewById(R.id.sign_up_password_editText);
        mNickNameEditText = (EditText)findViewById(R.id.sign_up_nickname_editText);
        mSignUpRequestButton = (Button) findViewById(R.id.sign_up_request_button);
        mProgressBar = (ProgressBar) findViewById(R.id.sign_up_request_progressBar);
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        mAddProfilePicutreImageView = (ImageView) findViewById(R.id.sign_up_add_profile_picutre_imageView);
        mProfilePictureImageView = (ImageView)findViewById(R.id.sign_up_profile_picture_imageView);


        mAuth = FirebaseHelper.getAuth();

        mSignUpRequestButton.setOnClickListener(onClickListener);
        mAddProfilePicutreImageView.setOnClickListener(onClickListener);

    }


    Button.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int id = v.getId();

            switch (id) {

                case R.id.sign_up_request_button:
                    mProgressBar.setVisibility(ProgressBar.VISIBLE);
                    String email = mEmailEditText.getText().toString();
                    String password = mPasswordEditText.getText().toString();
                    mSignUpRequestButton.setEnabled(false);
                    String nickName = mNickNameEditText.getText().toString();
                    BitmapDrawable drawable = (BitmapDrawable) mProfilePictureImageView.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    dismiss();
                    break;


                case R.id.sign_up_add_profile_picutre_imageView:

                    onClickProfileImageButtonClick.onClickPlusButton();

            }

        }
    };


    @Override
    public ImageView sendImageView() {
        return mProfilePictureImageView;
    }
}
