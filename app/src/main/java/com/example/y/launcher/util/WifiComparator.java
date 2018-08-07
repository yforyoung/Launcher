package com.example.y.launcher.util;

import com.example.y.launcher.beans.Wifi;

import java.util.Comparator;

public class WifiComparator implements Comparator{

    @Override
    public int compare(Object o1, Object o2) {
        Wifi w1 = (Wifi) o1;
        Wifi w2 = (Wifi) o2;
        int level=0;
        if (w1.getLevel() > w2.getLevel())
            level=-1;
        if (w1.isConnect()&&!w2.isConnect())
            level=-1;
        if (w1.isSave()&&!w2.isSave())
            level=-1;
        return level;
    }
}
