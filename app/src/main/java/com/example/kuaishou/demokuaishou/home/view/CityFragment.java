package com.example.kuaishou.demokuaishou.home.view;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.example.kuaishou.demokuaishou.R;
import com.example.kuaishou.demokuaishou.base.BaseFragment;
import com.example.kuaishou.demokuaishou.common.ErrorBean;
import com.example.kuaishou.demokuaishou.home.contract.CityVideoContract;
import com.example.kuaishou.demokuaishou.home.mode.CityVideoBean;
import com.example.kuaishou.demokuaishou.home.presenter.CityVideoPresenterImpl;

public class CityFragment extends BaseFragment<CityVideoContract.CityVideoPresenter, CityVideoContract.ICityVideoView>implements CityVideoContract.ICityVideoView {

    private CityVideoAdapter cityVideoAdapter;
    private RecyclerView rv;

    @Override
    protected void initPresenter() {
        httpPresenter = new CityVideoPresenterImpl();
    }

    @Override
    protected void initView(View rootView) {
        initRv(rootView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_city;
    }

    private void initRv(View rootView) {
        rv = rootView.findViewById(R.id.rv);
        cityVideoAdapter = new CityVideoAdapter();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rv.setLayoutManager(staggeredGridLayoutManager);
        rv.setAdapter(cityVideoAdapter);
    }

    @Override
    protected void initData() {
        httpPresenter.findCityVideo();
    }

    @Override
    public void onCityVideo(CityVideoBean cityVideoBean) {
        cityVideoAdapter.updateData(cityVideoBean.getResult());
    }

    @Override
    public void showError(ErrorBean errorBean) {
        Toast.makeText(getContext(), errorBean.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
