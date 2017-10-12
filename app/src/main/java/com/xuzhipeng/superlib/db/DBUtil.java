package com.xuzhipeng.superlib.db;

import android.util.Log;

import com.xuzhipeng.superlib.common.util.PrefUtil;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/13
 * Desc: 数据库操作类
 */

public class DBUtil {

    private static final String TAG = "DBUtil";

    /**
     * 关闭数据库
     */
    public static void closeDB() {
        DaoManager.getInstance().closeConnection();
    }

    //用户相关

    /**
     * 用户是否插入数据库
     */
    public static void checkUserInsert() {
        User user = null;
        UserDao userDao = DaoManager.getInstance().getUserDao();
        QueryBuilder<User> builder = userDao.queryBuilder();
        String userNo = PrefUtil.getUserNo();
        try {
            user = builder.where(UserDao.Properties.StuId.eq(userNo)).build().unique();
        } catch (DaoException e) {
            e.printStackTrace();
        }

        if (user == null) {
            //用户不存在
            user = new User();
            user.setStuId(userNo);
            userDao.insert(user);
        }

        //保存用户数据库id
        PrefUtil.setUserId(user.getId());
        closeDB();
    }


    //书相关
    public static Book queryBookByIsbn(String isbn) {
        BookDao bookDao = DaoManager.getInstance().getBookDao();
        QueryBuilder<Book> builder = bookDao.queryBuilder();
        try {
            return builder.where(BookDao.Properties.Isbn.eq(isbn)).build().unique();
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Long insertBook(Book book) {
        BookDao bookDao = DaoManager.getInstance().getBookDao();
        return bookDao.insert(book);
    }

    public static Book queryBookById(Long bookId) {
        BookDao bookDao = DaoManager.getInstance().getBookDao();
        return bookDao.loadByRowId(bookId);
    }


    //收藏相关
    public static Collect queryCollect(Long userId, Long bookId) {
        CollectDao collectDao = DaoManager.getInstance().getCollectDao();
        QueryBuilder<Collect> builder = collectDao.queryBuilder();
        try {
            return builder.where(CollectDao.Properties.UserId.eq(userId)
                    , CollectDao.Properties.BookId.eq(bookId)).build().unique();
        } catch (DaoException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void updateCollect(Collect collect) {
        CollectDao collectDao = DaoManager.getInstance().getCollectDao();
        collectDao.update(collect);
    }

    //取消收藏
    public static void unCollect(Collect collect) {
        collect.setLike(false);
        updateCollect(collect);
    }

    public static void insertCollect(Collect collect) {
        CollectDao collectDao = DaoManager.getInstance().getCollectDao();
        collectDao.insert(collect);
    }

    //用户id查询用户收藏
    public static List<Collect> queryUserCollect(Long userId) {
        CollectDao collectDao = DaoManager.getInstance().getCollectDao();
        QueryBuilder<Collect> builder = collectDao.queryBuilder();
        return builder.where(CollectDao.Properties.UserId.eq(userId)
                , CollectDao.Properties.Like.eq(1)).build().list();
    }

    //根据书id取消收藏
    public static void cancelCollect(Long userId, Long bookId) {
        CollectDao collectDao = DaoManager.getInstance().getCollectDao();
        QueryBuilder<Collect> builder = collectDao.queryBuilder();
        try {
            Collect collect = builder.where(CollectDao.Properties.UserId.eq(userId)
                    , CollectDao.Properties.BookId.eq(bookId)).build().unique();
            if (collect != null) {
                collect.setLike(false);
                updateCollect(collect);
            }
        } catch (DaoException e) {
            e.printStackTrace();
        } finally {
            closeDB();
        }
    }


    //建议相关
    public static void insertSuggest(String name) {
        SuggestDao suggestDao = DaoManager.getInstance().getSuggestDao();
        Suggest oldSuggest = null;
        try {
            QueryBuilder<Suggest> builder = suggestDao.queryBuilder();
            oldSuggest = builder.where(SuggestDao.Properties.Name.eq(name)).unique();
        } catch (DaoException e) {
            e.printStackTrace();
        }

        if (oldSuggest == null) {
            Suggest newSuggest = new Suggest();
            newSuggest.setName(name);
            newSuggest.setTimes(1L);
            suggestDao.insert(newSuggest);
        } else {
            //加一
            oldSuggest.setTimes(oldSuggest.getTimes() + 1L);
            suggestDao.update(oldSuggest);
        }

        closeDB();
    }


    public static String[] querySuggestLike(String str) {
        SuggestDao suggestDao = DaoManager.getInstance().getSuggestDao();

        QueryBuilder<Suggest> builder = suggestDao.queryBuilder();
        List<Suggest> suggests =
                builder.where(SuggestDao.Properties.Name.like("%" + str + "%"))
                        .orderDesc(SuggestDao.Properties.Times)
                        .build().list();
        Log.d(TAG, "querySuggestLike: " + suggests.size());
        if (suggests.size() != 0) {
            String[] names = new String[suggests.size()];
            for (int i = 0; i < suggests.size(); i++) {
                names[i] = suggests.get(i).getName();
            }
            closeDB();
            return names;
        }
        closeDB();
        return null;
    }
}
