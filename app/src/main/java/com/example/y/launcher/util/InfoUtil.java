package com.example.y.launcher.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.example.y.launcher.App;

import java.net.NetworkInterface;
import java.net.SocketException;

public class InfoUtil {
    private static PackageInfo packageInfo;
    private static  Context mContext = App.getInstance().getContext();

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

    public static String getHardVersion() {
        return Build.HARDWARE;
    }

    public static String getMacAddress() {
        byte[] add = new byte[0];
        try {
            NetworkInterface nif = NetworkInterface.getByName("wlan0");
            add = nif.getHardwareAddress();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        if (add == null)
            return "";
        else {
            StringBuilder buf = new StringBuilder();
            for (byte b : add) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            return buf.toString();
        }
    }
}
