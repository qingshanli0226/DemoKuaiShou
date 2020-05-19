package com.example.kuaishou.demokuaishou.login.view;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.kuaishou.demokuaishou.home.view.CityFragment;
import com.example.kuaishou.demokuaishou.home.view.FocusFragment;
import com.example.kuaishou.demokuaishou.home.view.HomeFragment;

public class LoginFragmentAdapter extends FragmentStatePagerAdapter {
    private Fragment[] fragments = new Fragment[]{new LoginFragment(),new RegisterFragment()};
    private String[] titles = new String[]{"关注","发现","同城"};

    public LoginFragmentAdapter(FragmentManager fragmentManager)  {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int i) {
        return fragments[i];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
