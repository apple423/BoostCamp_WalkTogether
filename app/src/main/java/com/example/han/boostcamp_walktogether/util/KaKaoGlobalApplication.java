package com.example.han.boostcamp_walktogether.util;

import android.app.Application;

import com.example.han.boostcamp_walktogether.Adapters.KaKaoSDKAdapter;
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
