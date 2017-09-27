package com.xuzhipeng.superlib.view;


import com.xuzhipeng.superlib.db.Book;

import java.util.List;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/14
 * Desc:
 */

public interface ICollectView extends ILoadView{
    void setBooks(List<Book> books);
}
