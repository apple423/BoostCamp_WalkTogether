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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.UploadTask;

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
    private String mEmail,mPassword,mNickName,mImageURL,mAnimalType,mKind;
    private boolean isProfileImageSelected = false;




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

        mSignUpRequestButton.setEnabled(true);
        mProfilePictureImageView.setVisibility(View.INVISIBLE);
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
                    mEmail = mEmailEditText.getText().toString();
                    mPassword = mPasswordEditText.getText().toString();
                    if(mEmail.length()!=0 && mPassword.length()!=0) {
                        FirebaseHelper.signUpWithEmail(mEmail, mPassword)
                                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        //mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(context, "회원가입 실패",
                                                    Toast.LENGTH_SHORT).show();

                                        } else {

                                            mNickName = mNickNameEditText.getText().toString();

                                            isProfileImageSelected = onClickProfileImageButtonClick.sendIsImageSelected();


                                            if(!isProfileImageSelected){

                                                mProfilePictureImageView.setImageResource(R.mipmap.ic_launcher);
                                               // mProfilePictureImageView.setVisibility(View.VISIBLE);
                                                Log.d("From Default","asgsggsd");
                                            }else{

                                                mProfilePictureImageView = onClickProfileImageButtonClick.sendSettedImageView();
                                                Log.d("From Gallery","sadggdssag");
                                            }
                                            mImageURL = "";
                                            UploadTask uploadTask= FirebaseHelper.uploadProfilePicture(mProfilePictureImageView);
                                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
                                                    // Handle unsuccessful uploads
                                                }
                                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                                    mImageURL = taskSnapshot.getDownloadUrl().toString();
                                                    Log.d("ImageURL in Listener : ",mImageURL);
                                                    FirebaseHelper.sendUserData(mEmail,mNickName,mImageURL,"","");
                                                }
                                            });
                                            Log.d("ImageURL : ",mImageURL);
                                            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                                            Toast.makeText(context, "회원가입 성공", Toast.LENGTH_LONG).show();

                                        }

                                    }
                                });
                    }
                    dismiss();
                    FirebaseUser user = FirebaseHelper.signInState();
                    if(user!=null){
                        Intent i = new Intent(context, MapActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        context.startActivity(i);

                    }

                    break;


                case R.id.sign_up_add_profile_picutre_imageView:

                    onClickProfileImageButtonClick.onClickPlusButton();
                    break;

            }

        }
    };

    public void setmProfilePictureImageView(ImageView mProfilePictureImageView) {
        this.mProfilePictureImageView = mProfilePictureImageView;
    }

    @Override
    public ImageView sendImageView() {
        return mProfilePictureImageView;
    }
}
