package com.xuzhipeng.superlib.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.xuzhipeng.superlib.R;
import com.xuzhipeng.superlib.common.util.NetWorkUtil;

public class NetWorkReceiver extends BroadcastReceiver {

    protected static class InstanceHolder {
        public static final NetWorkReceiver INSTANCE = new NetWorkReceiver();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!NetWorkUtil.isNetworkConnected(context)) {
            Toast.makeText(context, R.string.no_network, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 注册网络广播
     */
    public static void registerNet(Context context) {
        IntentFilter filter =
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(InstanceHolder.INSTANCE, filter);

    }

    /**
     * 取消注册
     */
    public static void unregisterNet(Context context) {
        context.unregisterReceiver(InstanceHolder.INSTANCE);
    }


}
