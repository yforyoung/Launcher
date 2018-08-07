package com.example.y.launcher.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.y.launcher.R;
import com.example.y.launcher.adapter.WifiAdapter;
import com.example.y.launcher.base.BaseActivity;
import com.example.y.launcher.beans.Wifi;
import com.example.y.launcher.util.WifiSetUtil;

import java.util.ArrayList;
import java.util.List;

public class NetSettingActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, WifiAdapter.OnItemClickListener {
    private Switch wifiSwitch;
    private WifiManager wifiManager;
    private List<Wifi> wifiList;
    private RecyclerView wifiRecyclerView;
    private WifiAdapter adapter;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    adapter.notifyDataSetChanged();
                    break;
                case 1:
                    break;
            }
        }
    };

    private BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            assert action != null;
            if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);
                if (state == WifiManager.WIFI_STATE_DISABLED) {
                    Log.i("info", "onReceive: 关闭");
                } else if (state == WifiManager.WIFI_STATE_ENABLED) {
                    Log.i("info", "onReceive: 开启");
                    WifiSetUtil.startScan();
                }
            } else if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                /*for (ScanResult sr : WifiSetUtil.getWifiList()) {
                    if (!sr.SSID.trim().equals("")) {
                        Wifi wifi=new Wifi();
                        wifi.setSSID(sr.SSID);
                        wifi.setLevel();
                    }
                }*/
                wifiList.addAll(WifiSetUtil.getWifiList());
                handler.sendEmptyMessage(0);
            }
        }
    };

    @Override
    public void initView() {
        setContentView(R.layout.activity_net_setting);
        wifiSwitch = findViewById(R.id.wifi_switch);
        wifiRecyclerView = findViewById(R.id.wifi_list);
    }

    @Override
    public void initData() {
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        assert wifiManager != null;
        wifiSwitch.setChecked(wifiManager.isWifiEnabled());
        registerBrodCastReceiver();
        wifiList = new ArrayList<>();
        adapter = new WifiAdapter(wifiList);
        wifiRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        wifiRecyclerView.setAdapter(adapter);
        wifiRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    public void initListener() {
        wifiSwitch.setOnCheckedChangeListener(this);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        wifiManager.setWifiEnabled(isChecked);
        wifiList.clear();
        if (!isChecked) handler.sendEmptyMessage(0);
    }

    @Override
    public void onItemClick(View v, int tag) {
        final Wifi wifi = wifiList.get(tag);
        if (wifi.getType() != WifiSetUtil.ESS) {
            View view = LayoutInflater.from(this).inflate(R.layout.wifi_connect_dialog, null, false);
            TextView wifiSignal = view.findViewById(R.id.wifi_signal);
            TextView wifiCapability = view.findViewById(R.id.wifi_capability);
            final EditText wifiPwd = view.findViewById(R.id.wifi_pwd);
            switch (wifi.getLevel()) {
                case 3:
                    wifiSignal.setText("强");
                    break;
                case 2:
                    wifiSignal.setText("一般");
                    break;
                default:
                    wifiSignal.setText("弱");
                    break;
            }
            wifiCapability.setText(wifi.getCapabilities());

            new AlertDialog.Builder(this)
                    .setView(view)
                    .setPositiveButton("连接", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            wifi.setPwd(wifiPwd.getText().toString());
                            WifiSetUtil.connectWifi(wifi);
                        }
                    })
                    .setNegativeButton("取消", null)
                    .create()
                    .show();
        } else
            WifiSetUtil.connectWifi(wifi);
    }

    private void registerBrodCastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(wifiReceiver, filter);
    }

}
