package com.xuzhipeng.superlib.db;

import com.xuzhipeng.superlib.common.app.App;

public class DaoManager {
    private static  final String  DB_NAME="Book.db";//数据库名称
    private volatile  static DaoManager manager;//多线程访问
    private  static DaoMaster.DevOpenHelper helper;
    private static  DaoMaster daoMaster;
    private static DaoSession daoSession;

    /**
     * 使用单例模式获得操作数据库的对象
     */
    public  static DaoManager getInstance(){

        if (manager==null){
            synchronized (DaoManager.class){
                if (manager==null){
                    manager = new DaoManager();
                }
            }
        }
        return manager;
    }


    /**
     * 判断是否存在数据库，如果没有则创建数据库
     */
    private DaoMaster getDaoMaster(){
        if (daoMaster==null){
            DaoMaster.DevOpenHelper helper =
                    new DaoMaster.DevOpenHelper(App.getContext(),DB_NAME,null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    /**
     * 完成对数据库的添加、删除、修改、查询的操作，仅仅是一个接口
     */
    private DaoSession getDaoSession(){
        if (daoSession==null){
            if (daoMaster==null){
                daoMaster = getDaoMaster();
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }



    public BookDao getBookDao(){
        return getDaoSession().getBookDao();
    }

    public UserDao getUserDao(){
        return getDaoSession().getUserDao();
    }

    public SuggestDao getSuggestDao(){return getDaoSession().getSuggestDao();}

    public CollectDao getCollectDao(){return getDaoSession().getCollectDao();}

    /**
     * 关闭所有的操作,数据库开启的时候，使用完毕了必须要关闭
     */
    public void closeConnection(){
        closeHelper();
        closeDaoSession();
    }
    private void  closeHelper(){
        if (helper!=null){
            helper.close();
            helper = null;
        }
    }
    private void closeDaoSession(){
        if (daoSession!=null){
            daoSession.clear();
            daoSession = null;
        }
    }
}
