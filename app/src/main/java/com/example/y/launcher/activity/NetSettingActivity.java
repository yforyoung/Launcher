package com.example.y.launcher.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
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
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.example.y.launcher.R;
import com.example.y.launcher.adapter.WifiAdapter;
import com.example.y.launcher.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class NetSettingActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, WifiAdapter.OnItemClickListener {
    private Switch wifiSwitch;
    private WifiManager wifiManager;
    private List<ScanResult> wifiList;
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
                    wifiManager.startScan();
                }
            } else if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                for (ScanResult sr : wifiManager.getScanResults()) {
                    if (!sr.SSID.trim().equals("")) {
                        wifiList.add(sr);
                    }
                }
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
        ScanResult sr = wifiList.get(tag);
        Log.i("info", "onItemClick: " + sr.level + "  " + sr.capabilities);
    }

    private void registerBrodCastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(wifiReceiver, filter);
    }

}
