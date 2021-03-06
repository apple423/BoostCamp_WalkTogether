package com.example.han.boostcamp_walktogether.interfaces;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;


/**
 * Created by Han on 2017-07-31.
 */
// + 이미지를 클릭 시 저장소 권한 부여 및 갤러리로 Intent를 넘기기 위한 인터페이스
public interface OnClickProfileImageButtonClickInterface {
    void onClickPlusButton();
    ImageView sendSettedImageView();
    boolean sendIsImageSelected();

}
