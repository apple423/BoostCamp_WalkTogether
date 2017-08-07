package com.example.han.boostcamp_walktogether.helper;

import android.content.Context;
import android.widget.Toast;

import com.kakao.auth.ISessionCallback;
import com.kakao.util.exception.KakaoException;

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
