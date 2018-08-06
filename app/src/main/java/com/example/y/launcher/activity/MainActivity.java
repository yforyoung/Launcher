package com.example.y.launcher.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;

import com.example.y.launcher.LauncherViewPager;
import com.example.y.launcher.R;
import com.example.y.launcher.adapter.ViewPagerAdapter;
import com.example.y.launcher.base.BaseActivity;
import com.example.y.launcher.util.ToastUtil;

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
        request();
        manager = getSupportFragmentManager();
        ViewPagerAdapter adapter = new ViewPagerAdapter(manager);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        tabHead.setupWithViewPager(viewPager);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==1){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
                ToastUtil.showToast("权限开启");
            else
                ToastUtil.showToast("拒绝权限将无法正常使用");
        }
    }
    private void request() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) { //表示未授权时
            //进行授权
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }
}
