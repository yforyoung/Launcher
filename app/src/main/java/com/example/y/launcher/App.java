package com.example.y.launcher;


import android.annotation.SuppressLint;
import android.content.Context;

public class App {
    @SuppressLint("StaticFieldLeak")
    private static final App app = new App();
    private Context context;

    public void init(Context context){
        this.context=context;
    }

    public static App getInstance() {
        return app;
    }

    public Context getContext() {
        return context;
    }

    private App() {
    }
}
