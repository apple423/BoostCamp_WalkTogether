package com.example.han.boostcamp_walktogether.util;

/**
 * Created by Han on 2017-08-04.
 */
// 여러 String 키들을 저장한 Util
public class StringKeys {

    //Login Activity의 sharedPreferences 상수
    public static final String FACEBOOK_SHARED_PREFERENCES = "FacebookUserDataCheck";
    public static final String KAKAO_SHARED_PREFERENCES = "KaKaoUserDataCheck";
    public static final String FACEBOOK_DATA_SEND_CHECK="FacebookDataSendCheck";
    public static final String KAKAO_DATA_SEND_CHECK="KaKaoDataSendCheck";



    //Location Activity Location ID
    public static final String LOCATION_ID_KEY ="LOCATION_ID";
    public static final String LOCATION_FREEBOARD_KEY = "LOCATION_FREEBOARD_KEY";
    public static final String LOCATION_FREEBOARD_PARCELABLE = "LOCATION_FREEBOARD_PARCELABLE";
    public static final String LOCATION_NAME ="LOCATION_NAME";

    //User profile  SharedPreferences
    public static final String USER_PROFILE="userProfile";
    public static final String USER_NICK_NAME = "nickName";
    public static final String USER_PROFILE_PICTURE = "userProfilePicture";
    public static final String USER_EMAIL= "userEmail";
    public static final String LOCATION_INTENT_KEY = "locationData";

    //ParkList and ParkImageList
    public static final String PARK_LIST = "parkList";
    public static final String PARK_IMAGE_LIST = "parkImageList";
    public static final String COMMENT_LIST = "commentList";


    //MainActivity
    public static final String SEARCH_LATITUDE = "latitude";
    public static final String SEARCH_LONGITUDE = "longitude";

    //WalkDiaryActivity
    public static final String IMAGE_FILE_URI = "lmageFileUri";
    public static final String WALK_DIARY_ADD_DIALOG = "dialog_add";
    public static final String WALK_DIARY_INFO_DIALOG = "dialog_info";


}
