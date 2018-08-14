
package com.example.y.launcher.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.y.launcher.adapter.AppsAdapter;
import com.example.y.launcher.R;
import com.example.y.launcher.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class AppsActivity extends BaseActivity {
    RecyclerView recyclerView;
    PackageManager manager;
    List<ResolveInfo> infos = new ArrayList<>();

    @Override
    public void initView() {
        setContentView(R.layout.activity_apps);
        recyclerView = findViewById(R.id.apps_list);
    }

    @Override
    public void initData() {
        manager = getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        infos = manager.queryIntentActivities(mainIntent, 0);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 7, GridLayoutManager.VERTICAL, false));
        initInfos();
        AppsAdapter adapter = new AppsAdapter(infos, manager);
        adapter.setOnItemClickListener(new AppsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                ResolveInfo info = infos.get(position);
                String pName = info.activityInfo.packageName;
                String name = info.activityInfo.name;
                Log.i("info", "onItemClick: " + pName + "   name" + name);
                ComponentName componentName = new ComponentName(pName, name);
                Intent intent = new Intent();
                intent.setComponent(componentName);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void initInfos() {
        if (infos != null) {
            for (int i=0;i<infos.size();i++) {
                String name = infos.get(i).activityInfo.packageName;
                if (name.equals("com.android.con tacts")//通讯录
                        || name.equals("com.android.dialer")//电话
                        || name.equals(" com.android.mms")//信息
                        || name.equals("com.android.email")//email
                        || name.equals("com.android.soundrecorder")//录音机
                        || name.equals("com.example.y.launcher")//启动
                        || name.equals("com.android.stk")//stk
                        || name.equals("stericson.busybox")//busybox
                        || name.equals("com.mediatek.datatransfer")//备份与恢复
                        || name.equals("com.android.providers.downloads.ui")//下载
                        || name.equals("com.android.quicksearchbox")//搜索
                        || name.equals("com.mediatek.mco")//MediaTek™ 多核观测器
                        || name.equals("com.cootek.smartinputv5")//触宝输入法
                        || name.equals("com.qttaudio.android.demo")//QttAudio体验版
                        ) {
                    infos.remove(i);
                }
            }
        }

    }
}
