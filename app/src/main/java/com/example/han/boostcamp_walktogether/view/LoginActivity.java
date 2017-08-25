package com.example.han.boostcamp_walktogether.view;

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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.han.boostcamp_walktogether.ActionBar.DrawerBaseActivity;
import com.example.han.boostcamp_walktogether.R;
import com.example.han.boostcamp_walktogether.helper.FirebaseHelper;
import com.example.han.boostcamp_walktogether.helper.KaKaoSessionCallback;
import com.example.han.boostcamp_walktogether.helper.RequestKakaoMeAndSignUpInterface;
import com.example.han.boostcamp_walktogether.interfaces.OnClickProfileImageButtonClickInterface;
import com.example.han.boostcamp_walktogether.interfaces.SendImageViewToDialogInterface;
import com.example.han.boostcamp_walktogether.util.SharedPreferenceUtil;
import com.example.han.boostcamp_walktogether.util.StringKeys;
import com.example.han.boostcamp_walktogether.widget.SignUpDialog;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
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

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.han.boostcamp_walktogether.util.StringKeys.FACEBOOK_DATA_SEND_CHECK;
import static com.example.han.boostcamp_walktogether.util.StringKeys.FACEBOOK_SHARED_PREFERENCES;
import static com.example.han.boostcamp_walktogether.util.StringKeys.KAKAO_DATA_SEND_CHECK;
import static com.example.han.boostcamp_walktogether.util.StringKeys.KAKAO_SHARED_PREFERENCES;
import static com.example.han.boostcamp_walktogether.util.StringKeys.USER_EMAIL;
import static com.example.han.boostcamp_walktogether.util.StringKeys.USER_NICK_NAME;
import static com.example.han.boostcamp_walktogether.util.StringKeys.USER_PROFILE;
import static com.example.han.boostcamp_walktogether.util.StringKeys.USER_PROFILE_PICTURE;

/**
 * Created by Han on 2017-07-25.
 */

public class LoginActivity extends AppCompatActivity implements OnClickProfileImageButtonClickInterface, RequestKakaoMeAndSignUpInterface {

    private static final int PICK_FROM_GALLERY = 100;

    private Button mSignInButton;
    private Button mSignUpButton;
    private ImageButton mFacebookSignInButton;
    private LinearLayout mFacebookSignInLinear;
    private SignUpDialog mSignUpDialog;
    private SendImageViewToDialogInterface mSendImageViewToDialogInterface;
    private ImageView mProfilePictureFromDialog;
    private EditText mEmailEditText, mPasswordEditText;
    private ProgressBar mProgressBar;
    private boolean isProfileImageSelected = false;
    private String mEmail, mPassword;
    private LoginManager mFacebookLoginManager;
    private ArrayList<String> mFacebookLoginArrayList;
    private Context mContext;
    private CallbackManager mCallbackManager;
    private KaKaoSessionCallback mKaKaoSessionCallback;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;
        mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignUpButton = (Button) findViewById(R.id.sign_up_button);
        //mFacebookSignInButton = (ImageButton) findViewById(R.id.facebook_sign_in_button);
        mEmailEditText = (EditText) findViewById(R.id.sign_in_email_editText);
        mPasswordEditText = (EditText) findViewById(R.id.sign_in_password_editText);
        mProgressBar = (ProgressBar) findViewById(R.id.sign_in_progressBar);
        mFacebookSignInLinear = (LinearLayout) findViewById(R.id.facebook_sign_in_linear);



        mSignUpDialog = new SignUpDialog(this, this);
        mSendImageViewToDialogInterface = mSignUpDialog;


        mFacebookLoginManager = LoginManager.getInstance();
        mKaKaoSessionCallback = new KaKaoSessionCallback(this,this);
        Session.getCurrentSession().addCallback(mKaKaoSessionCallback);

