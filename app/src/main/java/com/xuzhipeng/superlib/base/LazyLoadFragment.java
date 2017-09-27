package com.xuzhipeng.superlib.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.xuzhipeng.superlib.R;
import com.xuzhipeng.superlib.common.util.ViewUtil;
import com.xuzhipeng.superlib.view.ILoadView;


/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/8/19
 */

public abstract class LazyLoadFragment extends Fragment implements ILoadView {

    private static final String TAG = "LazyLoadFragment";

    private MaterialDialog dialog;
    
    //控件是否初始化完成
    private boolean isViewCreated;

    //数据是否加载完毕
    private boolean isLoadDataCompleted;

    protected abstract int getLayoutId();

    protected abstract void initView(View view);

    protected void loadData(){

    }

    /**
     * 加载完成
     */
    private void loadCompleted(){
        isLoadDataCompleted = true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(),container,false);
        initView(view);
        getExtra();
        setView();
        setListener();
        initData();

        isViewCreated = true;
        return view;
    }

    /**
     *  初始化数据
     */
    protected void initData() {
    }

    /**
     *  设置监听
     */
    protected void setListener() {

    }

    protected void getExtra() {

    }


    /**
     *  设置视图
     */
    protected void setView() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser && isViewCreated  && !isLoadDataCompleted){
            loadData();
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getUserVisibleHint()){
            loadData();
        }
    }

    @Override
    public void showProgress() {
        dialog  = ViewUtil.getProgressBar(getActivity(), R.string.load_data);
    }

    @Override
    public void hideProgress() {
        if(! dialog.isCancelled()){
            dialog.dismiss();
        }
        loadCompleted();
    }
}
