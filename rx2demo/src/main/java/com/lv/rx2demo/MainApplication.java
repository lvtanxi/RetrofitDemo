package com.lv.rx2demo;

import android.app.Application;

import com.orhanobut.logger.Logger;


/**
 * User: 吕勇
 * Date: 2016-02-24
 * Time: 11:57
 * Description:
 */
public class MainApplication extends Application {
    private static MainApplication sInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init("lvtanxi");
        sInstance = this;
    }

    public static MainApplication getInstance() {
        return sInstance;
    }
}
