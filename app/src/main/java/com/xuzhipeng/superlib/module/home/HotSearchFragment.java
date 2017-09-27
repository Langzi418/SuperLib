package com.xuzhipeng.superlib.module.home;


import android.view.View;
import com.moxun.tagcloudlib.view.TagCloudView;
import com.xuzhipeng.superlib.R;
import com.xuzhipeng.superlib.address.Address;
import com.xuzhipeng.superlib.base.LazyLoadFragment;
import com.xuzhipeng.superlib.common.util.NetWorkUtil;
import com.xuzhipeng.superlib.model.HotSearch;
import com.xuzhipeng.superlib.module.adapter.HotSearchAdapter;
import com.xuzhipeng.superlib.presenter.HotSearchPresenter;
import com.xuzhipeng.superlib.view.IHotSearchView;

import java.util.List;



public class HotSearchFragment extends LazyLoadFragment implements IHotSearchView {


    private HotSearchAdapter mAdapter;
    private HotSearchPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_hot_search;
    }

    @Override
    protected void initView(View view) {
        TagCloudView tagView = view.findViewById(R.id.hot_search_tcv);
        mAdapter = new HotSearchAdapter(getActivity());
        tagView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        mPresenter = new HotSearchPresenter(this);
    }

    @Override
    protected void loadData() {

        if(!NetWorkUtil.isNetworkConnected(getActivity())){
            return;
        }

        super.loadData();
        mPresenter.loadHotSearch(Address.getHotSearch());
    }

    @Override
    public void setHotSearch(List<HotSearch> searches) {
        mAdapter.setNewData(searches);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mPresenter!=null){
            mPresenter.detachView();
        }
    }
}
