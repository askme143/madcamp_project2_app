package com.example.madcampserverapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.madcampserverapp.server.MyResponse;
import com.example.madcampserverapp.ui.home.FragmentHome;
import com.example.madcampserverapp.ui.userinfo.FragmentMyinfo2;
import com.example.madcampserverapp.ui.write.FragmentWrite;
import com.example.madcampserverapp.ui.gallery.FragmentGallery;

import com.example.madcampserverapp.server.RequestHttpURLConnection;

import com.example.madcampserverapp.ui.contact.FragmentContact;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private FragmentHome fragmentHome;
    private FragmentWrite fragmentWrite;
    private FragmentContact fragmentContact;
    private FragmentGallery fragmentGallery;
    private FragmentMyinfo2 fragmentMyinfo;

    private String url = "http://192.249.19.242:7380";
    private String mFacebookID;
    private String phoneNumber;
    private String location;

    public String getUrl() { return url; }
    public String getFacebookID() { return mFacebookID; }

    private int mPermissionCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Ignore: Code for testing */
        boolean test = false;
        if (test){
            String testUrl = url + "/gallery/upload";
            Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.contact_icon)).getBitmap();
            ContentValues contentValues = new ContentValues();
            contentValues.put("fb_id", "12321");
            contentValues.put("file_name", "contact_icon.jpg");

            MyResponse response = new MyResponse() {
                @Override
                public void response(String result) {
                    Log.e("hello", result);
                }
            };

            NetworkTask networkTask = new NetworkTask(testUrl, bitmap, contentValues, response);
            networkTask.execute(null);
        }

        checkPermission();


        /* Get user info */
        Intent intent = getIntent();
        mFacebookID = intent.getStringExtra("fbID");

        /*Get user location & phonenumber*/
        Intent intent2=getIntent();
        phoneNumber=intent2.getStringExtra("phonenumber");
        location=intent2.getStringExtra("location");

        /*Send them to FragmentMyinfo2*/
//        Intent intent3=new Intent(getApplicationContext(),FragmentMyinfo2.class);
//        intent3.putExtra("phonenumber",phoneNumber);
//        intent3.putExtra("location",location);
        Bundle bundle=new Bundle(2);
        bundle.putString("location",location);
        bundle.putString("phonenumber",phoneNumber);
        fragmentMyinfo.setArguments(bundle);

        // Bundle bundle=new Bundle(1);
        //                bundle.putString("writer_name",writer);
        //                fragmentContact.setArguments(bundle);

        /* bottom navigation view click listener */
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView
                .OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
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
    }

    public void checkPermission() {
        String tmp = "";

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED)
            tmp += Manifest.permission.READ_CONTACTS + " ";
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED)
//            tmp += Manifest.permission.READ_EXTERNAL_STORAGE + " ";
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED)
//            tmp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
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

        /* Default fragment (home page) */
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragmentHome).commitAllowingStateLoss();
    }

    public static class NetworkTask extends ThreadTask<Void, String> {

        private String mUrl;
        private MyResponse mMyResponse;

        private Bitmap mBitmap;
        private ContentValues mValues;
        private JSONObject mJSONObject;

        public NetworkTask(String url, ContentValues values, MyResponse myResponse) {
            mUrl = url;
            mValues = values;
            mMyResponse = myResponse;
        }

        public NetworkTask(String url, JSONObject jsonObject, MyResponse myResponse) {
            mUrl = url;
            mJSONObject = jsonObject;
            mMyResponse = myResponse;
        }

        public NetworkTask(String url, Bitmap bitmap, ContentValues contentValues, MyResponse myResponse) {
            mUrl = url;
            mBitmap = bitmap;
            mValues = contentValues;
            mMyResponse = myResponse;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void arg) {
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();

            if (mBitmap != null)
                return requestHttpURLConnection.uploadImage(mUrl, mBitmap, mValues);
            else if (mJSONObject != null)
                return requestHttpURLConnection.request(mUrl, mJSONObject);
            else if (mValues != null)
                return requestHttpURLConnection.request(mUrl, mValues);
            else
                return null;
        }

        @Override
        protected void onPostExecute(String result) {
            mMyResponse.response(result);
        }
    }


    /*Image Gallery Code */
    public boolean selectingImage = false;
    public String startTimeID;

    public boolean isSelection() {
        return selectingImage;
    }

    public void startSelectImage(String id) {
        selectingImage = true;
        startTimeID = id;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragmentGallery).commitAllowingStateLoss();
    }

    public void finishSelectImage(com.example.madcampserverapp.ui.gallery.Image image) {
        image.saveExerciseImage(startTimeID);
        selectingImage = false;
        startTimeID = null;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragmentWrite).commitAllowingStateLoss();
    }

}