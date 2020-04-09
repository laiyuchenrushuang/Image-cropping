package com.seatrend.vendor.demo;

import android.app.Application;
import android.content.Context;

/**
 * Created by ly on 2020/3/11 12:08
 * <p>
 * Copyright is owned by chengdu haicheng technology
 * co., LTD. The code is only for learning and sharing.
 * It is forbidden to make profits by spreading the code.
 */
public class MAPP extends Application {
    static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;

    }

    public static Context getContext() {
        return appContext;
    }
}
