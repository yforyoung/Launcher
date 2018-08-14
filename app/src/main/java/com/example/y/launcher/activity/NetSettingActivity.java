package com.example.y.launcher.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.example.y.launcher.util.SpfUtil;
import com.example.y.launcher.util.ToastUtil;
import com.example.y.launcher.util.WifiComparator;
import com.example.y.launcher.util.WifiUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/*未使用 自定义网络设置*/
public class NetSettingActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, WifiAdapter.OnItemClickListener, View.OnFocusChangeListener {
    private Switch wifiSwitch;
    private List<Wifi> wifiList;
    private RecyclerView wifiRecyclerView;
    private WifiAdapter adapter;
    private static final String TAG = "NetSettingActivity";

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Collections.sort(wifiList, new WifiComparator());
                    adapter.notifyDataSetChanged();
                    break;
                case 1:
                    //sendEmptyMessageDelayed(1, 1000 * 20);
                    break;
            }
        }
    };

    private BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            assert action != null;
            switch (action) {
                case WifiManager.WIFI_STATE_CHANGED_ACTION:
                    int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);
                    if (state == WifiManager.WIFI_STATE_ENABLED) {
                        //Log.i("info", "onReceive: 开启");
                        WifiUtil.startScan();
                    }
                    break;
                case WifiManager.SCAN_RESULTS_AVAILABLE_ACTION:
                    wifiList.clear();
                    wifiList.addAll(WifiUtil.getWifiList());
                    handler.sendEmptyMessage(0);
                    break;
                case WifiManager.EXTRA_SUPPLICANT_CONNECTED:
                    handler.sendEmptyMessage(0);
                    break;
                case WifiManager.EXTRA_SUPPLICANT_ERROR:
                    ToastUtil.showToast("失败");
                    break;
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
        wifiSwitch.setChecked(WifiUtil.isWifiEnabled());
        wifiList = new ArrayList<>();
        adapter = new WifiAdapter(wifiList);
        wifiRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        wifiRecyclerView.setAdapter(adapter);
        wifiRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        registerBrodCastReceiver();
    }


    @Override
    public void initListener() {
        wifiSwitch.setOnCheckedChangeListener(this);
        adapter.setOnItemClickListener(this);
        wifiSwitch.setOnFocusChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        WifiUtil.setWifiEnabled(isChecked);
        wifiList.clear();
        if (!isChecked)
            handler.sendEmptyMessage(0);
    }

    @Override
    public void onItemClick(View v, int tag) {
        Wifi wifi = wifiList.get(tag);
        if (wifi.isConnect()) {
            showCutWifiDialog(wifi);
        } else {
            if (wifi.isSave()) {
                wifi.setPwd(SpfUtil.getString(wifi.getSSID(),""));
                for (Wifi w : wifiList) {
                    if (w.isConnect()) {
                        WifiUtil.cutWifiConnection(w);
                    }
                }
                showConnectSavedWifiDialog(wifi);
            } else {
                showConnectWifiDialog(wifi);
            }
        }

    }

    private void showConnectWifiDialog(final Wifi wifi) {
        if (wifi.getType() != WifiUtil.ESS) {
            @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.wifi_connect_dialog, null, false);
            TextView wifiSignal = view.findViewById(R.id.wifi_signal);
            TextView wifiCapability = view.findViewById(R.id.wifi_capability);
            final EditText wifiPwd = view.findViewById(R.id.wifi_pwd);
            switch (wifi.getLevel()) {
                case 3:
                    wifiSignal.setText("强");
                    break;
                case 0:
                    wifiSignal.setText("弱");
                    break;
                default:
                    wifiSignal.setText("一般");
                    break;
            }
            wifiCapability.setText(wifi.getCapabilities());

            new AlertDialog.Builder(this)
                    .setView(view)
                    .setTitle(wifi.getSSID())
                    .setPositiveButton("连接", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            wifi.setPwd(wifiPwd.getText().toString());
                           // wifi.setConnect(true);
                            SpfUtil.putString(wifi.getSSID(), wifi.getPwd());
                            handler.sendEmptyMessage(0);
                        }
                    })
                    .setNegativeButton("取消", null)
                    .create()
                    .show();

        }
        WifiUtil.connectWifi(wifi);
    }

    private void showConnectSavedWifiDialog(final Wifi wifi) {
        new AlertDialog.Builder(this)
                .setTitle(wifi.getSSID())
                .setPositiveButton("连接", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WifiUtil.connectWifiSaved(wifi);
                        //wifi.setConnect(true);
                        Log.i(TAG, "onClick: connect");
                        handler.sendEmptyMessage(0);
                    }
                })
                .setNegativeButton("取消", null)
                .create()
                .show();
    }

    private void showCutWifiDialog(final Wifi wifi) {
        new AlertDialog.Builder(this)
                .setTitle(wifi.getSSID())
                .setPositiveButton("断开", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WifiUtil.cutWifiConnection(wifi);
                        handler.sendEmptyMessage(0);
                    }
                })
                .setNegativeButton("取消", null)
                .create()
                .show();
    }

    private void registerBrodCastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.EXTRA_SUPPLICANT_CONNECTED);
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiReceiver);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        //TODO
    }
}
