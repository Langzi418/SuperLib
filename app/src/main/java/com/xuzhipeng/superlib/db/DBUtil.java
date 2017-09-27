package com.xuzhipeng.superlib.db;

import android.util.Log;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/13
 * Desc: 数据库操作类
 */

public class DBUtil {
    private static BookDao bookDao;
    private static UserDao userDao;
    private static SuggestDao suggestDao;
    private static CollectDao collectDao;

    private static final String TAG = "MainActivity";

    private static BookDao getBookDao() {
        if (bookDao == null) {
            bookDao = DaoManager.getInstance().getBookDao();
        }

        return bookDao;
    }

    private static UserDao getUserDao() {
        if (userDao == null) {
            userDao = DaoManager.getInstance().getUserDao();
        }

        return userDao;
    }


    private static SuggestDao getSuggestDao() {
        if (suggestDao == null) {
            suggestDao = DaoManager.getInstance().getSuggestDao();
        }

        return suggestDao;
    }

    private static CollectDao getCollectDao() {
        if (collectDao == null) {
            collectDao = DaoManager.getInstance().getCollectDao();
        }

        return collectDao;
    }

    /**
     * 关闭数据库
     */
    public static void closeDB() {
        DaoManager.getInstance().closeConnection();
    }


    //用户相关
    public static long insertUser(User user) {
        return getUserDao().insert(user);
    }

    public static User queryUserById(Long userId) {
        return getUserDao().loadByRowId(userId);
    }

    public static User queryUserByStuNo(String userNo) {
        QueryBuilder<User> builder = getUserDao().queryBuilder();
        return builder.where(UserDao.Properties.StuId.eq(userNo)).build().unique();
    }


    //书相关
    public static Book queryBookByIsbn(String isbn) {
        QueryBuilder<Book> builder = getBookDao().queryBuilder();
        return builder.where(BookDao.Properties.Isbn.eq(isbn)).build().unique();
    }

    public static Long insertBook(Book book) {
        return getBookDao().insert(book);
    }

    public static Book queryBookById(long bookId) {
        return getBookDao().loadByRowId(bookId);
    }




    //收藏相关
    public static Collect queryCollect(Long userId, Long bookId) {
        QueryBuilder<Collect> builder = getCollectDao().queryBuilder();
        return builder.where(CollectDao.Properties.UserId.eq(userId)
                , CollectDao.Properties.BookId.eq(bookId)).build().unique();
    }

    public static void updateCollect(Collect collect) {
        getCollectDao().update(collect);
    }

    //取消收藏
    public static void unCollect(Collect collect) {
        collect.setLike(false);
        updateCollect(collect);
    }

    //通过用户id和bookID 取消收藏
    public static void unCollect(Long userId,Long bookId) {
        Collect collect = queryCollect(userId,bookId);
        unCollect(collect);
    }

    public static void insertCollect(Collect collect) {
        getCollectDao().insert(collect);
    }

    //用户id查询用户收藏
    public static List<Collect> queryUserCollect(Long userId) {
        QueryBuilder<Collect> builder = getCollectDao().queryBuilder();
        return builder.where(CollectDao.Properties.UserId.eq(userId)
        ,CollectDao.Properties.Like.eq(1)).build().list();
    }


    //建议相关
    public static void insertSuggest(String name) {
        Suggest sug = ifSuggestExists(name);
        if (sug == null) {
            Suggest suggest = new Suggest();
            suggest.setName(name);
            suggest.setTimes(1L);
            getSuggestDao().insert(suggest);
        } else {
            //加一
            sug.setTimes(sug.getTimes() + 1L);
            updateSuggest(sug);
        }
    }

    private static void updateSuggest(Suggest sug) {
        getSuggestDao().update(sug);
    }

    public static String[] querySuggestLike(String str) {
        QueryBuilder<Suggest> builder = getSuggestDao().queryBuilder();
        List<Suggest> suggests = builder.where(SuggestDao.Properties.Name.like(str))
                .orderDesc(SuggestDao.Properties.Times)
                .build().list();
        Log.d(TAG, "querySuggestLike: " + suggests.size());
        if (suggests.size() != 0) {
            String[] strings = new String[suggests.size()];
            for (int i = 0; i < suggests.size(); i++) {
                strings[i] = suggests.get(i).getName();
            }
            return strings;
        }
        return null;
    }

    private static Suggest ifSuggestExists(String name) {
        try {
            QueryBuilder<Suggest> builder = getSuggestDao().queryBuilder();
            return builder.where(SuggestDao.Properties.Name.eq(name)).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
