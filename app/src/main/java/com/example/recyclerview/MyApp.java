package com.example.recyclerview;

import android.app.Application;

import com.didichuxing.doraemonkit.DoraemonKit;

/**
 * @author Administrator
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DoraemonKit.install(this);
    }
}
