package com.example.kuaishou.login.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.kuaishou.demokuaishou.R;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText passwordEditText;
    private EditText nameEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nameEditText = findViewById(R.id.name);
        passwordEditText = findViewById(R.id.password);
        findViewById(R.id.loginButton).setOnClickListener(this);
        findViewById(R.id.logoutButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginButton:
                login();
                break;
            case R.id.logoutButton:
                break;
        }
    }


    private void login() {
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
