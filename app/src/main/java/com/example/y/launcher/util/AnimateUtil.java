package com.example.y.launcher.util;

import android.support.v4.view.ViewCompat;
import android.view.View;

public class AnimateUtil {
    public static void onFocusAnimation(View v,boolean hasFocus){
        if (hasFocus) {
            ViewCompat.animate(v).scaleX(1.1f).scaleY(1.1f).translationZ(1).start();
        }else{
            ViewCompat.animate(v).scaleX(1f).scaleY(1f).translationZ(1).start();
        }
    }
}
