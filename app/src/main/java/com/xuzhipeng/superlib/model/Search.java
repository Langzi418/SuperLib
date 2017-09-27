package com.xuzhipeng.superlib.model;


import android.net.Uri;

import com.xuzhipeng.superlib.address.Address;

/**
 *
 */
public class Search {



    private String strSearchType; //检索类型,题名(title),作者(author),主题词(keyword)，ISBN(isbn)
    private String strText;  //检索输入的文本
    private String doctype;  //图书类型，所有(ALL),中文图书(01),西文(02),中文期刊(11),西文(12)
    private String displaypg;  //显示条数，20,30,50,100
    private String sort;   //排序方式,入藏日期(CATA_DATE),题名(M_TITLE)，作者(M_AUTHOR)，
                           // 出版社(M_PUBLISHER),出版日期(M_PUB_YEAR)
    private String orderby;  //升(asc) 降(desc)

//    public void setDefault() {
//        strSearchType = "title";
//        doctype = "ALL";
//        displaypg = "20";
//        sort = "CATA_DATE";
//        orderby = "desc";
//    }

    public int getDisplaypg() {
        return Integer.parseInt(displaypg);
    }

    public void setStrSearchType(long selNo) {
        if(selNo == 0L){
            strSearchType = "title";
        }else if (selNo == 1L){
            strSearchType = "author";
        }else if (selNo == 2L){
            strSearchType = "keyword";
        }else {
            strSearchType = "isbn";
        }
    }

    public void setStrText(String strText) {
        this.strText = strText;
    }

    public void setDoctype(long selNo) {
        if(selNo == 0L){
            doctype = "ALL";
        }else if (selNo == 1L){
            doctype = "01";
        }else if (selNo == 2L){
            doctype = "02";
        }else if (selNo == 3L){
            doctype = "11";
        }else {
            doctype = "12";
        }
    }

    public void setDisplaypg(long selNo) {
        if(selNo == 0L){
            displaypg = "20";
        }else if (selNo == 1L){
            displaypg = "30";
        }else if (selNo == 2L){
            displaypg = "50";
        }else {
            displaypg = "100";
        }
    }

    public void setSort(long selNo) {
        if(selNo == 0L){
            sort = "CATA_DATE";
        }else if (selNo == 1L){
            sort = "M_TITLE";
        }else if (selNo == 2L){
            sort = "M_AUTHOR";
        }else if (selNo == 3L){
            sort = "M_PUBLISHER";
        }else {
            sort = "M_PUB_YEAR";
        }
    }

    public void setOrderby(long selNo) {
        if(selNo == 0L ){
            orderby = "desc";
        }else if(selNo == 1L){
            orderby = "asc";
        }
    }

    @Override
    public String toString() {
        return Uri.parse(Address.getSearch())
                .buildUpon()
                .appendQueryParameter("strSearchType", strSearchType)
                .appendQueryParameter("strText", strText)
                .appendQueryParameter("doctype", doctype)
                .appendQueryParameter("displaypg", displaypg)
                .appendQueryParameter("sort", sort)
                .appendQueryParameter("orderby", orderby)
                .build().toString();
    }
}
