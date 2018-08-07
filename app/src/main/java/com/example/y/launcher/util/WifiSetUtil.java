package com.example.y.launcher.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import com.example.y.launcher.beans.Wifi;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
 * 对wifi显示进行排序
 * 断开连接
 * 查看wifi信息
 * 保存wifi
 * */
public class WifiSetUtil {
    private static WifiManager manager;
    private static List<WifiConfiguration> existingConfigs;
    public static final int ESS = 0;
    private static final int PSK = 1;
    private static final int WEP = 2;

    @SuppressLint("WifiManagerPotentialLeak")
    public static void init(Context context) {
        manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    public static void setWifiEnabled(boolean b) {
        manager.setWifiEnabled(b);
    }

    public static void startScan() {
        manager.startScan();
    }

    public static List<Wifi> getWifiList() {
        List<Wifi> wifiList = new ArrayList<>();
        Map<String, String> temp = new HashMap<>();
        WifiInfo wifiInfo = WifiSetUtil.getConnectWifiInfo();
        existingConfigs = manager.getConfiguredNetworks();
        String s = "";
        if (wifiInfo != null)
            s = wifiInfo.getSSID();
        for (ScanResult sr : manager.getScanResults()) {
            if (!sr.SSID.equals("") && !temp.containsKey(sr.SSID)) {
                Wifi wifi = new Wifi();
                wifi.setSSID(sr.SSID);
                wifi.setLevel(WifiManager.calculateSignalLevel(sr.level, 4));
                if (s.equals("\"" + sr.SSID + "\""))
                    wifi.setConnect(true);
                else
                    wifi.setConnect(false);
                if (isExist(sr.SSID))
                    wifi.setSave(true);
                else
                    wifi.setSave(false);
                wifi.setPwd("");
                String c = sr.capabilities;
                wifi.setCapabilities(c.substring(0, c.indexOf("[ESS]")));
                if (c.contains("PSK"))
                    wifi.setType(PSK);
                else if (c.contains("WEP"))
                    wifi.setType(WEP);
                else
                    wifi.setType(ESS);
                wifiList.add(wifi);
                temp.put(sr.SSID, sr.SSID);
            }
        }
        return wifiList;
    }

    public static void connectWifi(Wifi wifi) {
        int netId = manager.addNetwork(createWifiConfig(wifi.getSSID(), wifi.getPwd(), wifi.getType()));
        if (!manager.enableNetwork(netId, true))
            manager.reconnect();
        else {
            wifi.setConnect(true);
            wifi.setSave(true);
        }
    }

    public static void cutWifiConnection(Wifi wifi){
        manager.disconnect();
        wifi.setConnect(false);
    }

    public static void connectWifiSaved(Wifi wifi){
        WifiConfiguration config=savedWifi(wifi.getSSID());
        if (config!=null){
            int netId=manager.addNetwork(config);
            manager.enableNetwork(netId,true);
            wifi.setConnect(true);
            wifi.setSave(true);
        }
    }

    private static WifiInfo getConnectWifiInfo() {
        return manager.getConnectionInfo();
    }

    private static WifiConfiguration createWifiConfig(String SSID, String pwd, int type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";

        switch (type) {
            case ESS://无密码
                config.wepKeys[0] = "";
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                config.wepTxKeyIndex = 0;
                break;
            case PSK://PSK加密
                config.preSharedKey = "\"" + pwd + "\"";
                config.hiddenSSID = true;
                config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                //config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                config.status = WifiConfiguration.Status.ENABLED;
                break;
            case WEP://WEP加密
                config.hiddenSSID = true;
                config.wepKeys[0] = "\"" + pwd + "\"";
                config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                config.wepTxKeyIndex = 0;
                break;
            default:
        }
        return config;
    }


    /*存在显示已保存*/
    private static boolean isExist(String SSID) {
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return true;
            }
        }
        return false;
    }


    private static WifiConfiguration savedWifi(String SSID){
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }
        return null;
    }

    public static boolean isWifiEnabled() {
        return manager.isWifiEnabled();
    }

}


