package com.xuzhipeng.superlib.common.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.xuzhipeng.superlib.common.NetWorkReceiver;


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

        listenForeground();

    }

    /**
     * 应用处于前台
     */
    private void listenForeground() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
                NetWorkReceiver.registerNet(sContext);
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }


    /**
     * 应用处于后台
     */
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            NetWorkReceiver.unregisterNet(this);
        }
    }

    public static Context getContext() {
        return sContext;
    }

}
