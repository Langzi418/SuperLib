package com.xuzhipeng.superlib.module.college;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xuzhipeng.superlib.MainActivity;
import com.xuzhipeng.superlib.R;
import com.xuzhipeng.superlib.base.BaseActivity;
import com.xuzhipeng.superlib.common.util.HttpUtil;
import com.xuzhipeng.superlib.common.util.NetWorkUtil;
import com.xuzhipeng.superlib.common.util.PrefUtil;
import com.xuzhipeng.superlib.common.util.ViewUtil;

import java.lang.reflect.Type;
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

public class CollegeActivity extends BaseActivity {

    private static final String TAG = "CollegeActivity";

    private RecyclerView mCollegeRv;
    private CollegeAdapter mCollegeAdapter;
    private HasNetWorkReceiver mReceiver;

    public static Intent newIntent(Context context) {
        return new Intent(context,CollegeActivity.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_college;
    }

    @Override
    protected void initView() {
        mCollegeRv =  findViewById(R.id.college_rv);
    }

    @Override
    protected void setView() {
        setToolbarTitle(R.string.choose_college);
        mCollegeAdapter = new CollegeAdapter(null);
        mCollegeRv.setLayoutManager(new LinearLayoutManager(this));
        mCollegeRv.setAdapter(mCollegeAdapter);
    }

    //onCreate
    @Override
    protected void setListener() {
        mReceiver = new HasNetWorkReceiver();
        mReceiver.registerNet(this,mReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mReceiver.unregisterNet(this,mReceiver);
    }

    /**
     *  多布局 adapter
     */
    private class CollegeAdapter extends BaseMultiItemQuickAdapter<College, BaseViewHolder> {
        public CollegeAdapter(List<College> data) {
            super(data);
            addItemType(0, R.layout.item_college_tip);
            addItemType(1, R.layout.item_college);
        }

        @Override
        protected void convert(BaseViewHolder helper, final College item) {
            switch (item.getItemType()) {
                case 1:
                    helper.setText(R.id.college_tv, item.name);
                    TextView textView = helper.getView(R.id.college_tv);
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            MaterialDialog.Builder builder =
                                    ViewUtil.showTwoDialog(CollegeActivity.this,
                                            getString(R.string.college_ok,item.name));
                            builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@android.support.annotation.NonNull MaterialDialog dialog, @android.support.annotation.NonNull DialogAction which) {
                                    PrefUtil.setBaseUrl(CollegeActivity.this,item.base);
                                    startActivity(new Intent(CollegeActivity.this, MainActivity.class));
                                    PrefUtil.setFirstStart(CollegeActivity.this,false);
                                    finish();
                                }
                            }).onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@android.support.annotation.NonNull MaterialDialog dialog, @android.support.annotation.NonNull DialogAction which) {
                                    dialog.dismiss();
                                }
                            }).show();
                        }
                    });
                    break;
                case 0:
                    helper.setText(R.id.college_tip, item.name);
                    break;
            }
        }
    }


    /**
     *  网络连接工作
     */
    public class HasNetWorkReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(NetWorkUtil.isNetworkConnected(CollegeActivity.this)){
                MaterialDialog dialog = ViewUtil.getProgressBar(CollegeActivity.this,R.string.load_data);
                requestCollege();
                dialog.dismiss();
            }
        }

        /**
         * 注册网络广播
         */
        public void registerNet(Context context,HasNetWorkReceiver receiver) {
            IntentFilter filter =
                    new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            context.registerReceiver(receiver, filter);

        }

        /**
         * 取消注册
         */
        public  void unregisterNet(Context context,HasNetWorkReceiver receiver) {
            context.unregisterReceiver(receiver);
        }
    }


    public static final String URL_COLLEGE = "http://101.132.121.229/college.txt" ;

    /**
     *  请求些学校数据
     */
    private void requestCollege() {
        Observable.create(new ObservableOnSubscribe<List<College>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<College>> e) throws Exception {
                String html = HttpUtil.sendOkHttp(URL_COLLEGE);
                if(html == null){
                    //备用
                    html = CollegeData.DATA;
                }

                Type type = new TypeToken<ArrayList<College>>(){}.getType();
                List<College> colleges = new Gson().fromJson(html,type);
                if(colleges == null){
                    colleges = new ArrayList<>();
                }
                e.onNext(colleges);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<List<College>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull List<College> colleges) {
                mCollegeAdapter.setNewData(colleges);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "collegeError: ",e);
            }

            @Override
            public void onComplete() {

            }
        });
    }

}
