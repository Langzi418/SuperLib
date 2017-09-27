package com.xuzhipeng.superlib.module.update;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/22
 * Desc:
 */

public class Update {


    /**
     * versionCode : 1
     * versionName : 1.0.0
     * apkUrl : http://openbox.mobilem.360.cn/index/d/sid/3282847
     * updateMsg : 修复已知bug
     * size : 5M
     */

    private int versionCode;
    private String versionName;
    private String apkUrl;
    private String updateMsg;
    private String size;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public String getUpdateMsg() {
        return updateMsg;
    }

    public void setUpdateMsg(String updateMsg) {
        this.updateMsg = updateMsg;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
