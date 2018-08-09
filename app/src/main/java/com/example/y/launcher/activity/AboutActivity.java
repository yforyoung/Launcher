package com.example.y.launcher.activity;

import android.annotation.SuppressLint;
import android.widget.TextView;
import com.example.y.launcher.R;
import com.example.y.launcher.base.BaseActivity;
import com.example.y.launcher.util.InfoUtil;
import com.example.y.launcher.util.WifiUtil;

public class AboutActivity extends BaseActivity {
    private TextView ip, mac,wlanMac,softVersion,hardVersion;

    @Override
    public void initView() {
        setContentView(R.layout.activity_about);
        ip = findViewById(R.id.ip);
        mac = findViewById(R.id.mac);
        wlanMac=findViewById(R.id.wlan_mac);
        softVersion=findViewById(R.id.soft_version);
        hardVersion=findViewById(R.id.hard_version);

    }

    @SuppressLint({"SetTextI18n", "HardwareIds"})
    @Override
    public void initData() {
        ip.setText("IP地址:     " + WifiUtil.getIP());
        mac.setText("MAC地址:       " + WifiUtil.getMacAddress());
        wlanMac.setText("WLAN MAC地址:     "+WifiUtil.getWlanMacAddress());
        softVersion.setText("软件版本:      "+ InfoUtil.getSoftVersion());
        hardVersion.setText("硬件版本:      "+InfoUtil.getHardVersion());
    }


}
