package com.example.madcampserverapp.ui.userinfo;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.madcampserverapp.LoginActivity;
import com.example.madcampserverapp.MainActivity;
import com.example.madcampserverapp.R;
import com.example.madcampserverapp.ThreadTask;
import com.example.madcampserverapp.server.MyResponse;
import com.example.madcampserverapp.server.RequestHttpURLConnection;
import com.example.madcampserverapp.ui.home.Post;
import com.example.madcampserverapp.ui.write.FragmentWrite;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.ArrayList;

public class FragmentMyinfo2 extends Fragment {
    private static final String TAG = "FragmentMyinfo2";
    private String location;
    private String phonenumber;
    private TextView tv1;
    private TextView tv2;
    private ArrayList<Post> postArrayList;

    AccessTokenTracker accessTokenTracker;
    CallbackManager callbackManager = CallbackManager.Factory.create();

    private LoginButton loginButtonfb2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myinfo, container, false);

        tv1=(TextView) view.findViewById(R.id.location_person);
        tv2=(TextView) view.findViewById(R.id.phonenumber_person);

        //Bundle
        Bundle bundle=getArguments();
        if (bundle!=null){
            location=bundle.getString("location");
            phonenumber=bundle.getString("phonenumber");
        }
        //tv setText
        tv1.setText(location);
        tv2.setText(phonenumber);


        loginButtonfb2 = (LoginButton) view.findViewById(R.id.login_button2);

        loginButtonfb2.setReadPermissions("email");
        // If using in a fragment
        loginButtonfb2.setFragment(this);

        // Callback registration
        loginButtonfb2.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });


        /* Build Access Tracker of FB */
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

                if (currentAccessToken == null) {
                    Log.e(TAG, "FaceBook onCurrentAccessTokenChanged currentAccessToken is null"); //로그아웃 시

                    Intent intent_goLogin = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent_goLogin);
                } else {
                    Log.e(TAG, "FaceBook onCurrentAccessTokenChanged currentAccessToken is not null");
                }
                /* Start tracking */
                accessTokenTracker.startTracking();

            }

        };
        return view;
    }


}
