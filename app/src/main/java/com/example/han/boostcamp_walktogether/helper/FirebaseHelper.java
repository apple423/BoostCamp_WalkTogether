package com.example.han.boostcamp_walktogether.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.example.han.boostcamp_walktogether.data.UserData;
import com.facebook.AccessToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kakao.usermgmt.response.model.UserProfile;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by Han on 2017-07-31.
 */
// 파이어베이스 관련 Helper클래스
    // TODO 4. 서버 구현을 인하여 안 쓰는 것들을 정리해야 한다.
public class FirebaseHelper {

    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    private static final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private static final FirebaseStorage mStorage = FirebaseStorage.getInstance();
    private static final StorageReference storageRef = mStorage.getReferenceFromUrl("gs://boostcampwalktogether.appspot.com/");


    public static FirebaseAuth getAuth(){

        return mAuth;
    }

    // 파이어베이스를 통해 이메일 패스워드 회원가입
    public static Task<AuthResult> signUpWithEmail(String email, String password){


        return mAuth.createUserWithEmailAndPassword(email, password);

    }
    // 파이어베이스를 통해 이메일로 로그인
    public static Task<AuthResult> signInWithEmail(String email, String password){

        return mAuth.signInWithEmailAndPassword(email, password);
    }

    public static void sendUserData(String email,String nickName,String imageURL,String animalType,String kind){

        mDatabase.child("users").push();
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();
        UserData userData = new UserData(email,nickName,imageURL,animalType,kind, uid);
        Map<String, Object> userDataMap = userData.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/users/"+ uid, userDataMap);

        mDatabase.updateChildren(childUpdates);


        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(imageURL)) // 프로필 사진 파이어베이스 user에 추가
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });
    }
  // 로그인 상태 체크
    public static FirebaseUser signInState(){

        FirebaseUser user = mAuth.getCurrentUser();

            return user;

    }
    // 로그아웃
    public static void signOut(){

        mAuth.signOut();

    }
    // 프로필 사진 업로드
    public static UploadTask uploadProfilePicture(ImageView imageView){

        final Uri[] downloadUrl = new Uri[1];
        //String[] subEmail = email.split("@");
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();

        StorageReference imagesRef = storageRef.child("images/profile/"+ uid+"/profile.jpg");
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imagesRef.putBytes(data);


        return uploadTask;
    }

    // 페이스북 토큰 이용해서 파이어베이스에 유저로 등록
    public static Task<AuthResult> handleFacebookAccessToken(AccessToken token, final Context context) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        return mAuth.signInWithCredential(credential);

    }

    // 페이스북으로 로그인한 사용자의 데이터를 파이어베이스에 저장
    // 쓸때 없는거 같기 때문에 삭제를 고려하고 있다.
    public static void sendFacebookUserData(){

        FirebaseUser user =mAuth.getCurrentUser();
        if(user!=null){

            for(UserInfo profile : user.getProviderData()){


                String uid = profile.getUid();
                // Name, email address, and profile photo Url
                String name = profile.getDisplayName();
                String email = profile.getEmail();
                Uri photoUrl = profile.getPhotoUrl();

                mDatabase.child("users").push();
                UserData userData = new UserData(email,name, photoUrl.toString(),"","", uid);
                Map<String, Object> userDataMap = userData.toMap();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/users/"+ uid, userDataMap);
                mDatabase.updateChildren(childUpdates);
            }


        }


    }

    public static void sendKakaoUserData(UserProfile userProfile){
        String uid = Long.toString(userProfile.getId());
        String name = userProfile.getNickname();
        String email = userProfile.getEmail();
        String photoUrl = userProfile.getThumbnailImagePath();


        Log.d("chkKaKaoSendUserData",email + " "+ name + " " + photoUrl + " " + uid);

        mDatabase.child("users").push();
        UserData userData = new UserData(email,name, photoUrl,"","", uid);
        Map<String, Object> userDataMap = userData.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/users/"+ uid, userDataMap);
        mDatabase.updateChildren(childUpdates);
    }


}
