package com.example.y.launcher.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.y.launcher.adapter.AppsAdapter;
import com.example.y.launcher.R;
import com.example.y.launcher.base.BaseFragment;

import java.util.List;

public class SecondFragment extends BaseFragment {
    RecyclerView recyclerView;
    PackageManager manager;
    List<ResolveInfo> infos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_second,container,false);
    }

    @Override
    public void initView() {
        recyclerView=getActivity().findViewById(R.id.all_application);
    }

    @Override
    public void initData() {
        manager=getActivity().getPackageManager();
        Intent mainIntent=new Intent(Intent.ACTION_MAIN,null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        infos=manager.queryIntentActivities(mainIntent,0);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),5,GridLayoutManager.HORIZONTAL,false));
        AppsAdapter adapter=new AppsAdapter(infos,manager);
        adapter.setOnItemClickListener(new AppsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                ResolveInfo info=infos.get(position);
                String pName=info.activityInfo.packageName;
                String name=info.activityInfo.name;
                ComponentName componentName=new ComponentName(pName,name);
                Intent intent=new Intent();
                intent.setComponent(componentName);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
