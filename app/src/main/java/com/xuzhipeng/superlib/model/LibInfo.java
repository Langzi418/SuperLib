package com.xuzhipeng.superlib.model;

import java.util.List;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/12
 * Desc:
 */

public class LibInfo {
    private String isbn;
    private String category;
    private String name;

    private List<BookStatus> statusList;

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BookStatus> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<BookStatus> statusList) {
        this.statusList = statusList;
    }
}
