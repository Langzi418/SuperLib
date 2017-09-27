package com.xuzhipeng.superlib.module.mylib;

import android.content.Context;
import android.net.Uri;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.xuzhipeng.superlib.address.Address;
import com.xuzhipeng.superlib.common.util.PrefUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/7/17
 */

public class MyLibHttp {

    private static final String TAG = "MyLibHttp";


    private static MyLibHttp sLib;
    private OkHttpClient mClient;
    private static Context mContext;

    private MyLibHttp(Context context) {
        ClearableCookieJar cookieJar =
                new PersistentCookieJar(
                        new SetCookieCache(), new SharedPrefsCookiePersistor(context));

        mClient = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();

    }

    public static MyLibHttp getInstance(Context context) {
        if (sLib == null) {
            synchronized (MyLibHttp.class) {
                if (sLib == null) {
                    sLib = new MyLibHttp(context);
                }
            }
        }
        mContext = context;
        return sLib;
    }


    /**
     * 自定义url,主要用于get
     */
    public String sendOkUrl(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        return sendOkRequest(request);
    }

    /**
     * 自定义请求，主要用于post
     */
    private String sendOkRequest(Request request) throws IOException {
        Response response = mClient.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        }
        return null;
    }


    /**
     * 验证登录
     */
    public String checkLogin(String username, String password, String select) {
        //用于以后登录
        PrefUtil.setLoginWay(mContext, select);

        RequestBody requestBody = new FormBody.Builder()
                .add("number", username)
                .add("passwd", password)
                .add("select", select)
                .build();

        Request request = new Request.Builder()
                .url(Address.getReaderVerify())
                .post(requestBody)
                .build();

        try {
            return sendOkRequest(request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }


    /**
     * 首次登录认证姓名
     *
     * @param name 名字
     * @return boolean 值
     */
    public boolean getConRes(String name) {
        RequestBody requestBody = new FormBody.Builder()
                .add("name", name)
                .build();

        Request request = new Request.Builder()
                .url(Address.getReaderConRes())
                .post(requestBody)
                .build();
        try {
            String html = sendOkRequest(request);

            if (html != null) {
                Document doc = Jsoup.parse(html);
                Elements ele = doc.select("div#container div#navsidebar");

                //只能根据 HTML 判断登录情况
                return ele.size() != 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String loadZJXX() {

        //再次加载，证件信息，重新登录
        String username = PrefUtil.getUserNo(mContext);
        String pwd = PrefUtil.getPwd(mContext);

        if (username != null && pwd != null) {
            String select = PrefUtil.getLoginWay(mContext);
            return checkLogin(username, pwd, select);
        }

        return null;
    }


    public String loadDQJY() {
        try {
            return sendOkUrl(Address.getDQJY());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String loadJYLS(int page) {

        String url = Uri.parse(Address.getJYLS())
                .buildUpon()
                .appendQueryParameter("page", String.valueOf(page))
                .build().toString();
        try {
            return sendOkUrl(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String loadRenew(String barNo, String checkNo) {
        String url = Uri.parse(Address.getRenew()).buildUpon()
                .appendQueryParameter("bar_code", barNo)
                .appendQueryParameter("check", checkNo)
                .appendQueryParameter("time", String.valueOf(System.currentTimeMillis()))
                .build().toString();
        try {
            String html = sendOkUrl(url);
            if (html != null) {
                Document doc = Jsoup.parse(html);
                return doc.getElementsByTag("font").text();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
