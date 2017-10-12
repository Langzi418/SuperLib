package com.xuzhipeng.superlib.common.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.xuzhipeng.superlib.R;
import com.xuzhipeng.superlib.common.app.App;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/7/17
 */

public class PrefUtil {

    private static SharedPreferences preferences;

    private static SharedPreferences getPreferences() {
        if (preferences == null) {
            synchronized (PrefUtil.class) {
                if (preferences == null) {
                    preferences = PreferenceManager
                            .getDefaultSharedPreferences(App.getContext());
                }
            }
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


    //用于查找用户收藏资料
    private static final String PREF_USER_ID = "userId";

    //首次登录
    private static final String PREF_FIRST_START = "isFirstStart";

    //主题模式
    private static final String PREF_THEME_ID = "themeId";

    public static String getUserNo() {
        return getPreferences()
                .getString(PREF_USER_NO, null);
    }

    public static void setUserNo(String userNo) {
        getPreferences()
                .edit()
                .putString(PREF_USER_NO, userNo)
                .apply();
    }


    public static String getPwd() {
        return getPreferences()
                .getString(PREF_PASSWORD, null);
    }

    public static void setPwd(String pwd) {
        getPreferences()
                .edit()
                .putString(PREF_PASSWORD, pwd)
                .apply();
    }


    public static boolean getSuccess() {
        return getPreferences()
                .getBoolean(PREF_SUCCESS, false);
    }

    public static void setSuccess(boolean login) {
        getPreferences()
                .edit()
                .putBoolean(PREF_SUCCESS, login)
                .apply();
    }

    public static Long getUserId() {
        return getPreferences()
                .getLong(PREF_USER_ID, 0L);
    }

    public static void setUserId(Long userId) {
        getPreferences()
                .edit()
                .putLong(PREF_USER_ID, userId)
                .apply();
    }


    public static String getBaseUrl() {
        return getPreferences()
                .getString(PREF_URL_BASE, null);
    }

    public static void setBaseUrl(String baseUrl) {
        getPreferences()
                .edit()
                .putString(PREF_URL_BASE, baseUrl)
                .apply();
    }

    public static Boolean getFirstStart() {
        //默认第一次启动
        return getPreferences().getBoolean(PREF_FIRST_START, true);
    }

    public static void setFirstStart(Boolean isFirst) {
        getPreferences()
                .edit()
                .putBoolean(PREF_FIRST_START, isFirst).apply();
    }


    public static int getThemeId() {
        return getPreferences().getInt(PREF_THEME_ID, R.style.AppTheme);
    }

    public static void setThemeId(int themeId) {
        getPreferences()
                .edit()
                .putInt(PREF_THEME_ID, themeId)
                .apply();
    }
}
