package com.xuzhipeng.superlib.module.mylib;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xuzhipeng.superlib.R;
import com.xuzhipeng.superlib.base.LazyLoadFragment;
import com.xuzhipeng.superlib.common.util.NetWorkUtil;
import com.xuzhipeng.superlib.common.util.PrefUtil;
import com.xuzhipeng.superlib.db.DBUtil;
import com.xuzhipeng.superlib.db.User;
import com.xuzhipeng.superlib.module.adapter.ZJXXAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ZJXXFragment extends LazyLoadFragment {

    private static final String TAG = "ZJXXFragment";
    private static final String BUNDLE_HTML = "html";

    private RecyclerView mZjxxRv;
    private String zjxxHtml;
    private ZJXXAdapter mZJXXAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_zjxx;
    }

    @Override
    protected void initView(View view) {
        mZjxxRv =  view.findViewById(R.id.zjxx_rv);
    }


    @Override
    protected void setView() {
        mZJXXAdapter = new ZJXXAdapter(R.layout.item_zjxx, null);
        mZjxxRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mZjxxRv.setNestedScrollingEnabled(false);
        mZjxxRv.setAdapter(mZJXXAdapter);
    }

    @Override
    protected void getExtra() {
        zjxxHtml = getArguments().getString(BUNDLE_HTML);
    }

    @Override
    protected void loadData() {
        if(!NetWorkUtil.isNetworkConnected(getActivity())){
            return;
        }

        setData();
    }

    public void setData() {
        Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<String>> e) throws Exception {
                List<String> infos = new ArrayList<>();
                if (zjxxHtml == null) {
                    zjxxHtml = MyLibHttp.getInstance(getActivity()).loadZJXX();
                    if (zjxxHtml == null) {
                        e.onNext(infos);
                        e.onComplete();
                    }
                }

                Document doc = Jsoup.parse(zjxxHtml);
                Elements content = doc.select("div#mylib_content");
                Elements a = content.select("div.mylib_msg a");
                if (a.size() != 0) {
                    infos.add(a.get(0).text());
                    infos.add(a.get(1).text());
                }

                Elements tds = content.select("div#mylib_info table td");
                String username = null;
                if (tds.size() != 0) {
                    infos.add(tds.get(1).text());
                    infos.add(tds.get(4).text());
                    infos.add(tds.get(6).text());
                    infos.add(tds.get(7).text());
                    infos.add(tds.get(10).text());
                    infos.add(tds.get(12).text());
                    infos.add(tds.get(13).text());
                    infos.add(tds.get(14).text());
                    username = tds.get(1).text().split("：")[1];
                }

                checkIfInsert(username);

                e.onNext(infos);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        ZJXXFragment.super.showProgress();
                    }

                    @Override
                    public void onNext(@NonNull List<String> list) {
                        mZJXXAdapter.setNewData(list);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        ZJXXFragment.super.hideProgress();


                    }

                    @Override
                    public void onComplete() {
                        ZJXXFragment.super.hideProgress();
                    }
                });
    }

    /**
     * 数据库中不存在则保存
     */
    private void checkIfInsert(String username) {
        //刷新用户名
        PrefUtil.setUserName(getActivity(),username);
        //登录成功,可以显示用户名了
        PrefUtil.setSuccess(getActivity(), true);
        User user = DBUtil.queryUserByStuNo(PrefUtil.getUserNo(getActivity()));
        if (user != null) {
            //用户信息存在，不存储，但刷新用户id
            PrefUtil.setUserId(getActivity(),user.getId());
            DBUtil.closeDB();
            return;
        }

        user = new User();
        user.setStuId(PrefUtil.getUserNo(getActivity()));
        user.setName(username);
        Long id = DBUtil.insertUser(user);
        PrefUtil.setUserId(getActivity(),id);
        DBUtil.closeDB();
    }
}
