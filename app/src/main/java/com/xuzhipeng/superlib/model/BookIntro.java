package com.xuzhipeng.superlib.model;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/12
 * Desc:
 */

public class BookIntro {

    private String nameIndex;
    private String author;
    private String store;
    private String pressYear;
    private String loanable;

    private String infoUrl;

    public String getNameIndex() {
        return nameIndex;
    }

    public void setNameIndex(String nameIndex) {
        this.nameIndex = nameIndex;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getPressYear() {
        return pressYear;
    }

    public void setPressYear(String pressYear) {
        this.pressYear = pressYear;
    }

    public String getLoanable() {
        return loanable;
    }

    public void setLoanable(String loanable) {
        this.loanable = loanable;
    }


    public String getInfoUrl() {
        return infoUrl;
    }

    public void setInfoUrl(String infoUrl) {
        this.infoUrl = infoUrl;
    }
}
