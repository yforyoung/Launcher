package com.example.y.launcher.activity;

import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import com.example.y.launcher.R;
import com.example.y.launcher.base.BaseActivity;

public class SettingActivity extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private TextView bluetooth,application,language,timeToOpen,timeSet,about;

    @Override
    public void initView() {
        setContentView(R.layout.activity_setting);
        bluetooth = findViewById(R.id.bluetooth);
        application = findViewById(R.id.application);
        language = findViewById(R.id.language);
        timeToOpen = findViewById(R.id.time_to_open);
        timeSet=findViewById(R.id.time_set);
        about=findViewById(R.id.about);
    }

    @Override
    public void initListener() {
        bluetooth.setOnClickListener(this);
        application.setOnClickListener(this);
        language.setOnClickListener(this);
        timeToOpen.setOnClickListener(this);
        timeSet.setOnClickListener(this);
        about.setOnClickListener(this);

        bluetooth.setOnFocusChangeListener(this);
        application.setOnFocusChangeListener(this);
        language.setOnFocusChangeListener(this);
        timeToOpen.setOnFocusChangeListener(this);
        timeSet.setOnFocusChangeListener(this);
        about.setOnFocusChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bluetooth:
                jump2Activity(Settings.ACTION_BLUETOOTH_SETTINGS);
                break;
            case R.id.application:
                jump2Activity(Settings.ACTION_APPLICATION_SETTINGS);
                break;
            case R.id.language:
                jump2Activity(Settings.ACTION_INPUT_METHOD_SETTINGS );
                break;
            case R.id.time_to_open:
                jump2Activity(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
                break;
            case R.id.time_set:
                jump2Activity(Settings.ACTION_DATE_SETTINGS);
                break;
            case R.id.about:
                jump2Activity(Settings.ACTION_DEVICE_INFO_SETTINGS);
                break;
        }
    }

    public void jump2Activity(String action) {
        Intent intent = new Intent();
        intent.setAction(action);
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
