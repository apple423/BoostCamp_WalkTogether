package com.example.han.boostcamp_walktogether.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Han on 2017-08-04.
 */

public class SharedPreferenceUtil {


    private static SharedPreferences mSharedPreferences;

    public static void setFaceBookCheckSharedPreference(Context context, String key,int mode){

        mSharedPreferences = context.getSharedPreferences(key,mode);

    }

    public static void setKaKaoCheckSharedPreference(Context context, String key, int mode){

        mSharedPreferences = context.getSharedPreferences(key,mode);

    }

    public static void editFaceBookCheckSharedPreference(String key, boolean value){

       SharedPreferences.Editor sharedPreferenceEditor =  mSharedPreferences.edit();
        sharedPreferenceEditor.putBoolean(key,value);
        sharedPreferenceEditor.apply();

    }

    public static void editKaKaoCheckSharedPreference( String key,boolean value){

        SharedPreferences.Editor sharedPreferenceEditor =  mSharedPreferences.edit();
        sharedPreferenceEditor.putBoolean(key,value);
        sharedPreferenceEditor.apply();
    }

    public static boolean getFaceBookStoredValue(String key){

        boolean value = mSharedPreferences.getBoolean(key,false);
        return value;

    }


    public static boolean getKaKaoStoredValue( String key){

        boolean value = mSharedPreferences.getBoolean(key,false);
        return value;

    }


}
