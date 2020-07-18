package com.example.madcampserverapp.ui.userinfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.madcampserverapp.R;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;

public class FragmentMyinfo2 extends Fragment {
    private LoginButton loginButtonfb2;
    private Intent data;
    private static final String TAG = "LoginActivity";

    CallbackManager callbackManager = CallbackManager.Factory.create();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view= inflater.inflate(R.layout.fragment_myinfo,container,false);

        loginButtonfb2=(LoginButton) view.findViewById(R.id.login_button2);




        return view;

    }



}
