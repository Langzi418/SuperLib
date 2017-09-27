package com.xuzhipeng.superlib.common.app;

import android.app.Application;
import android.content.Context;

import com.xuzhipeng.superlib.common.NetWorkReceiver;
import com.xuzhipeng.superlib.common.util.PrefUtil;


/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/7/29
 */

public class App extends Application {


    //全局上下文
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();
        PrefUtil.setBaseUrl(this,"http://opac.lib.wust.edu.cn:8080/");
        NetWorkReceiver.registerNet(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        NetWorkReceiver.unregisterNet(this);
    }

    public static Context getContext(){
        return sContext;
    }

    public static String getBaseUrl(){
        return PrefUtil.getBaseUrl(getContext());
    }
}
