package com.example.y.launcher.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;


import com.example.y.launcher.beans.Wifi;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WifiSetUtil {
    private static WifiManager manager;
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
        List<Wifi> wifis = new ArrayList<>();
        Map<String, String> temp = new HashMap<>();
        for (ScanResult sr : manager.getScanResults()) {
            if (!temp.containsKey(sr.SSID)) {
                Wifi wifi = new Wifi();
                wifi.setSSID(sr.SSID);
                wifi.setLevel(WifiManager.calculateSignalLevel(sr.level, 4));
                wifi.setConnect(false);
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

                wifis.add(wifi);
                temp.put(sr.SSID, sr.SSID);
            }
        }
        return wifis;
    }

    public static void connectWifi(Wifi wifi) {
        int netId = manager.addNetwork(createWifiConfig(wifi.getSSID(), wifi.getPwd(), wifi.getType()));
        if (!manager.enableNetwork(netId, true))
            manager.reconnect();
    }

    private static WifiConfiguration createWifiConfig(String SSID, String pwd, int type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";

        WifiConfiguration tempConfig = isExist(SSID);
        if (tempConfig != null) {
            manager.removeNetwork(tempConfig.networkId);
        }

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


    private static WifiConfiguration isExist(String SSID) {
        List<WifiConfiguration> existingConfigs = manager.getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }
        return null;
    }


}
