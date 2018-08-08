package com.example.y.launcher.activity;

import android.annotation.SuppressLint;
import android.net.wifi.WifiInfo;
import android.widget.TextView;

import com.example.y.launcher.R;
import com.example.y.launcher.base.BaseActivity;
import com.example.y.launcher.util.WifiSetUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;

public class AboutActivity extends BaseActivity {
    private TextView ip,mac;
    @Override
    public void initView() {
        setContentView(R.layout.activity_about);
        ip=findViewById(R.id.ip);
        mac=findViewById(R.id.mac);

    }

    @SuppressLint({"SetTextI18n", "HardwareIds"})
    @Override
    public void initData() {
        WifiInfo info= WifiSetUtil.getConnectWifiInfo();
        if (info!=null){
            ip.setText("IP地址:     "+intToIp(info.getIpAddress()));
            mac.setText("WLANMAC地址:       "+info.getBSSID());
        }
    }

    public String intToIp(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }

    // Android 6.0以上获取WiFi的Mac地址
    //由于android6.0对wifi mac地址获取进行了限制，用原来的方法获取会获取到02:00:00:00:00:00这个固定地址。
    //但是可以通过读取节点进行获取"/sys/class/net/wlan0/address"
    public String getMacAddr() {
        try {
            return loadFileAsString("/sys/class/net/wlan0/address")
                    .toUpperCase().substring(0, 17);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    private String loadFileAsString(String filePath)
            throws java.io.IOException {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }


}
