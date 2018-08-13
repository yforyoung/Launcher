package com.example.y.launcher.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.y.launcher.R;
import com.example.y.launcher.activity.AppsActivity;
import com.example.y.launcher.activity.SettingActivity;
import com.example.y.launcher.base.BaseFragment;
import com.example.y.launcher.util.AnimateUtil;
import com.example.y.launcher.util.ToastUtil;


public class FirstFragment extends BaseFragment implements View.OnFocusChangeListener, View.OnClickListener {
    private LinearLayout setting,apps,videoMeeting,netSysSet,refreshSys,fileManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first,container,false);
    }

    @Override
    public void initView() {
        setting = getActivity().findViewById(R.id.setting);
        apps = getActivity().findViewById(R.id.apps);
        videoMeeting=getActivity().findViewById(R.id.video_meeting);
        netSysSet=getActivity().findViewById(R.id.net_sys_set);
        refreshSys=getActivity().findViewById(R.id.refresh_sys);
        fileManager=getActivity().findViewById(R.id.file_manager);
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
    }

    @Override
    public void initData() {
        videoMeeting.requestFocus();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        AnimateUtil.onFocusAnimation(v,hasFocus);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting:
                Intent intent=new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
                break;
            case R.id.apps:
                Intent intent1=new Intent(getActivity(),AppsActivity.class);
                startActivity(intent1);
                break;
            case R.id.video_meeting:
                ComponentName componentName=new ComponentName("com.paradisecloudtech.vc","com.paradisecloudtech.vc.mvp.activity.SplashActivity");
                Intent intent2=new Intent();
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
                ComponentName componentName1=new ComponentName("com.mediatek.filemanager","com.mediatek.filemanager.FileManagerOperationActivity");
                Intent intent3=new Intent();
                intent3.setComponent(componentName1);
                startActivity(intent3);
                break;

        }
    }


}
