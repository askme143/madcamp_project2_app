package com.example.madcampserverapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.madcampserverapp.ui.contact.FragmentContact;
import com.example.madcampserverapp.ui.gallery.FragmentGallery;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private FragmentContact fragmentContact;
    private FragmentGallery fragmentGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentContact = new FragmentContact();
        fragmentGallery = new FragmentGallery();

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
                    case R.id.free:{
                        return true;
                    }
                    default: return false;
                }
            }
        });
    }
}