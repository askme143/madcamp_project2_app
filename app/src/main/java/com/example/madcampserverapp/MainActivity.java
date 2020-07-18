package com.example.madcampserverapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.net.Network;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.madcampserverapp.ui.home.FragmentHome;
import com.example.madcampserverapp.ui.write.FragmentWrite;

import com.example.madcampserverapp.server.NetworkTask;
import com.example.madcampserverapp.server.RequestHttpURLConnection;

import com.example.madcampserverapp.ui.contact.FragmentContact;
import com.example.madcampserverapp.ui.gallery.FragmentGallery;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class MainActivity extends AppCompatActivity {
    private FragmentHome fragmentHome;
    private FragmentWrite fragmentWrite;
    private FragmentContact fragmentContact;
    private FragmentGallery fragmentGallery;
    private NetworkTask networkTask;

    private String url = "http://192.249.19.242:7380";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ContentValues contentValues = new ContentValues();
//        contentValues.put("email", "askme143@kaist.ac.kr");
//        contentValues.put("name", "윤영일");
//        contentValues.put("fb_id", "12321");
//        networkTask = new NetworkTask(url, contentValues);

//        ContentValues contentValues = new ContentValues();
//        contentValues.put("fb_id", "12321");
//        networkTask = new NetworkTask(url, contentValues);
//
//        networkTask.execute(null);

        fragmentContact = new FragmentContact();
        fragmentGallery = new FragmentGallery();
        fragmentHome = new FragmentHome();
        fragmentWrite = new FragmentWrite();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragmentContact).commitAllowingStateLoss();

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
                    default: return false;
                }
            }
        });
    }

    public static class NetworkTask extends ThreadTask<Void, String> {

        private String mUrl;
        private ContentValues mValues;

        public NetworkTask(String url, ContentValues values) {
            mUrl = url;
            mValues = values;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(Void arg) {
            String result;
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();

            result = requestHttpURLConnection.request(mUrl, mValues);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println(result);
        }
    }
}