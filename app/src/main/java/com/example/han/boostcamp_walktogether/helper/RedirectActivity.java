package com.example.han.boostcamp_walktogether.helper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.example.han.boostcamp_walktogether.view.LoginActivity;
import com.example.han.boostcamp_walktogether.view.MapActivity;

/**
 * Created by Han on 2017-08-02.
 */

public class RedirectActivity extends AppCompatActivity {

    protected void redirectLoginActivity() {
        final Intent intent = new Intent(RedirectActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    protected void redirectMapActivity() {
        final Intent intent = new Intent(RedirectActivity.this, MapActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
