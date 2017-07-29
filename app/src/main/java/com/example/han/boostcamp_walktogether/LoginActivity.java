package com.example.han.boostcamp_walktogether;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.han.boostcamp_walktogether.ActionBar.DrawerBaseActivity;

/**
 * Created by Han on 2017-07-25.
 */

public class LoginActivity extends DrawerBaseActivity {

    private Button signInButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_login, mFrameLayout, false);
        mDrawerLayout.addView(contentView, 0);


        signInButton = (Button)findViewById(R.id.sign_in_button);
        final Intent intent  = new Intent(this,MapActivity.class);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(intent);


            }
        });
    }
}
