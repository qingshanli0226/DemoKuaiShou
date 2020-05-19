package com.example.kuaishou.demokuaishou.home.view;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kuaishou.demokuaishou.KSUserManager;
import com.example.kuaishou.demokuaishou.R;
import com.example.kuaishou.demokuaishou.login.view.LoginRegisterActiviy;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MainFragmentAdapter mainFragmentAdapter;
    private TextView loginTv;
    private ImageView menuImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginTv = findViewById(R.id.loginTv);
        menuImage = findViewById(R.id.menu);

        loginTv.setOnClickListener(this);
        menuImage.setOnClickListener(this);
        if (KSUserManager.getInstance().isLogin()) {
            menuImage.setVisibility(View.VISIBLE);
            loginTv.setVisibility(View.GONE);
        } else {
            loginTv.setVisibility(View.VISIBLE);
            menuImage.setVisibility(View.GONE);
        }
        initViewPager();
        initSlideMenu();
    }

    private void initSlideMenu() {
        SlidingMenu slidingMenu = new SlidingMenu(this);
        slidingMenu.setMenu(R.layout.slide_menu);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setBehindOffset(500);
        slidingMenu.attachToActivity(this,SlidingMenu.SLIDING_WINDOW);

        ImageView avatarImage = slidingMenu.getMenu().findViewById(R.id.avatar);
        avatarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"点击头像", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViewPager() {
        mainFragmentAdapter = new MainFragmentAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        viewPager.setAdapter(mainFragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginTv:
                //跳转到登录界面
                LoginRegisterActiviy.launch(this);

                break;
            case R.id.menu:
                //显示侧滑菜单
        }
    }
}
