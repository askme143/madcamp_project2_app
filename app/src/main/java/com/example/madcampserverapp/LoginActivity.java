package com.example.madcampserverapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    private LoginButton loginButtonfb;
    private Button gotoregButton;
    private Intent data;
    private Button loginBtn;
    private static final String TAG = "LoginActivity";

    CallbackManager callbackManager = CallbackManager.Factory.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        gotoregButton=(Button) findViewById(R.id.button_signup);
        gotoregButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);

            }
        });

        loginBtn=(Button) findViewById(R.id.button_login);
        loginBtn.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent_login=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent_login);
            }
        });

        loginButtonfb = (LoginButton) findViewById(R.id.login_button);
        loginButtonfb.setReadPermissions("email");

        loginButtonfb.setReadPermissions(Arrays.asList("user_status"));



        // Callback registration
        loginButtonfb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e(TAG, "onSucces LoginResult= " + loginResult.getAccessToken().getUserId());


                GraphRequest request = new GraphRequest().newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            Log.e(TAG, "FaceBook onSuccess : " + object.getString("email"));
                            Log.e(TAG, "FaceBook onSuccess : " + object.getString("name"));

                            //서버에 고유 id, email, name 전달
                            //get 방식으로 전달

                            Intent intent_goActive=new Intent(getApplicationContext(),MainActivity.class);
                            //intent_goActive.putExtra( ,,, );
                            startActivity(intent_goActive);



                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,email,name");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                Log.e(TAG, "FaceBook 로그인 취소");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e(TAG, "FaceBook 로그인 에러");
            }
        });


        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if(currentAccessToken == null){
                    Log.e(TAG, "FaceBook onCurrentAccessTokenChanged currentAccessToken is null");
                } else {
                    Log.e(TAG, "FaceBook onCurrentAccessTokenChanged currentAccessToken is not null");
                }
            }
        };

        accessTokenTracker.startTracking();


        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if(isLoggedIn){
            Log.e(TAG, "Facebook 로그인 상태 ");

            //로그인상태일 경우 바로 MainActivity로
            Intent intent_goActive=new Intent(getApplicationContext(),MainActivity.class);
            //intent_goActive.putExtra( ,,, );
            startActivity(intent_goActive);
        } else {
            Log.e(TAG, "Facebook 비 로그인 상태 " );
        }
    }

    /*callbackManager.onActivityResult를 호출하여
    로그인 결과를 callbackManager를 통해 LoginManager에 전달*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }

}
