package com.example.kuaishou.demokuaishou;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.kuaishou.demokuaishou.home.view.MainActivity;

public class AdrActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageView imageView = new ImageView(this);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(layoutParams);
        imageView.setImageResource(R.mipmap.adr);
        setContentView(imageView);

        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                finish();//广告页显示3秒钟结束
            }
        }.sendEmptyMessageDelayed(0, 3000);
    }

    public static void launch(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, AdrActivity.class);
        activity.startActivity(intent);
    }
}
