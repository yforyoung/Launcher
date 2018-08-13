
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
    List<ResolveInfo> infos=new ArrayList<>();

    @Override
    public void initView() {
        setContentView(R.layout.activity_apps);
        recyclerView=findViewById(R.id.apps_list);
    }

    @Override
    public void initData() {
        manager=getPackageManager();
        Intent mainIntent=new Intent(Intent.ACTION_MAIN,null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        infos=manager.queryIntentActivities(mainIntent,0);
        recyclerView.setLayoutManager(new GridLayoutManager(this,7,GridLayoutManager.VERTICAL,false));
        AppsAdapter adapter=new AppsAdapter(infos,manager);
        adapter.setOnItemClickListener(new AppsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                ResolveInfo info=infos.get(position);
                String pName=info.activityInfo.packageName;
                String name=info.activityInfo.name;
                Log.i("info", "onItemClick: "+pName+"   name"+name);
                ComponentName componentName=new ComponentName(pName,name);
                Intent intent=new Intent();
                intent.setComponent(componentName);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
