package com.example.madcampserverapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.madcampserverapp.server.MyResponse;
import com.example.madcampserverapp.ui.home.FragmentHome;
import com.example.madcampserverapp.ui.userinfo.FragmentMyinfo2;
import com.example.madcampserverapp.ui.write.FragmentWrite;

import com.example.madcampserverapp.server.RequestHttpURLConnection;

import com.example.madcampserverapp.ui.contact.FragmentContact;
import com.example.madcampserverapp.ui.gallery.FragmentGallery;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Ignore: Code for testing */
        String testUrl = url + "/gallery/upload";
        Bitmap bitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.contact_icon)).getBitmap();
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

        /* Get user info */
        Intent intent = getIntent();
        mFacebookID = intent.getStringExtra("fbID");

        /* Make fragment objects */
        fragmentContact = new FragmentContact();
        fragmentGallery = new FragmentGallery();
        fragmentHome = new FragmentHome();
        fragmentWrite = new FragmentWrite();
        fragmentMyinfo = new FragmentMyinfo2();

        /* Default fragment (home page) */
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragmentHome).commitAllowingStateLoss();

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

    public String getFacebookID() {
        return mFacebookID;
    }

    public String getUrl() {
        return url;
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
                return requestHttpURLConnection.upload(mUrl, mBitmap, mValues);
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
}