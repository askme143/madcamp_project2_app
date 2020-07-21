package com.example.madcampserverapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.madcampserverapp.server.MyResponse;
import com.example.madcampserverapp.ui.gallery.Image;
import com.example.madcampserverapp.ui.home.FragmentHome;
import com.example.madcampserverapp.ui.userinfo.FragmentMyinfo2;
import com.example.madcampserverapp.ui.write.FragmentWrite;
import com.example.madcampserverapp.ui.gallery.FragmentGallery;

import com.example.madcampserverapp.server.RequestHttpURLConnection;

import com.example.madcampserverapp.ui.contact.FragmentContact;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    private FragmentHome fragmentHome;
    private FragmentWrite fragmentWrite;
    private FragmentContact fragmentContact;
    private FragmentGallery fragmentGallery;
    private FragmentMyinfo2 fragmentMyinfo;

    private String url = "http://192.249.19.242:7380";
    private String mFacebookID;
    private String mName;
    private String phoneNumber;
    private String location;
    private String writer_name;

    /* Getter */
    public String getUrl() { return url; }
    public String getFacebookID() { return mFacebookID; }
    public String getName() { return mName; }
    public String getLocation() { return location; }
    public boolean isWriteImageSelection() { return writeImageSelection; }

    /* Number of requiring permissions */
    private int mPermissionCount;

    /* Selection indicator */
    private boolean writeImageSelection = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* bottom navigation view click listener */
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView
                .OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
                /* Initialize selection indicator */
                writeImageSelection = false;

                switch (menuItem.getItemId()){
                    case R.id.contact:{
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame_layout, fragmentContact).commitAllowingStateLoss();
                        return true;
                    }
                    case R.id.gallery:{
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame_layout, fragmentGallery).commitAllowingStateLoss();
                        return true;
                    }
                    case R.id.home:{
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame_layout, fragmentHome).commitAllowingStateLoss();
                        return true;
                    }
                    case R.id.write:{
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame_layout, fragmentWrite).commitAllowingStateLoss();
                        return true;
                    }
                    case R.id.myinfo:{
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame_layout, fragmentMyinfo).commitAllowingStateLoss();
                        return true;
                    }
                    default: return false;
                }
            }
        });

        checkPermission();
    }

    public void checkPermission() {
        String tmp = "";

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED)
            tmp += Manifest.permission.READ_CONTACTS + " ";
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED)
            tmp += Manifest.permission.CAMERA + " ";

        if (!TextUtils.isEmpty(tmp)) {
            String[] tmpArray = tmp.trim().split(" ");
            mPermissionCount = tmpArray.length;
            ActivityCompat.requestPermissions(this, tmpArray, 1);
        } else {
            startFragment();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (grantResults.length != mPermissionCount) {
            Toast.makeText(this, "아직 승인받지 않았습니다.", Toast.LENGTH_LONG).show();
            return;
        }

        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,"아직 승인받지 않았습니다.",Toast.LENGTH_LONG).show();
                return;
            }
        }

        startFragment();
    }

    private void startFragment() {
        /* Make fragment objects */
        fragmentContact = new FragmentContact();
        fragmentGallery = new FragmentGallery();
        fragmentHome = new FragmentHome();
        fragmentWrite = new FragmentWrite();
        fragmentMyinfo = new FragmentMyinfo2();

        /* Get intent */
        Intent intent = getIntent();

        /* Get user location & phoneNumber & user info */
        if (intent.hasExtra("fb_id")) {
            phoneNumber = intent.getStringExtra("phonenumber");
            location = intent.getStringExtra("location");
            mFacebookID = intent.getStringExtra("fb_id");
            mName = intent.getStringExtra("name");
        }

        /* Send user info to FragmentMyinfo2 */
        Bundle bundle = new Bundle(2);
        bundle.putString("location", location);
        bundle.putString("phonenumber", phoneNumber);
        fragmentMyinfo.setArguments(bundle);

        /* View selecting point */
        if (intent.hasExtra("writer_name")) {
            /* If caller is BIG_POST_ACTIVITY, then move to CONTACT TAB */

            /* Send writer naem to contact tab */
            Bundle bundle2 = new Bundle();
            writer_name = intent.getStringExtra("writer_name");
            bundle2.putString("writer_name", writer_name);
            fragmentContact.setArguments(bundle2);

            bottomNavigationView.setSelectedItemId(R.id.contact);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,fragmentContact).commitAllowingStateLoss();
        } else {
            /* Default fragment (home tab) */
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, fragmentHome).commitAllowingStateLoss();
        }
    }

    public void startWriteImageSelection() {
        writeImageSelection = true;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragmentGallery).commitAllowingStateLoss();
    }

    public void endWriteImageSelection(Bitmap bitmap) {
        if (bitmap != null)
            fragmentWrite.addWriteImage(bitmap);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragmentWrite).commitAllowingStateLoss();
    }

    public void endWritePost() {
        bottomNavigationView.setSelectedItemId(R.id.home);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragmentHome).commitAllowingStateLoss();
    }

//    /* Image Gallery Code */
//    public boolean selectingImage = false;
//    public String startTimeID;
//
//    public boolean isSelection() {
//        return selectingImage;
//    }
//
//    public void startSelectImage(String id) {
//        selectingImage = true;
//        startTimeID = id;
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.frame_layout, fragmentGallery).commitAllowingStateLoss();
//    }
//
//    public void finishSelectImage(com.example.madcampserverapp.ui.gallery.Image image) {
//        image.saveExerciseImage(startTimeID);
//        selectingImage = false;
//        startTimeID = null;
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.frame_layout, fragmentWrite).commitAllowingStateLoss();
//    }
}