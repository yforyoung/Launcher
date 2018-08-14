package com.example.y.launcher.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.widget.TextView;
import com.example.y.launcher.R;
import com.example.y.launcher.base.BaseActivity;
import com.example.y.launcher.util.InfoUtil;
import com.example.y.launcher.util.WifiUtil;

import java.util.Objects;

/*关于  未使用*/
public class AboutActivity extends BaseActivity {
    private TextView ip, mac,wlanMac,softVersion,hardVersion,gateWay,netMask;

    @Override
    public void initView() {
        setContentView(R.layout.activity_about);
        ip = findViewById(R.id.ip);
        mac = findViewById(R.id.mac);
        wlanMac=findViewById(R.id.wlan_mac);
        softVersion=findViewById(R.id.soft_version);
        hardVersion=findViewById(R.id.hard_version);
        gateWay=findViewById(R.id.gate_way);
        netMask=findViewById(R.id.net_mask);

        registerReceiver();
    }

    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Objects.equals(intent.getAction(), WifiManager.WIFI_STATE_CHANGED_ACTION)){
                initData();
            }
        }
    };

    private void registerReceiver(){
        IntentFilter filter=new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(receiver,filter);
    }

    @SuppressLint({"SetTextI18n", "HardwareIds"})
    @Override
    public void initData() {
        ip.setText("IP地址:     " + WifiUtil.getIP());
        gateWay.setText("网关:        "+WifiUtil.getGateWay());
        netMask.setText("子网掩码:      "+WifiUtil.getNetMask());
        mac.setText("MAC地址:       " + InfoUtil.getMacAddress());
        wlanMac.setText("WLAN MAC地址:     "+WifiUtil.getWlanMacAddress());
        softVersion.setText("软件版本:      "+ InfoUtil.getSoftVersion());
        hardVersion.setText("硬件版本:      "+InfoUtil.getHardVersion());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
