package com.example.y.launcher.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.example.y.launcher.R;
import com.example.y.launcher.base.BaseActivity;

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private TextView netSet;
    @Override
    public void initView() {
        setContentView(R.layout.activity_setting);
        netSet=findViewById(R.id.net_set);
    }

    @Override
    public void initListener() {
        netSet.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.net_set:
                jump2Activity(NetSettingActivity.class);
                break;
        }
    }

    public void jump2Activity(Class c){
        Intent intent=new Intent(this,c);
        startActivity(intent);
    }
}
