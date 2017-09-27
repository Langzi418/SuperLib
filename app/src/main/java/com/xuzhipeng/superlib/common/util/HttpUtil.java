package com.xuzhipeng.superlib.common.util;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/13
 * Desc:
 */

public class HttpUtil {
    private static OkHttpClient client;

    private static OkHttpClient getClient(){
        if(client == null){
            synchronized (OkHttpClient.class){
                if(client == null){
                    client = new OkHttpClient
                            .Builder()
                            .connectTimeout(10000, TimeUnit.MILLISECONDS)
                            .build();
                }
            }
        }

        return client;
    }

    /**
     * 发送同步请求，返回 String
     * @throws IOException 异常
     */
    public static String sendOkHttp(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = getClient().newCall(request).execute();
        if(response.isSuccessful() && response.body()!=null){
            return response.body().string();
        }

        return null;
    }
}
