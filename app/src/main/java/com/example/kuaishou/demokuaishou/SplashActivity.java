package com.example.kuaishou.demokuaishou;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.example.kuaishou.demokuaishou.home.view.MainActivity;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textView = new TextView(this);
        setContentView(textView);


        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                MainActivity.launch(SplashActivity.this, 0);
                finish();
            }
        }.sendEmptyMessageDelayed(0, 1000);
    }
}
