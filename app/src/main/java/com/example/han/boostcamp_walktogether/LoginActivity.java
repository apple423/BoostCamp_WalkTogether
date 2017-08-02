package com.example.han.boostcamp_walktogether;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.han.boostcamp_walktogether.ActionBar.DrawerBaseActivity;
import com.example.han.boostcamp_walktogether.helper.FirebaseHelper;
import com.example.han.boostcamp_walktogether.helper.KaKaoSessionCallback;
import com.example.han.boostcamp_walktogether.helper.RequestKakaoMeAndSignUpInterface;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.helper.log.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Han on 2017-07-25.
 */

public class LoginActivity extends DrawerBaseActivity implements OnClickProfileImageButtonClick, RequestKakaoMeAndSignUpInterface {

    private static final int PICK_FROM_GALLERY = 100;

    private Button mSignInButton;
    private Button mSignUpButton;
    private ImageButton mFacebookSignInButton;
    private SignUpDialog mSignUpDialog;
    private SendImageViewToDialog mSendImageViewToDialog;
    private ImageView mProfilePictureFromDialog;
    private EditText mEmailEditText, mPasswordEditText;
    private ProgressBar mProgressBar;
    private boolean isProfileImageSelected = false;
    private String mEmail, mPassword;
    private LoginManager mFacebookLoginManager;
    private View.OnClickListener mOnClickListener;
    private ArrayList<String> mFacebookLoginArrayList;
    private Context mContext;
    private CallbackManager mCallbackManager;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mSharedPreferencesEditor;
    private KaKaoSessionCallback mKaKaoSessionCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_login, mFrameLayout, false);
        mDrawerLayout.addView(contentView, 0);


        mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignUpButton = (Button) findViewById(R.id.sign_up_button);
        mFacebookSignInButton = (ImageButton) findViewById(R.id.facebook_sign_in_button);
        mEmailEditText = (EditText) findViewById(R.id.sign_in_email_editText);
        mPasswordEditText = (EditText) findViewById(R.id.sign_in_password_editText);
        mProgressBar = (ProgressBar) findViewById(R.id.sign_in_progressBar);

        mSignUpDialog = new SignUpDialog(this, this);
        mSendImageViewToDialog = mSignUpDialog;

        mFacebookLoginManager = LoginManager.getInstance();
        mContext = this;
        mKaKaoSessionCallback = new KaKaoSessionCallback(this,this);
        Session.getCurrentSession().addCallback(mKaKaoSessionCallback);
        //Session.getCurrentSession().checkAndImplicitOpen();

        mFacebookLoginArrayList = new ArrayList<String>();
        mFacebookLoginArrayList.add(getResources().getString(R.string.facebook_login_permission_email));
        mFacebookLoginArrayList.add(getResources().getString(R.string.facebook_login_permission_public_profile));

        mSharedPreferences = getSharedPreferences("FacebookUserDataCheck",MODE_PRIVATE);
        mSharedPreferencesEditor = mSharedPreferences.edit();
        mSharedPreferencesEditor.putBoolean("FacebookDataSendCheck",false);
        mSharedPreferencesEditor.apply();

        mSharedPreferences = getSharedPreferences("KaKaoUserDataCheck",MODE_PRIVATE);
        mSharedPreferencesEditor = mSharedPreferences.edit();
        mSharedPreferencesEditor.putBoolean("KaKaoDataSendCheck",false);
        mSharedPreferencesEditor.apply();


        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id = v.getId();

                switch (id) {

                    case R.id.sign_in_button:
                        mEmail = mEmailEditText.getText().toString();
                        mPassword = mPasswordEditText.getText().toString();

                        if (mEmail.length() != 0 && mPassword.length() != 0) {
                            mProgressBar.setVisibility(View.VISIBLE);
                            FirebaseHelper.signInWithEmail(mEmail, mPassword
                            ).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    mProgressBar.setVisibility(View.INVISIBLE);
                                    if (!task.isSuccessful()) {

                                        Toast.makeText(LoginActivity.this, "로그인 실패",
                                                Toast.LENGTH_SHORT).show();
                                    } else {

                                        redirectMapActivity();

                                    }

                                }
                            });

                        }
                        break;

                    case R.id.sign_up_button:
                        mSignUpDialog.show();
                        break;

                    case R.id.facebook_sign_in_button :
                        mCallbackManager = CallbackManager.Factory.create();
                        mFacebookLoginManager.logInWithReadPermissions((Activity) mContext, mFacebookLoginArrayList);
                        mFacebookLoginManager.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                FirebaseHelper.handleFacebookAccessToken(loginResult.getAccessToken(),mContext)
                                        .addOnCompleteListener((Activity) mContext, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    // Sign in success, update UI with the signed-in user's information
                                                    mSharedPreferences = getSharedPreferences("FacebookUserDataCheck",MODE_PRIVATE);
                                                    boolean isDataSent = mSharedPreferences.getBoolean("FacebookDataSendCheck",false);
                                                    if(!isDataSent){
                                                        FirebaseHelper.sendFacebookUserData();
                                                        mSharedPreferencesEditor.putBoolean("FacebookDataSendCheck",true);
                                                        mSharedPreferencesEditor.apply();
                                                    }


                                                    Log.d("FacebookSignIn", "signInWithCredential:success");
                                                    Toast.makeText(mContext, "Authentication sucess.",
                                                            Toast.LENGTH_SHORT).show();
                                                    redirectMapActivity();

                                                } else {
                                                    // If sign in fails, display a message to the user.
                                                    Log.w("FacebookSignIn", "signInWithCredential:failure", task.getException());
                                                    Toast.makeText(mContext, "Authentication failed.",
                                                            Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });
                            }

                            @Override
                            public void onCancel() {

                            }

                            @Override
                            public void onError(FacebookException error) {

                            }
                        });

                }


            }
        };

        mSignInButton.setOnClickListener(mOnClickListener);
        mSignUpButton.setOnClickListener(mOnClickListener);
        mFacebookSignInButton.setOnClickListener(mOnClickListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(mKaKaoSessionCallback);
    }

    @Override
    public void onClickPlusButton() {

        Log.i("Test plus Button", "ok");


        try {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);

            } else {

                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, PICK_FROM_GALLERY);


            }
        } catch (Exception e) {

        }

    }

    @Override
    public ImageView sendSettedImageView() {
        return mProfilePictureFromDialog;
    }

    @Override
    public boolean sendIsImageSelected() {
        return isProfileImageSelected;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {

            mProfilePictureFromDialog = mSendImageViewToDialog.sendImageView();
            Glide.with(this).load(data.getData()).into(mProfilePictureFromDialog);
            mProfilePictureFromDialog.setVisibility(View.VISIBLE);
            isProfileImageSelected = true;


        }
        else if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }


    @Override
    public void onBackPressed() {

        AlertDialog.Builder alterDialogBuilder = new AlertDialog.Builder(this)
                .setTitle("종료하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alterDialogBuilder.create();
        alertDialog.show();
    }



    public void redirectMapActivity(){

        final Intent intent = new Intent(this, MapActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    @Override
    public void requestMe() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    Toast.makeText(mContext, "정보가져오기 실패", Toast.LENGTH_SHORT).show();
                    //finish();
                } else {
                    // redirectLoginActivity();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
//                redirectLoginActivity();
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
               // Logger.d("UserProfile : " + userProfile);
                Log.d("UserProfile :" , userProfile.toString());
                //Toast.makeText(mContext,userProfile.toString(),Toast.LENGTH_LONG).show();
                mSharedPreferences = getSharedPreferences("KaKaoUserDataCheck",MODE_PRIVATE);
                boolean isDataSent = mSharedPreferences.getBoolean("KaKaoDataSendCheck",false);
                Log.d("chkKaKaoBoolean",Boolean.toString(isDataSent));

                if(!isDataSent){
                    FirebaseHelper.sendKakaoUserData(userProfile);
                    mSharedPreferencesEditor.putBoolean("KaKaoDataSendCheck",false);
                    mSharedPreferencesEditor.apply();
                }

                redirectMapActivity();
            }

            @Override
            public void onNotSignedUp() {
                requestSignUp();
            }
        });
    }

    @Override
    public void requestSignUp() {
        final Map<String, String> properties = new HashMap<String, String>();
        properties.put("animalType","");
        properties.put("kind","");

        UserManagement.requestSignup(new ApiResponseCallback<Long>() {
            @Override
            public void onNotSignedUp() {
            }

            @Override
            public void onSuccess(Long result) {
                requestMe();

            }

            @Override
            public void onFailure(ErrorResult errorResult) {
                final String message = "UsermgmtResponseCallback : failure : " + errorResult;
                com.kakao.util.helper.log.Logger.w(message);
                Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                // finish();
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
            }
        }, properties);
    }


}
