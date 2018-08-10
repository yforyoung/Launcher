package com.example.y.launcher.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.y.launcher.R;
import com.example.y.launcher.activity.AppsActivity;
import com.example.y.launcher.activity.SettingActivity;
import com.example.y.launcher.base.BaseFragment;
import com.example.y.launcher.util.AnimateUtil;


public class FirstFragment extends BaseFragment implements View.OnFocusChangeListener, View.OnClickListener {
    private CardView setting,apps;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first,container,false);
    }

    @Override
    public void initView() {
        setting = getActivity().findViewById(R.id.setting);
        apps = getActivity().findViewById(R.id.apps);
    }

    @Override
    public void initListener() {
        setting.setOnClickListener(this);
        apps.setOnClickListener(this);

        setting.setOnFocusChangeListener(this);
        apps.setOnFocusChangeListener(this);

        setting.setFocusableInTouchMode(true);
        apps.setFocusableInTouchMode(true);
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        AnimateUtil.onFocusAnimation(v,hasFocus);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting:
                Intent intent=new Intent(getActivity(),SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.apps:
                Intent intent1=new Intent(getActivity(),AppsActivity.class);
                startActivity(intent1);
                break;
        }
    }


}
