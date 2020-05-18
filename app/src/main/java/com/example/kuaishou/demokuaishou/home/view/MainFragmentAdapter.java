package com.example.kuaishou.demokuaishou.home.view;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MainFragmentAdapter extends FragmentStatePagerAdapter {
    private Fragment[] fragments = new Fragment[]{new FocusFragment(),new HomeFragment(),new CityFragment()};
    private String[] titles = new String[]{"关注","发现","同城"};

    public MainFragmentAdapter(FragmentManager fragmentManager)  {
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
