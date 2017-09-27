package com.xuzhipeng.superlib.module.update;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.allenliu.versionchecklib.core.AVersionService;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.xuzhipeng.superlib.R;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/22
 * Desc:
 */

public class UpdateService extends AVersionService {

    private static final String TAG = "UpdateService";

    @Override
    public IBinder onBind(Intent intent) {

        return super.onBind(intent);
    }

    @Override
    public void onResponses(AVersionService service, String response) {
        if (response == null) {
            return;
        }

        Log.d(TAG, "onResponses: " + response);
        int clientVersion = UpdateUtil.getVersionCode(this);
        try {
            Update update = new Gson().fromJson(response, Update.class);
            int severVersion = update.getVersionCode();
            if (severVersion > clientVersion && clientVersion != 0) {
                String apkUrl = update.getApkUrl();
                String title = getString(R.string.version_name, update.getVersionName());
                String size = getString(R.string.version_size, update.getSize());
                String msg = update.getUpdateMsg();
                service.showVersionDialog(apkUrl, title, size + "\n" + msg);
            } else {
                stopSelf();
            }
        }catch (JsonSyntaxException e){
            e.printStackTrace();
        }
    }



}
