package com.xuzhipeng.superlib.address;

import com.xuzhipeng.superlib.common.app.App;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/26
 * Desc:
 */

public class Address {

//    public static String base;

    public static String getBase() {
        return App.getBaseUrl();
    }

    public static String getOpac() {
        return getBase() + "opac/";
    }

    public static String getSearch() {
        return getBase() + "opac/openlink.php";
    }

    public static String getHotSearch() {
        return getBase() + "opac/top100.php";
    }

    public static String getHotBook() {
        return getBase() + "top/top_book.php";
    }

    public static String getLogin() {
        return getBase() + "reader/login.php";
    }

    public static String getReaderVerify() {
        return getBase() + "reader/redr_verify.php";
    }

    public static String getReaderConRes() {
        return getBase() + "reader/redr_con_result.php";
    }

    public static String getDQJY() {
        return getBase() + "reader/book_lst.php";
    }

    public static String getJYLS() {
        return getBase() + "reader/book_hist.php";
    }

    public static String getRenew() {
        return getBase() + "reader/ajax_renew.php";
    }
}