        // 페이스북 로그인 퍼미션 추가
        mFacebookLoginArrayList = new ArrayList<String>();
        mFacebookLoginArrayList.add(getResources().getString(R.string.facebook_login_permission_email));
        mFacebookLoginArrayList.add(getResources().getString(R.string.facebook_login_permission_public_profile));

        // 로그인 후 사용자 데이터 유지를 위하여 사용합니다.
        SharedPreferenceUtil.setFaceBookCheckSharedPreference(this, StringKeys.FACEBOOK_SHARED_PREFERENCES,MODE_PRIVATE);
        SharedPreferenceUtil.editFaceBookCheckSharedPreference(FACEBOOK_DATA_SEND_CHECK,false);
        SharedPreferenceUtil.setFaceBookCheckSharedPreference(this,KAKAO_SHARED_PREFERENCES,MODE_PRIVATE);
        SharedPreferenceUtil.editFaceBookCheckSharedPreference(KAKAO_DATA_SEND_CHECK,false);

        mSignInButton.setOnClickListener(mOnClickListener);
        mSignUpButton.setOnClickListener(mOnClickListener);
        //mFacebookSignInButton.setOnClickListener(mOnClickListener);
        mFacebookSignInLinear.setOnClickListener(mOnClickListener);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(mKaKaoSessionCallback);
    }

    // 다이얼로그에서 +버튼 누를시 갤러리 실행
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

    // 다이얼로그에 갤러리에서 선택한 이미지의 이미지뷰 반영을 위하여
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

            mProfilePictureFromDialog = mSendImageViewToDialogInterface.sendImageView();
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
                .setTitle(getResources().getString(R.string.finish_prefer))
                .setPositiveButton(getResources().getString(R.string.finish_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.finish_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alterDialogBuilder.create();
        alertDialog.show();
    }



    public void redirectMainActivity(){

        final Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    // 카카오톡 로그인시 사용자 정보를 요청하기 위한 함수

    @Override
    public void requestMe() {
        UserManagement.requestMe(kakaoMeResponseCallback);
    }

    // 카카오톡 로그인 후 앱 연결을 위한 함수
    @Override
    public void requestSignUp() {
        final Map<String, String> properties = new HashMap<String, String>();
        properties.put("animalType","");
        properties.put("kind","");

        UserManagement.requestSignup(kakaoApiResponseCallback, properties);
    }


    View.OnClickListener mOnClickListener = new View.OnClickListener() {
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
                        ).addOnCompleteListener(LoginActivity.this, onCompleteSignInListener);
                    }
                    break;

                case R.id.sign_up_button:
                    mSignUpDialog.show();
                    break;

                case R.id.facebook_sign_in_linear :
                    mCallbackManager = CallbackManager.Factory.create();
                    mFacebookLoginManager.logInWithReadPermissions((Activity) mContext, mFacebookLoginArrayList);
                    mFacebookLoginManager.registerCallback(mCallbackManager,facebookCallback);

            }

        }
    };

    // 파이어베이스 이메일/패스워드 로그인 후 콜백 리스너
    OnCompleteListener<AuthResult> onCompleteSignInListener = new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {

            mProgressBar.setVisibility(View.INVISIBLE);
            if (!task.isSuccessful()) {

                Toast.makeText(LoginActivity.this, "로그인 실패",
                        Toast.LENGTH_SHORT).show();
            } else {

                setUserProfileToSharedPreferences();
                redirectMainActivity();

            }

        }
    };

    // 페이스북 계정이 파이어베이스에 계정 추가 성공 여부를 위한 콜백 리스너
    OnCompleteListener<AuthResult> onCompleteFaceBookSignInListener = new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                // Sign in success, update UI with the signed-in user's information

                SharedPreferenceUtil.setFaceBookCheckSharedPreference(mContext,
                        FACEBOOK_SHARED_PREFERENCES,MODE_PRIVATE);
                boolean isDataSent = SharedPreferenceUtil.getFaceBookStoredValue(FACEBOOK_DATA_SEND_CHECK);


                if(!isDataSent){
                    FirebaseHelper.sendFacebookUserData();
                    SharedPreferenceUtil.editFaceBookCheckSharedPreference(FACEBOOK_DATA_SEND_CHECK,true);

                }

                setUserProfileToSharedPreferences();


                Log.d("FacebookSignIn", "signInWithCredential:success");
                Toast.makeText(mContext, "Authentication sucess.",
                        Toast.LENGTH_SHORT).show();
                redirectMainActivity();

            } else {
                // If sign in fails, display a message to the user.
                Log.w("FacebookSignIn", "signInWithCredential:failure", task.getException());
                Toast.makeText(mContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();

            }
        }
    };

    private void setUserProfileToSharedPreferences() {
        FirebaseUser user = FirebaseHelper.signInState();
        SharedPreferenceUtil.setUserProfileSharedPreference(mContext,USER_PROFILE,MODE_PRIVATE);
        SharedPreferenceUtil.editUserProfileSharedPreference(USER_EMAIL,user.getEmail());
        SharedPreferenceUtil.editUserProfileSharedPreference(USER_PROFILE_PICTURE,user.getPhotoUrl().toString());
        SharedPreferenceUtil.editUserProfileSharedPreference(USER_NICK_NAME,user.getDisplayName());

    }

    // 페이스북 로그인 성공 여부를 위한 콜백리스너
    FacebookCallback<LoginResult> facebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            FirebaseHelper.handleFacebookAccessToken(loginResult.getAccessToken(),mContext)
                    .addOnCompleteListener((Activity) mContext, onCompleteFaceBookSignInListener);
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };

    ApiResponseCallback<Long> kakaoApiResponseCallback = new ApiResponseCallback<Long>() {
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
        }

        @Override
        public void onSessionClosed(ErrorResult errorResult) {
        }
    };

    // 카카오톡 유저 정보 가져오기 성공여부의 리스너
    MeResponseCallback kakaoMeResponseCallback = new MeResponseCallback() {
        @Override
        public void onFailure(ErrorResult errorResult) {
            String message = "failed to get user info. msg=" + errorResult;
            Logger.d(message);

            ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
            if (result == ErrorCode.CLIENT_ERROR_CODE) {
                Toast.makeText(mContext, "정보가져오기 실패", Toast.LENGTH_SHORT).show();

            } else {

            }
        }

        @Override
        public void onSessionClosed(ErrorResult errorResult) {
//                redirectLoginActivity();
        }

        @Override
        public void onSuccess(UserProfile userProfile) {

            Log.d("UserProfile :" , userProfile.toString());
            SharedPreferenceUtil.setKaKaoCheckSharedPreference(mContext,KAKAO_SHARED_PREFERENCES,MODE_PRIVATE);
            boolean isDataSent =  SharedPreferenceUtil.getKaKaoStoredValue(KAKAO_DATA_SEND_CHECK);
            Log.d("chkKaKaoBoolean",Boolean.toString(isDataSent));

            if(!isDataSent){
                FirebaseHelper.sendKakaoUserData(userProfile);

                SharedPreferenceUtil.editKaKaoCheckSharedPreference(KAKAO_DATA_SEND_CHECK,true);

            }
            SharedPreferenceUtil.setUserProfileSharedPreference(mContext,USER_PROFILE,MODE_PRIVATE);
            SharedPreferenceUtil.editUserProfileSharedPreference(USER_EMAIL, userProfile.getEmail());
            SharedPreferenceUtil.editUserProfileSharedPreference(USER_PROFILE_PICTURE,userProfile.getThumbnailImagePath());
            SharedPreferenceUtil.editUserProfileSharedPreference(USER_NICK_NAME,userProfile.getNickname());
            //setUserProfileToSharedPreferences();

            redirectMainActivity();
        }

        @Override
        public void onNotSignedUp() {
            requestSignUp();
        }
    };

}
