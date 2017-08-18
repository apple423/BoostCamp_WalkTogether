package com.example.han.boostcamp_walktogether.interfaces;

import android.widget.TextView;

/**
 * Created by Han on 2017-07-28.
 */
// 장소별 게시판에서 특정 게시물을 클릭했을때 특정 게시물로 엑티비티 전환을 위한 인터페이스
public interface OnClickFreeboardInterface {

    void onClickBoard(int position);
    String onClickLike(TextView textView, int position, boolean like);
}
