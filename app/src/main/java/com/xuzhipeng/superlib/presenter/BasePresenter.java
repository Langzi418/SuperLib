package com.xuzhipeng.superlib.presenter;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/7/31
 */

public class BasePresenter<T>  {

    protected T mView;

    protected void attachView(T view){
        mView = view;
    }

    public void detachView() {
        mView = null;
    }


}
