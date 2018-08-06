package com.example.y.launcher.activity;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import com.example.y.launcher.R;
import com.example.y.launcher.base.BaseActivity;

public class NetSettingActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    private Switch wifiSwitch;
    private WifiManager wifiManager;


    @Override
    public void initView() {
        setContentView(R.layout.activity_net_setting);
        wifiSwitch=findViewById(R.id.wifi_switch);
    }

    @Override
    public void initData() {
        wifiManager= (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    @Override
    public void initListener() {
        wifiSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        wifiManager.setWifiEnabled(isChecked);
    }
}
