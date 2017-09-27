package com.xuzhipeng.superlib.model;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/7/18
 */

public class DQJY {
    private String bookName;
    private String bookJHX;
    private String bookGCD;

    private String barNo;  //续借
    private String checkNo;

    public String getBarNo() {
        return barNo;
    }

    public void setBarNo(String barNo) {
        this.barNo = barNo;
    }

    public String getCheckNo() {
        return checkNo;
    }

    public void setCheckNo(String checkNo) {
        this.checkNo = checkNo;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookJHX() {
        return bookJHX;
    }

    public void setBookJHX(String bookJHX) {
        this.bookJHX = bookJHX;
    }

    public String getBookGCD() {
        return bookGCD;
    }

    public void setBookGCD(String bookGCD) {
        this.bookGCD = bookGCD;
    }
}
