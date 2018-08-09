package com.example.y.launcher.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.example.y.launcher.App;

public class InfoUtil {
    private static PackageInfo packageInfo;
    private static Context mContext = App.getInstance().getContext();

    private static PackageInfo getPackageInfo() {
        if (packageInfo == null) {
            try {
                packageInfo = mContext
                        .getPackageManager()
                        .getPackageInfo(mContext.getPackageName(), 0);

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return packageInfo;

    }

    public static String getSoftVersion() {
        return getPackageInfo().versionName;
    }

    public static String getHardVersion(){
        return Build.HARDWARE;
    }
}
