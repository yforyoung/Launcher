package com.example.y.launcher.util;

import com.example.y.launcher.beans.Wifi;

import java.util.Comparator;

public class WifiComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        Wifi w1 = (Wifi) o1;
        Wifi w2 = (Wifi) o2;
        int cr;
        boolean c = w1.isConnect() == w2.isConnect();
        if (!c) {
            cr = w1.isConnect() ? -1 : 1;
        } else {
            boolean s = w1.isSave() == w2.isSave();
            if (!s) {
                cr = w1.isSave() ? -2 : 2;
            } else {
                cr = w1.getLevel() - w2.getLevel() > 0 ? -3 : 3;
            }
        }
        return cr;
    }
}
