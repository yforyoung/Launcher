package com.example.y.launcher;

import android.app.Application;

public class LauncherApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        App.getInstance().init(getApplicationContext());
    }
}