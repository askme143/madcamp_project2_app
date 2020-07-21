package com.example.madcampserverapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.madcampserverapp.ui.home.BigPostActivity;
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
    private static final int BIG_POST_CODE = 1;

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
        Bundle bundle = new Bundle(3);
        bundle.putString("location", location);
        bundle.putString("phonenumber", phoneNumber);
        bundle.putString("name", mName);
        fragmentMyinfo.setArguments(bundle);

        /* Send user info to FragmentWrite */
        Bundle bundle3 = new Bundle(1);
        bundle3.putString("location",location);
        fragmentWrite.setArguments(bundle3);

        /* Default fragment (home tab) */
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragmentHome).commitAllowingStateLoss();
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

    public void showBigPost(String postID) {
        Intent intent = new Intent(this, BigPostActivity.class);

        /* Must put all user info for back up */
        intent.putExtra("fb_id", mFacebookID);
        intent.putExtra("post_id", postID);

        startActivityForResult (intent, BIG_POST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("Main", "Hello???");
        if (requestCode == BIG_POST_CODE) {
            Log.e("Main", "Hello??");
            if (resultCode == RESULT_OK) {
                Log.e("Main", "Hello?");
                Bundle bundle = new Bundle();
                bundle.putString("writer_name", data.getStringExtra("writer_name"));
                fragmentContact.setArguments(bundle);

                bottomNavigationView.setSelectedItemId(R.id.contact);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,fragmentContact).commitAllowingStateLoss();
            }
        }
    }
}