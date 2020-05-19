package com.example.kuaishou.demokuaishou.login.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.kuaishou.demokuaishou.R;

//该类管理两个Fragment，一个是注册Fragment，一个是登录Fragment,默认显示登录Fragment
public class LoginRegisterActiviy extends AppCompatActivity {
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_register);

        initViewPager();
    }

    private void initViewPager() {
        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new LoginFragmentAdapter(getSupportFragmentManager()));
    }


    //定义接口实现Fragment切换
    public void switchFragment(int index) {
        viewPager.setCurrentItem(index);
    }


    public static void launch(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, LoginRegisterActiviy.class);
        activity.startActivity(intent);
    }
}
