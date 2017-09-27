package com.xuzhipeng.superlib.module.update;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.allenliu.versionchecklib.core.AllenChecker;
import com.allenliu.versionchecklib.core.VersionParams;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/22
 * Desc:
 */

public class UpdateUtil {

    public static final String URL_UPDATE
            = "http://101.132.121.229/update.txt";

    private static PackageManager pm;

    private static PackageManager getPm(ContextWrapper wrapper){
        if(pm == null){
            pm = wrapper.getPackageManager();
        }

        return pm;
    }

    /**
     *  检测更新
     */
    public static void update(Context context){
        VersionParams.Builder builder = new VersionParams.Builder()
                .setRequestUrl(URL_UPDATE)
                .setDownloadAPKPath(Environment.getExternalStoragePublicDirectory
                        (Environment.DIRECTORY_DOWNLOADS).getPath())
                .setService(UpdateService.class);
        AllenChecker.startVersionCheck(context, builder.build());
    }


    /**
     *  返回版本名
     */
    public static String getVersionName(ContextWrapper wrapper){
        PackageManager pm = getPm(wrapper);
        try {
            PackageInfo info = pm.getPackageInfo(wrapper.getPackageName(),0);
            return  info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     *  返回版本号
     */

    public static int getVersionCode(ContextWrapper wrapper){
        PackageManager pm = getPm(wrapper);
        try {
            PackageInfo info = pm.getPackageInfo(wrapper.getPackageName(),0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return 0;
    }




}
