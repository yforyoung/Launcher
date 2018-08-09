package com.example.y.launcher;

import android.app.Application;
import com.example.y.launcher.util.WifiUtil;

public class LauncherApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        App.getInstance().init(getApplicationContext());
        WifiUtil.init(getApplicationContext());
    }
}
