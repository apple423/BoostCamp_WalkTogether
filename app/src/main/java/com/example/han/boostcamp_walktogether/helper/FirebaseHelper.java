package com.example.han.boostcamp_walktogether.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import static android.content.ContentValues.TAG;

/**
 * Created by Han on 2017-07-31.
 */

public class FirebaseHelper {

    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    private static final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private static final FirebaseStorage mStorage = FirebaseStorage.getInstance();

    public static FirebaseAuth getAuth(){

        return mAuth;
    }

    public static DatabaseReference getDatabaseReference(){

        return mDatabase;
    }

    public static FirebaseStorage getFirebaseStorage(){

        return mStorage;
    }


    public static void signUpWithEmail(final Context context,
                                       EditText emailEditText, EditText passwordEditText, final ProgressBar progressBar){

        String emailString = emailEditText.getText().toString();
        String passwordString = passwordEditText.getText().toString();


        mAuth.createUserWithEmailAndPassword(emailString, passwordString)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(ProgressBar.INVISIBLE);
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Toast.makeText(context, "회원가입 실패",
                                    Toast.LENGTH_SHORT).show();

                        } else{
                            Toast.makeText(context, "회원가입 성공", Toast.LENGTH_LONG).show();

                        }

                    }
                });



    }

}
