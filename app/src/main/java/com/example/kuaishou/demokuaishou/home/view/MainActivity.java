package com.example.kuaishou.demokuaishou.home.view;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kuaishou.demokuaishou.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends AppCompatActivity {

    private MainFragmentAdapter mainFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
