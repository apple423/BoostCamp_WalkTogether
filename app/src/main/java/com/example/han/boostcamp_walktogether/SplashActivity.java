package com.example.han.boostcamp_walktogether;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.han.boostcamp_walktogether.helper.FirebaseHelper;
import com.example.han.boostcamp_walktogether.view.LoginActivity;
import com.example.han.boostcamp_walktogether.view.MainActivity;
import com.google.firebase.auth.FirebaseUser;
import com.kakao.auth.Session;

/**
 * Created by Han on 2017-07-25.
 */

public class SplashActivity extends AppCompatActivity{

    private final int SPLASH_DISPLAY_LENGTH = 2000;
    private Context mContext = this;
    //TODO 2. 스플래쉬를 runnable 과 같이 다시 처리해야한다. 현재 깜빡임 현상이 있다.
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final FirebaseUser user = FirebaseHelper.signInState();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                if(user!=null | Session.getCurrentSession().isOpened()){
                    Intent i = new Intent(mContext, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                }else {
                    Intent i = new Intent(mContext, LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }

                finish();


            }

        }, SPLASH_DISPLAY_LENGTH);




    }

}
