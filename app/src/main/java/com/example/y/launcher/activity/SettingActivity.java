package com.example.y.launcher.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.TextView;

import com.example.y.launcher.R;
import com.example.y.launcher.base.BaseActivity;
import com.example.y.launcher.util.AnimateUtil;

public class SettingActivity extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private TextView netSet;
    private TextView info;
    private TextView sysSet;
    private TextView developerSet;

    @Override
    public void initView() {
        setContentView(R.layout.activity_setting);
        netSet = findViewById(R.id.net_set);
        info = findViewById(R.id.info);
        sysSet = findViewById(R.id.sys_set);
        developerSet = findViewById(R.id.developer_set);
    }

    @Override
    public void initListener() {
        netSet.setOnClickListener(this);
        info.setOnClickListener(this);
        sysSet.setOnClickListener(this);
        developerSet.setOnClickListener(this);
        netSet.setOnFocusChangeListener(this);
        info.setOnFocusChangeListener(this);
        sysSet.setOnFocusChangeListener(this);
        developerSet.setOnFocusChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.net_set:
                jump2Activity(NetSettingActivity.class);
                break;
            case R.id.info:
                jump2Activity(AboutActivity.class);
                break;
        }
    }

    public void jump2Activity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus){
            v.setBackgroundResource(R.color.colorSetFocusBg);
        }else{
            v.setBackgroundResource(R.color.colorSetBg);
        }
    }
}
