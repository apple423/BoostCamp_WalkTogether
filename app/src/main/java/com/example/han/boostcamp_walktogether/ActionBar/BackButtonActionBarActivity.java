package com.example.han.boostcamp_walktogether.ActionBar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.han.boostcamp_walktogether.R;
import com.example.han.boostcamp_walktogether.helper.FirebaseHelper;
import com.example.han.boostcamp_walktogether.view.LoginActivity;
import com.kakao.auth.Session;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

/**
 * Created by Han on 2017-07-26.
 */



public class BackButtonActionBarActivity  extends AppCompatActivity{
    protected Toolbar mToolbar;
    protected FrameLayout mFrameLayout;
    protected TextView mTextView;
    protected ImageView mImageView;
    protected ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.app_bar_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFrameLayout = (FrameLayout) findViewById(R.id.frame_content);
        mTextView = (TextView)findViewById(R.id.toolbar_textView);
        mImageView = (ImageView)findViewById(R.id.toolbar_heart_imageView);
        mProgressBar = (ProgressBar) findViewById(R.id.base_progress_bar);

    }

    protected void showProgressBar(){
        mProgressBar.setVisibility(View.VISIBLE);

    }

    protected void hideProgressBar(){

        mProgressBar.setVisibility(View.INVISIBLE);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_sign_out:
                AlertDialog.Builder alterDialogBuilder = new AlertDialog.Builder(this)
                        .setTitle(getResources().getString(R.string.logout_prefer))
                        .setPositiveButton(getResources().getString(R.string.logout_yes), onClickSignOutYes)
                        .setNegativeButton(getResources().getString(R.string.logout_no), onClickSignOutNo);

                AlertDialog alertDialog = alterDialogBuilder.create();
                alertDialog.show();
                return true;

        }


        return super.onOptionsItemSelected(item);

    }

    // 로그아웃 확인을 눌렀을 시
    DialogInterface.OnClickListener onClickSignOutYes = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            // 페이스북과 파이어베이스의 경우
            if (FirebaseHelper.signInState() != null) {
                FirebaseHelper.signOut();
                redirectLoginActivity();
            }
            // 카카오톡의 경우
            else if (Session.getCurrentSession().isOpened()) {

                UserManagement.requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        redirectLoginActivity();
                    }


                });
            }

        }
    };

    DialogInterface.OnClickListener onClickSignOutNo = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    };

    public void redirectLoginActivity() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }
}
