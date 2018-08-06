package com.example.y.launcher.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;

import com.example.y.launcher.LauncherViewPager;
import com.example.y.launcher.R;
import com.example.y.launcher.adapter.ViewPagerAdapter;
import com.example.y.launcher.base.BaseActivity;

public class MainActivity extends BaseActivity {
    private LauncherViewPager viewPager;
    private FragmentManager manager;
    private TabLayout tabHead;


    @Override
    public void initView() {
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.view_pager);
        tabHead = findViewById(R.id.tab_head);
    }

    @Override
    public void initData() {
        manager = getSupportFragmentManager();
        ViewPagerAdapter adapter = new ViewPagerAdapter(manager);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        tabHead.setupWithViewPager(viewPager);
    }
}
