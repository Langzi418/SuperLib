package com.xuzhipeng.superlib.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/7/17
 */

public class PrefUtil {

    private static SharedPreferences preferences;

    private static SharedPreferences getPreferences(Context context) {
        if (preferences == null) {
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
        }

        return preferences;
    }

    //图书馆基址
    private static final String PREF_URL_BASE = "baseUrl";

    //是否登录成功
    private static final String PREF_SUCCESS = "success";

    //用于登录
    private static final String PREF_USER_NO = "userNo";
    private static final String PREF_PASSWORD = "password";
    private static final String PREF_LOGIN_WAY = "select";

    private static final String PREF_USER_NAME = "username";

    //用于查找用户收藏资料
    private static final String PREF_USER_ID = "userId";

    //首次登录
    private static final String PERF_FIRST_START = "isFirstStart";

    public static String getUserNo(Context context) {
        return getPreferences(context)
                .getString(PREF_USER_NO, null);
    }

    public static void setUserNo(Context context, String userNo) {
        getPreferences(context)
                .edit()
                .putString(PREF_USER_NO, userNo)
                .apply();
    }



    public static String getPwd(Context context) {
        return getPreferences(context)
                .getString(PREF_PASSWORD, null);
    }

    public static void setPwd(Context context, String pwd) {
        getPreferences(context)
                .edit()
                .putString(PREF_PASSWORD, pwd)
                .apply();
    }


    public static boolean getSuccess(Context context) {
        return getPreferences(context)
                .getBoolean(PREF_SUCCESS, false);
    }

    public static void setSuccess(Context context, boolean login) {
        getPreferences(context)
                .edit()
                .putBoolean(PREF_SUCCESS, login)
                .apply();
    }

    public static Long getUserId(Context context) {
       return getPreferences(context)
                .getLong(PREF_USER_ID, 0L);
    }

    public static void setUserId(Context context, Long userId) {
        getPreferences(context)
                .edit()
                .putLong(PREF_USER_ID, userId)
                .apply();
    }

    public static String getUserName(Context context) {
        return getPreferences(context)
                .getString(PREF_USER_NAME, null);
    }

    public static void setUserName(Context context, String username) {
        getPreferences(context)
                .edit()
                .putString(PREF_USER_NAME, username)
                .apply();
    }


    public static String getBaseUrl(Context context) {
        return getPreferences(context)
                .getString(PREF_URL_BASE, null);
    }

    public static void setBaseUrl(Context context, String baseUrl) {
        getPreferences(context)
                .edit()
                .putString(PREF_URL_BASE, baseUrl)
                .apply();
    }

    public static Boolean getFirstStart(Context context){
        //默认第一次启动
        return getPreferences(context).getBoolean(PERF_FIRST_START,true);
    }

    public static void setFirstStart(Context context,Boolean isFirst){
        getPreferences(context)
                .edit()
                .putBoolean(PERF_FIRST_START,isFirst).apply();
    }

    public static String getLoginWay(Context context){
        return getPreferences(context).getString(PREF_LOGIN_WAY,null);
    }

    public static void setLoginWay(Context context,String select){
        getPreferences(context)
                .edit()
                .putString(PREF_LOGIN_WAY,select).apply();
    }
}
