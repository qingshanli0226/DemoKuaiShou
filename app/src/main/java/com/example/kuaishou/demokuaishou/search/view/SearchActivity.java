package com.example.kuaishou.demokuaishou.search.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.kuaishou.demokuaishou.R;
import com.example.kuaishou.demokuaishou.base.BaseActivity;
import com.example.kuaishou.demokuaishou.cache.CacheManager;
import com.example.kuaishou.demokuaishou.cache.dao.SearhEntityDao;
import com.example.kuaishou.demokuaishou.common.ErrorBean;
import com.example.kuaishou.demokuaishou.search.SearchContract;
import com.example.kuaishou.demokuaishou.search.mode.SearchBean;
import com.example.kuaishou.demokuaishou.search.mode.SearhEntity;
import com.example.kuaishou.demokuaishou.search.presenter.SearchPresenterImpl;

import org.greenrobot.greendao.Property;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity<SearchPresenterImpl, SearchContract.ISearchView> implements SearchContract.ISearchView, View.OnClickListener {
    private List<SearhEntity> searhEntityList = new ArrayList<>();

    private EditText searchEditText;
    private SearchRecyclerView searchRecyclerView;
    private SearchAdapter searchAdapter;

    private Button btnSearch;

    @Override
    protected void initPresenter() {
        httpPresenter = new SearchPresenterImpl();
    }

    @Override
    protected void initData() {
        //从数据库读取历史搜索记录数据,放在子线程中
        CacheManager.getInstance().getSingleExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                //获取数据库缓存，最多10条，并且按照时间降序排列的数据
                searhEntityList = CacheManager.getInstance().getSearhEntityDao()
                        .queryBuilder().orderDesc(SearhEntityDao.Properties.Time).limit(10).list();
                handler.sendEmptyMessage(1);
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                searchAdapter.updateData(searhEntityList);
            } else if (msg.what == 2){
                SearhEntity searhEntity = (SearhEntity) msg.obj;
                searhEntityList.add(0, searhEntity);//添加到0的位置
                searchAdapter.updateOneData(0);
            }
        }
    };

    @Override
    protected void initView() {
        searchEditText = findViewById(R.id.searchEditText);
        searchRecyclerView = findViewById(R.id.history);
        searchRecyclerView.setLayoutManager(new SearchRecyclerViewManager(this));
        searchRecyclerView.setDisplayAll(false);//开始并不全部显示
        searchAdapter = new SearchAdapter();
        searchRecyclerView.setAdapter(searchAdapter);

        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);
        findViewById(R.id.btnImage).setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }


    public static void launch(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, SearchActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onSearchData(SearchBean searchBean) {

    }

    @Override
    public void showError(ErrorBean errorBean) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSearch:
                if (TextUtils.isEmpty(searchEditText.getText().toString())) {
                    return;
                }
                searchData();
                addNewHistory();
                break;

            case R.id.btnImage:
                if (!searchRecyclerView.isDisplayAll()) {
                    searchRecyclerView.setDisplayAll(true);
                } else {
                    searchRecyclerView.setDisplayAll(false);
                }
                break;
        }
    }

    private void searchData() {
    }

    private void addNewHistory() {
        final SearhEntity searhEntity = new SearhEntity();
        searhEntity.setSearchContent(searchEditText.getText().toString());
        searhEntity.setTime(System.currentTimeMillis());
        CacheManager.getInstance().getSingleExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                //向数据库插入
                if (searhEntityList.size() == 10) {
                    //在数据库中将时间最久的删掉
                    CacheManager.getInstance().getSearhEntityDao().delete(searhEntityList.get(9));
                    searhEntityList.remove(9);//内存中删除
                }
                CacheManager.getInstance().getSearhEntityDao().insert(searhEntity);
                Message message = new Message();
                message.what = 2;
                message.obj = searhEntity;
                handler.sendMessage(message);
            }
        });
    }
}
