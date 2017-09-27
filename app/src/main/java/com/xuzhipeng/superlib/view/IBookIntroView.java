package com.xuzhipeng.superlib.view;


import com.xuzhipeng.superlib.model.BookIntro;

import java.util.List;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/12
 * Desc:
 */

public interface IBookIntroView extends ILoadView{
    void setResult(String result);
    void setIntros(List<BookIntro> intros);
    void setPageNum(int pageNum);
}
