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


public class WifiUtil {
    private static WifiManager manager;
    private static WifiInfo info;
    public static final int ESS = 0;
    private static final int PSK = 1;
    private static final int WEP = 2;

    @SuppressLint("WifiManagerPotentialLeak")
    public static void init(Context context) {
        manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    private static WifiInfo getConnectWifiInfo() {
        return manager.getConnectionInfo();
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
        for (ScanResult sr : manager.getScanResults()) {
            if (!sr.SSID.equals("") && !temp.containsKey(sr.SSID)) {
                wifiList.add(initWifi(sr));
                temp.put(sr.SSID, sr.SSID);
            }
        }
        return wifiList;
    }

    private static Wifi initWifi(ScanResult sr) {
        Wifi wifi = new Wifi();
        wifi.setSSID(sr.SSID);
        wifi.setLevel(WifiManager.calculateSignalLevel(sr.level, 4));
        if (SpfUtil.getString("connect_wifi", "").equals(sr.SSID))
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
        return wifi;
    }

    public static void connectWifi(Wifi wifi) {
        int netId = manager.addNetwork(createWifiConfig(wifi.getSSID(), wifi.getPwd(), wifi.getType()));
        if (!manager.enableNetwork(netId, true))
            manager.reconnect();
        else {
            SpfUtil.putString("connect_wifi", wifi.getSSID());
            wifi.setConnect(true);
            wifi.setSave(true);
        }
    }

    public static void cutWifiConnection(Wifi wifi) {
        manager.disconnect();
        wifi.setConnect(false);
        SpfUtil.putString("connect_wifi", "");
    }

    public static void connectWifiSaved(Wifi wifi) {
        wifi.setPwd(SpfUtil.getString(wifi.getSSID(), ""));
        connectWifi(wifi);
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
        List<WifiConfiguration> existingConfigs = manager.getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return true;
            }
        }
        return false;
    }


    private static WifiConfiguration getSavedWifi(String SSID) {
        List<WifiConfiguration> existingConfigs = manager.getConfiguredNetworks();
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

    public static String getWlanMacAddress() {
        return getConnectWifiInfo().getBSSID();
    }

    public static String getGateWay() {
        return intToIp(manager.getDhcpInfo().gateway);
    }

    public static String getNetMask(){
        return intToIp(manager.getDhcpInfo().netmask);
    }

    public static String getIP() {
        WifiInfo info = getConnectWifiInfo();
        if (info == null)
            return "";
        return intToIp(info.getIpAddress());
    }

    private static String intToIp(int ipInt) {
        return String.valueOf(ipInt & 0xFF) + "." +
                ((ipInt >> 8) & 0xFF) + "." +
                ((ipInt >> 16) & 0xFF) + "." +
                ((ipInt >> 24) & 0xFF);
    }
}


