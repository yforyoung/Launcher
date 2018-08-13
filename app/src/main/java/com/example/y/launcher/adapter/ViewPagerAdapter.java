package com.example.y.launcher.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.y.launcher.fragment.FirstFragment;
import com.example.y.launcher.fragment.SecondFragment;


public class ViewPagerAdapter extends FragmentPagerAdapter {
    Fragment f[]=new Fragment[]{new FirstFragment(),new SecondFragment()};
   // String title[]=new String[]{"主页","第二页"};

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        return f[position];
    }

    @Override
    public int getCount() {
        return 2;
    }
/*
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }*/
}
