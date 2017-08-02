package com.example.han.boostcamp_walktogether.helper;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.han.boostcamp_walktogether.LoginActivity;
import com.example.han.boostcamp_walktogether.MapActivity;
import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Han on 2017-08-02.
 */

public class KaKaoSessionCallback extends RedirectActivity implements ISessionCallback {


    private Context mContext;
    private RequestKakaoMeAndSignUpInterface requestKakaoMeAndSignUpInterface;
    @Override
    public void onSessionOpened() {
        //redirectMapAcitivity();
        Toast.makeText(mContext, "Success in session KaKao" , Toast.LENGTH_LONG).show();
        requestKakaoMeAndSignUpInterface.requestMe();
    }

    public KaKaoSessionCallback(Context context, RequestKakaoMeAndSignUpInterface requestKakaoMeAndSignUpInterface) {
        mContext = context;
        this.requestKakaoMeAndSignUpInterface = requestKakaoMeAndSignUpInterface;
    }

    @Override
    public void onSessionOpenFailed(KakaoException exception) {
        Toast.makeText(mContext, "Error in session KaKao" + exception.toString(), Toast.LENGTH_LONG).show();

    }
}
