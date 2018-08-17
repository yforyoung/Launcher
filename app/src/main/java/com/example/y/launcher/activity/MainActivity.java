package com.example.y.launcher.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.y.launcher.R;
import com.example.y.launcher.base.BaseActivity;
import com.example.y.launcher.util.AnimateUtil;
import com.example.y.launcher.util.ToastUtil;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends BaseActivity implements View.OnFocusChangeListener, View.OnClickListener {
    private static final String TAG = "MainActivity";
    private LinearLayout setting, apps, videoMeeting, netSysSet, refreshSys, fileManager;
    private TextView time, date;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            getTime();
            handler.sendEmptyMessageDelayed(0,1000);
            return true;
        }
    });
    @Override
    public void initView() {
        setContentView(R.layout.activity_main);
        setting = findViewById(R.id.setting);
        apps = findViewById(R.id.apps);
        videoMeeting = findViewById(R.id.video_meeting);
        netSysSet = findViewById(R.id.net_sys_set);
        refreshSys = findViewById(R.id.refresh_sys);
        fileManager = findViewById(R.id.file_manager);
        time = findViewById(R.id.time);
        date = findViewById(R.id.date);
    }

    @Override
    public void initListener() {
        setting.setOnClickListener(this);
        apps.setOnClickListener(this);
        videoMeeting.setOnClickListener(this);
        netSysSet.setOnClickListener(this);
        fileManager.setOnClickListener(this);
        refreshSys.setOnClickListener(this);

        setting.setOnFocusChangeListener(this);
        apps.setOnFocusChangeListener(this);
        videoMeeting.setOnFocusChangeListener(this);
        netSysSet.setOnFocusChangeListener(this);
        fileManager.setOnFocusChangeListener(this);
        refreshSys.setOnFocusChangeListener(this);

        setting.setFocusableInTouchMode(true);
        apps.setFocusableInTouchMode(true);
        videoMeeting.setFocusableInTouchMode(true);
        netSysSet.setFocusableInTouchMode(true);
        fileManager.setFocusableInTouchMode(true);
        refreshSys.setFocusableInTouchMode(true);
        videoMeeting.requestFocus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    public void initData() {
        request();
        handler.sendEmptyMessage(0);
    }


    private void getTime() {
        Date d = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        time.setText(sdfTime.format(d));
        date.setText(sdfDate.format(d));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG, "onKeyDown: "+keyCode);
        if (keyCode==4) {
            videoMeeting.requestFocus();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        AnimateUtil.onFocusAnimation(v, hasFocus);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting:
                Intent intent = new Intent(this,SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.apps:
                Intent intent1 = new Intent(this, AppsActivity.class);
                startActivity(intent1);
                break;
            case R.id.video_meeting:
                ComponentName componentName = new ComponentName("com.paradisecloudtech.vc", "com.paradisecloudtech.vc.mvp.activity.SplashActivity");
                Intent intent2 = new Intent();
                intent2.setComponent(componentName);
                startActivity(intent2);
                break;
            case R.id.net_sys_set:
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                break;
            case R.id.refresh_sys:
                ToastUtil.showToast("系统更新");
                //startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                break;
            case R.id.file_manager:
                ComponentName componentName1 = new ComponentName("com.mediatek.filemanager", "com.mediatek.filemanager.FileManagerOperationActivity");
                Intent intent3 = new Intent();
                intent3.setComponent(componentName1);
                startActivity(intent3);
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
