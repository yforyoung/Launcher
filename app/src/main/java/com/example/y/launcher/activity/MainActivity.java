package com.example.y.launcher.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.y.launcher.R;
import com.example.y.launcher.base.BaseActivity;
import com.example.y.launcher.service.RecordService;
import com.example.y.launcher.util.AnimateUtil;
import com.example.y.launcher.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends BaseActivity implements View.OnFocusChangeListener, View.OnClickListener {
    private static final String TAG = "MainActivity";
    private LinearLayout setting, apps, videoMeeting, netSysSet, refreshSys, fileManager, recordingShow;
    private TextView time, date;
    private String[] permissions = new String[]{
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.RECORD_AUDIO",
            "android.permission.ACCESS_FINE_LOCATION"
    };
    private List<String> pList = new ArrayList<>();
    private MediaProjectionManager manager;
    private RecordService recordService;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            getTime();
            handler.sendEmptyMessageDelayed(0, 1000);
            return true;
        }
    });
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            RecordService.RecordBinder binder = (RecordService.RecordBinder) service;
            recordService = binder.getService();
            WindowManager windowManager = getWindowManager();
            DisplayMetrics metrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(metrics);
            recordService.setConfig(metrics.widthPixels, metrics.heightPixels, metrics.densityDpi);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };


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
        recordingShow = findViewById(R.id.recording_show);
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
        //request();
        requestPermission();
        handler.sendEmptyMessage(0);
        Intent intent5 = new Intent(this, RecordService.class);
        bindService(intent5, connection, BIND_AUTO_CREATE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]);
                    if (showRequestPermission) {
                        Toast.makeText(this, "您拒绝了权限!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG, "onKeyDown: " + keyCode);
        if (keyCode == 4) {
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
                Intent intent = new Intent(this, SettingActivity.class);
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
                // ToastUtil.showToast("系统更新");
                if (recordService.isRunning()) {
                    new AlertDialog.Builder(this)
                            .setTitle("提示")
                            .setMessage("是否停止录屏？")
                            .setPositiveButton("停止录屏", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    stopRecord();
                                }
                            })
                            .setNegativeButton("取消", null)
                            .create().show();
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle("提示")
                            .setMessage("是否开始录屏？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startRecord();
                                }
                            })
                            .setNegativeButton("取消", null)
                            .create().show();
                }
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

    private void stopRecord() {
        // recordingShow.setVisibility(View.GONE);
        recordService.stopRecord();
        ToastUtil.showToast("已保存："+recordService.getSaveDirectory());
        unbindService(connection);
    }

    private void startRecord() {
        manager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        assert manager != null;
        Intent recordIntent = manager.createScreenCaptureIntent();
        startActivityForResult(recordIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            assert manager != null;
            MediaProjection projection = manager.getMediaProjection(resultCode, data);
            recordService.setProjection(projection);
            recordService.startRecord();
            ToastUtil.showToast("屏幕录制中");
            //recordingShow.setVisibility(View.VISIBLE); 改成通知栏
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void requestPermission() {         //请求权限
        pList.clear();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                pList.add(permission);
            }
        }
        if (!pList.isEmpty()) {
            String[] permissions = pList.toArray(new String[pList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
    }

    private void getTime() {
        Date d = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        time.setText(sdfTime.format(d));
        date.setText(sdfDate.format(d));
    }
}
