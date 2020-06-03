package com.example.kuaishou.demokuaishou.home.view;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kuaishou.demokuaishou.record.view.RecordActivity;
import com.example.kuaishou.demokuaishou.search.view.SearchActivity;
import com.example.kuaishou.demokuaishou.user.KSUserManager;
import com.example.kuaishou.demokuaishou.R;
import com.example.kuaishou.demokuaishou.login.view.LoginRegisterActiviy;
import com.example.kuaishou.demokuaishou.live.view.LiveActivity;
import com.example.kuaishou.demokuaishou.live.view.PreparePlayLiveActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, KSUserManager.ILoginListener {

    private MainFragmentAdapter mainFragmentAdapter;
    private TextView loginTv;
    private ImageView menuImage;
    private SlidingMenu slidingMenu;
    private final int DEFAUT_FRAGMENT = 1;
    private ViewPager viewPager;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginTv = findViewById(R.id.loginTv);
        menuImage = findViewById(R.id.menu);

        loginTv.setOnClickListener(this);
        menuImage.setOnClickListener(this);
        rootView = findViewById(R.id.rootView);
        findViewById(R.id.searchImg).setOnClickListener(this);
        findViewById(R.id.btnRecord).setOnClickListener(this);
        findViewById(R.id.btnLive).setOnClickListener(this);
        findViewById(R.id.btnPlayLive).setOnClickListener(this);

        initViewPager();
        initSlideMenu();
        switchFragment(getIntent());
        updateUIAccordingToLoginStatus();
        KSUserManager.getInstance().addLoginLister(this);
    }

    //当Activity实例存在时，你调用startActivity时，如果Activity的lacunchMode不是默认的标准模式，是signeltTask模式
    //这个Acitivty将不会创建，即onCreate方法不会执行，执行onNewIntent方法，并且将Activity显示出来.
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        switchFragment(intent);
        updateUIAccordingToLoginStatus();
    }

    private void switchFragment(Intent intent) {
        int fragmentIndex = intent.getIntExtra("fragmentIndex", -1);
        viewPager.setCurrentItem(fragmentIndex);
    }

    private void initSlideMenu() {
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMenu(R.layout.slide_menu);

        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setBehindOffset(300);
        slidingMenu.attachToActivity(this,SlidingMenu.SLIDING_WINDOW);
        ImageView avatarImage = slidingMenu.getMenu().findViewById(R.id.avatar);
        avatarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"点击头像", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUIAccordingToLoginStatus() {
        TextView  nameTv;
        nameTv = slidingMenu.getMenu().findViewById(R.id.name);
        if (KSUserManager.getInstance().isLogin()) {
            menuImage.setVisibility(View.VISIBLE);
            loginTv.setVisibility(View.GONE);
        } else {
            loginTv.setVisibility(View.VISIBLE);
            menuImage.setVisibility(View.GONE);
        }

        if (KSUserManager.getInstance().isLogin()) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
            nameTv.setText(KSUserManager.getInstance().getUserName());
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
            nameTv.setText("");

        }

        //当登录状态改变后，首页要进到默认Fragment下
        viewPager.setCurrentItem(DEFAUT_FRAGMENT);

    }

    private void initViewPager() {
        mainFragmentAdapter = new MainFragmentAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        viewPager.setAdapter(mainFragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginTv:
                //跳转到登录界面
                LoginRegisterActiviy.launch(this);
                //finish();//模拟系统回收MainActivity

                break;
            case R.id.menu:
                //显示侧滑菜单

                break;

            case R.id.searchImg:
                SearchActivity.launch(this);
                break;

            case R.id.btnRecord:
                RecordActivity.launch(this);
                break;

            case R.id.btnLive:
                LiveActivity.launch(this);
                break;

            case R.id.btnPlayLive:
                PreparePlayLiveActivity.launch(this);
                break;
        }
    }

    @Override
    public void onLogin() {
        //updateUIAccordingToLoginStatus();
    }

    @Override
    public void onLogout() {
        //updateUIAccordingToLoginStatus();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        KSUserManager.getInstance().removeLoginListener(this);
    }

    //fragmentIndex代表的是显示Activity里的那个Fragment
    public static void launch(Activity activity, int fragmentIndex) {
        Intent intent = new Intent();
        intent.setClass(activity, MainActivity.class);
        intent.putExtra("fragmentIndex", fragmentIndex);

        activity.startActivity(intent);
    }
}
