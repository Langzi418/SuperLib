package com.xuzhipeng.superlib;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.xuzhipeng.superlib.base.BaseActivity;
import com.xuzhipeng.superlib.base.MyFragmentPagerAdapter;
import com.xuzhipeng.superlib.common.util.PrefUtil;
import com.xuzhipeng.superlib.common.util.ViewUtil;
import com.xuzhipeng.superlib.db.DBUtil;
import com.xuzhipeng.superlib.model.Search;
import com.xuzhipeng.superlib.module.collection.CollectActivity;
import com.xuzhipeng.superlib.module.home.HotBookFragment;
import com.xuzhipeng.superlib.module.home.HotSearchFragment;
import com.xuzhipeng.superlib.module.intro.BookIntroActivity;
import com.xuzhipeng.superlib.module.mylib.LoginActivity;
import com.xuzhipeng.superlib.module.mylib.MyLibActivity;
import com.xuzhipeng.superlib.module.update.UpdateActivity;
import com.xuzhipeng.superlib.module.update.UpdateUtil;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private MaterialSearchView mSearchView;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavView;
    private TextView mUserTv;
    private Spinner mSearchTypeSpinner;
    private Spinner mDocTypeSpinner;
    private Spinner mDisplaySpinner;
    private Spinner mSortSpinner;
    private Spinner mAscDesSpinner;
    private MyFragmentPagerAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        //检测更新
        UpdateUtil.update(this);

        mDrawerLayout = findViewById(R.id.draw_layout);
        mSearchView = findViewById(R.id.search_view);
        mNavView = findViewById(R.id.nav_view);
        View headerView = mNavView.getHeaderView(0);
        mUserTv = headerView.findViewById(R.id.user_info_tv);

        mSearchTypeSpinner = findViewById(R.id.search_type_spinner);
        mDocTypeSpinner = findViewById(R.id.doc_type_spinner);
        mDisplaySpinner = findViewById(R.id.display_spinner);
        mSortSpinner = findViewById(R.id.sort_spinner);
        mAscDesSpinner = findViewById(R.id.asc_or_des_spinner);
    }

    @Override
    protected void setView() {
        setToolbar(R.string.app_name, R.drawable.ic_menu);
        mAdapter = new MyFragmentPagerAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                        .add(R.string.hot_search, HotSearchFragment.class)
                        .add(R.string.hot_book, HotBookFragment.class)
                        .create()
        );

        ViewUtil.setViewPager(mAdapter, this);
        mSearchView.setHint(getString(R.string.please_search));
        setSpinner(this, mSearchTypeSpinner, R.array.searchType);
        setSpinner(this, mDocTypeSpinner, R.array.docType);
        setSpinner(this, mDisplaySpinner, R.array.display);
        setSpinner(this, mSortSpinner, R.array.sort);
        setSpinner(this, mAscDesSpinner, R.array.asc_des);
    }

    @Override
    protected void setListener() {
        /**
         *  搜索点击设置
         */
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {

                if (TextUtils.isEmpty(query)) {
                    Toast.makeText(MainActivity.this, R.string.no_content, Toast.LENGTH_SHORT)
                            .show();
                    return true;
                }

                //插入搜索记录
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DBUtil.insertSuggest(query);
                    }
                }).start();

                goSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                getSuggest(newText);
                return false;
            }
        });


        /**
         *  menu 点击
         */
        mNavView.setNavigationItemSelectedListener(new NavigationView
                .OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.nav_search:
                        mDrawerLayout.openDrawer(Gravity.END);
                        break;
                    case R.id.nav_change:
                        startActivity(LoginActivity.newIntent(MainActivity.this));
                        break;
                    case R.id.nav_mylib:
                        goIfLogin();
                        break;
                    case R.id.nav_collection:
                        startActivity(CollectActivity.newIntent(MainActivity.this));
                        break;
                    case R.id.nav_about:
                        startActivity(UpdateActivity.newIntent(MainActivity.this));
                        break;
                }

                return false;
            }
        });


    }

    /**
     * 执行 search
     */
    private void goSearch(String query) {
        Search search = new Search();
        search.setStrText(query);
        search.setStrSearchType(mSearchTypeSpinner.getSelectedItemId());
        search.setDoctype(mDocTypeSpinner.getSelectedItemId());
        search.setDisplaypg(mDisplaySpinner.getSelectedItemId());
        search.setSort(mSortSpinner.getSelectedItemId());
        search.setOrderby(mAscDesSpinner.getSelectedItemId());
        startActivity(BookIntroActivity.newIntent
                (MainActivity.this, search.toString(), search.getDisplaypg()));
    }

    /**
     * 从数据库中得到数据
     */
    private void getSuggest(final String newText) {
        Observable.create(new ObservableOnSubscribe<String[]>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String[]> e) throws Exception {
                String[] names = DBUtil.querySuggestLike(newText);
                if (names != null && names.length > 0) {
                    e.onNext(names);
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String[]>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull final String[] strings) {
                        mSearchView.setSuggestions(strings);
                        mSearchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int
                                    position, long id) {
                                goSearch(strings[position]);
                            }
                        });
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    /**
     * 根据是否登录跳转
     */
    private void goIfLogin() {
        if (PrefUtil.getSuccess(MainActivity.this)) {
            startActivity(MyLibActivity.newIntent(MainActivity.this));
        } else {
            startActivity(LoginActivity.newIntent(MainActivity.this));
        }
    }


    /**
     * resume 刷新
     */
    @Override
    protected void onResume() {
        super.onResume();
        String userNo = PrefUtil.getUserNo(this);
        if (userNo != null) {
            mUserTv.setText(userNo);
        }else{
            mUserTv.setText(null);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 切换学校返回
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    mAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    /**
     * @param spinner 某个spinner
     * @param itemsId 对应的 数据 资源 id
     */
    public void setSpinner(Context context, Spinner spinner, int itemsId) {
        String[] items = context.getResources().getStringArray(itemsId);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

}
