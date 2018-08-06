package com.example.y.launcher.util;

import android.annotation.SuppressLint;
import android.widget.Toast;

import com.example.y.launcher.App;

public class ToastUtil {
    private static Toast toast;

    @SuppressLint("ShowToast")
    public static void showToast(String text){
        if (toast==null){
            toast=Toast.makeText(App.getInstance().getContext(), text, Toast.LENGTH_SHORT);
        }else{
            toast.setText(text);
        }
        toast.show();
    }
}
