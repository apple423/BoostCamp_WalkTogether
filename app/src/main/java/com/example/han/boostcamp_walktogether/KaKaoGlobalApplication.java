package com.example.han.boostcamp_walktogether;

import android.app.Application;
import android.content.Context;

import com.example.han.boostcamp_walktogether.Adapters.KaKaoSDKAdapter;
import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;

/**
 * Created by Han on 2017-08-02.
 */

public class KaKaoGlobalApplication  extends Application{
    private static volatile KaKaoGlobalApplication instance = null;



    public static KaKaoGlobalApplication getGlobalApplicationContext() {
        if(instance == null)
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        KakaoSDK.init(new KaKaoSDKAdapter());

    }

}
