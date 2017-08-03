package com.example.han.boostcamp_walktogether.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.han.boostcamp_walktogether.MapActivity;
import com.example.han.boostcamp_walktogether.data.ParkDataFromFirebaseDTO;
import com.example.han.boostcamp_walktogether.data.ParkRowDTO;
import com.example.han.boostcamp_walktogether.data.ParkSungNamDTO;
import com.example.han.boostcamp_walktogether.data.UserData;
import com.facebook.AccessToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.kakao.auth.Session;
import com.kakao.usermgmt.response.model.UserProfile;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by Han on 2017-07-31.
 */

public class FirebaseHelper {

    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    private static final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private static final FirebaseStorage mStorage = FirebaseStorage.getInstance();
    private static final StorageReference storageRef = mStorage.getReferenceFromUrl("gs://boostcampwalktogether.appspot.com/");
    private Intent mMapIntent;

    public static FirebaseAuth getAuth(){

        return mAuth;
    }

    public static DatabaseReference getDatabaseReference(){

        return mDatabase;
    }

    public static FirebaseStorage getFirebaseStorage(){

        return mStorage;
    }


    public static Task<AuthResult> signUpWithEmail(String email, String password){


        return mAuth.createUserWithEmailAndPassword(email, password);

    }

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
                .setPhotoUri(Uri.parse(imageURL))
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

    public static FirebaseUser signInState(){

        FirebaseUser user = mAuth.getCurrentUser();

            return user;

    }

    public static void signOut(){

        mAuth.signOut();

    }

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

    public static Task<AuthResult> handleFacebookAccessToken(AccessToken token, final Context context) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        return mAuth.signInWithCredential(credential);

    }

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

    public static void sendParkDataInSeoul(ParkRowDTO parkRowDTO){

        String uid = "seoul_"+ parkRowDTO.getP_IDX();
        String name = parkRowDTO.getP_PARK();
        String address = parkRowDTO.getP_ADDR();
        String photoUrl = parkRowDTO.getP_IMG();
        float latitude = parkRowDTO.getLATITUDE();
        float longitude = parkRowDTO.getLONGITUDE();

        mDatabase.child("parks/seoul").push();
        //UserData userData = new UserData(email,name, photoUrl,"","", uid);
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid",uid);
        result.put("name",name);
        result.put("imageURL", photoUrl);
        result.put("address",address);
        result.put("latitude",latitude);
        result.put("longitude",longitude);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/parks/seoul/"+ uid, result);
        mDatabase.updateChildren(childUpdates);
    }

    public static void sendParkDataInSungNam(Context context) {
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open("parkSungNam.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        BufferedReader br = null;
        br = new BufferedReader(new InputStreamReader(inputStream));
        Type listType = new TypeToken<List<ParkSungNamDTO>>(){}.getType();
        List<ParkSungNamDTO> parkSungNamDTOList = gson.fromJson(br, listType);

        for (ParkSungNamDTO parkSungNamDTOIndex:parkSungNamDTOList) {

            String uid = "sungnam_" + parkSungNamDTOIndex.getP_IDX();
            String name = parkSungNamDTOIndex.getP_PARK();
            String address = parkSungNamDTOIndex.getP_ADDR();
            parkSungNamDTOIndex.setP_IMG("https://firebasestorage.googleapis.com/v0/b/boostcampwalktogether.appspot.com/o/images%2Fprofile%2FaF4n4Nz8RnNPiqFqRxe0lDqoNIm1%2Fprofile.jpg?alt=media&token=ec11dba3-e7a1-4c2f-8290-0260b2a10aab");
            String photoUrl = parkSungNamDTOIndex.getP_IMG();
            float latitude = parkSungNamDTOIndex.getLATITUDE();
            float longitude = parkSungNamDTOIndex.getLONGITUDE();

            mDatabase.child("parks/sungnam").push();
            HashMap<String, Object> result = new HashMap<>();
            result.put("uid",uid);
            result.put("name",name);
            result.put("address",address);
            result.put("imageURL", photoUrl);
            result.put("latitude",latitude);
            result.put("longitude",longitude);
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/parks/sungnam/"+ uid, result);
            mDatabase.updateChildren(childUpdates);
        }
    }

    public static ArrayList<ParkDataFromFirebaseDTO> getAllSeoulParkData(){

        final ArrayList<ParkDataFromFirebaseDTO> seoulParkDataFromFirebaseDTOArrayList = new ArrayList<ParkDataFromFirebaseDTO>();
        mDatabase.child("parks").child("seoul").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                            ParkDataFromFirebaseDTO c = snapshot.getValue(ParkDataFromFirebaseDTO.class);
                            Log.d("Categories Seoul: ", c.getName() + " " +c.getAddress());
                            seoulParkDataFromFirebaseDTOArrayList.add(c);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });

        return seoulParkDataFromFirebaseDTOArrayList;
    }

    public static ArrayList<ParkDataFromFirebaseDTO> getAllSungnamParkData() {

        final ArrayList<ParkDataFromFirebaseDTO> sungnamParkDataFromFirebaseDTOArrayList = new ArrayList<ParkDataFromFirebaseDTO>();

        mDatabase.child("parks").child("sungnam").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                            ParkDataFromFirebaseDTO c = snapshot.getValue(ParkDataFromFirebaseDTO.class);
                            Log.d("Categories Sungnam: ", c.getName() + " " +c.getAddress());
                            sungnamParkDataFromFirebaseDTOArrayList.add(c);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
        return sungnamParkDataFromFirebaseDTOArrayList;
    }

}
