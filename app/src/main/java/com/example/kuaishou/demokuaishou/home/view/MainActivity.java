package com.example.kuaishou.demokuaishou.home.view;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kuaishou.demokuaishou.AdrActivity;
import com.example.kuaishou.demokuaishou.base.BaseActivity;
import com.example.kuaishou.demokuaishou.cache.CacheManager;
import com.example.kuaishou.demokuaishou.record.view.RecordActivity;
import com.example.kuaishou.demokuaishou.search.view.SearchMVPActivity;
import com.example.kuaishou.demokuaishou.user.KSUserManager;
import com.example.kuaishou.demokuaishou.R;
import com.example.kuaishou.demokuaishou.login.view.LoginRegisterActiviy;
import com.example.kuaishou.demokuaishou.live.view.LiveActivity;
import com.example.kuaishou.demokuaishou.live.view.PreparePlayLiveActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.umeng.analytics.MobclickAgent;

public class MainActivity extends BaseActivity implements View.OnClickListener, KSUserManager.ILoginListener, NavigationBar.ISelectedListener {

    private TextView loginTv;
    private ImageView menuImage;
    private SlidingMenu slidingMenu;
    private View rootView;
    private NavigationBar navigationBar;

    private Fragment currentFragment;//当前正在显示的Fragment
    private Fragment[] fragments = new Fragment[]{ new FocusFragment(), new HomeFragment(), new CityFragment()};

    @Override
    protected void create() {

        //默认进入HomeFragment
        switchFragmentByIndex(1);
    }

    @Override
    protected void initView() {
        loginTv = findViewById(R.id.loginTv);
        menuImage = findViewById(R.id.menu);

        loginTv.setOnClickListener(this);
        menuImage.setOnClickListener(this);
        rootView = findViewById(R.id.rootView);
        findViewById(R.id.searchImg).setOnClickListener(this);
        findViewById(R.id.btnRecord).setOnClickListener(this);
        findViewById(R.id.btnLive).setOnClickListener(this);
        findViewById(R.id.btnPlayLive).setOnClickListener(this);

        //initViewPager();
        initNavigationView();

        initSlideMenu();
        switchFragmentByIndex(getIntent().getIntExtra("fragmentIndex",0));
        navigationBar.setSelectedColor(getIntent().getIntExtra("fragmentIndex",0));

        updateUIAccordingToLoginStatus();
        KSUserManager.getInstance().addLoginLister(this);
    }

    private void initNavigationView() {
        navigationBar = findViewById(R.id.navigationBar);
        navigationBar.setiSelectedListener(this);
        navigationBar.setTabTile(new String[]{"1712", "1710", "1704"});
    }


    @Override
    public void onTabSelected(int index) {
        switchFragmentByIndex(index);

    }

    private void switchFragmentByIndex(int index) {
        Fragment fragment = fragments[index];
        //如果要切换的Fragment，已经显示出来了，就直接返回
        if (fragment == null || fragment == currentFragment) {
            return;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (currentFragment!=null) {
            fragmentTransaction.hide(currentFragment);//先隐藏当前显示的Fragment
        }

        if (fragment.isAdded()) {//如果说之前已经添加到系统中
            fragmentTransaction.show(fragment).commit();//直接显示出来
        } else {//如果说之前没有添加到系统中，现在就添加到系统中，添加后，会自动显示出来
            fragmentTransaction.add(R.id.frameLayoutId, fragment).commit();
        }

        currentFragment = fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    //当Activity实例存在时，你调用startActivity时，如果Activity的lacunchMode不是默认的标准模式，是signeltTask模式
    //这个Acitivty将不会创建，即onCreate方法不会执行，执行onNewIntent方法，并且将Activity显示出来.
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        switchFragmentByIndex(getIntent().getIntExtra("fragmentIndex",0));
        navigationBar.setSelectedColor(getIntent().getIntExtra("fragmentIndex",0));
        updateUIAccordingToLoginStatus();
    }

    @Override
    protected void resume() {
        super.resume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void pause() {
        super.pause();
        MobclickAgent.onPause(this);
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
        switchFragmentByIndex(1);

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
                SearchMVPActivity.launch(this);
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
    protected void destroy() {
        super.destroy();
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
