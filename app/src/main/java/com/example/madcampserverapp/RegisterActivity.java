package com.example.madcampserverapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private EditText reg_passwd, reg_email, reg_name;
    private Button btn_reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        reg_passwd = findViewById(R.id.reg_passwd);
        reg_email = findViewById(R.id.reg_email);
        reg_name = findViewById(R.id.reg_name);

        //회원가입 버튼 클릭시
        btn_reg = findViewById(R.id.reg_btn);
        btn_reg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String userPasswd = reg_passwd.getText().toString(); //edittext값 전달
                String userName = reg_name.getText().toString();
                String userEmail = reg_email.getText().toString();

            }

        });
    }

}
