package com.xuzhipeng.superlib.view;


import com.xuzhipeng.superlib.db.Book;
import com.xuzhipeng.superlib.db.Collect;
import com.xuzhipeng.superlib.model.DouBanInfo;
import com.xuzhipeng.superlib.model.DouComment;
import com.xuzhipeng.superlib.model.LibInfo;
import java.util.List;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/12
 * Desc:
 */

public interface IBookInfoView extends ILoadView {
    void setLibInfo(LibInfo libInfo);
    void setDouBanInfo(DouBanInfo douBanInfo);
    void setDouBanCmt(List<DouComment> comments);
    void setBook(Book book);
    void setDouBanCmtDetail(String s);
    void setCollect(Collect collect);
}
