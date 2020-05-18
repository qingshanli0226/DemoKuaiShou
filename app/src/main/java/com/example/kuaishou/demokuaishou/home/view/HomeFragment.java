package com.example.kuaishou.demokuaishou.home.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.kuaishou.demokuaishou.R;
import com.example.kuaishou.demokuaishou.common.ErrorBean;
import com.example.kuaishou.demokuaishou.home.contract.FindVideoContract;
import com.example.kuaishou.demokuaishou.home.mode.FindVideoBean;
import com.example.kuaishou.demokuaishou.home.mode.FindVideoDataBean;
import com.example.kuaishou.demokuaishou.home.presenter.FindVideoPresenter;

import java.util.List;

//
public class HomeFragment extends Fragment implements FindVideoContract.IFindVideoView {

    private FindVideoAdapter findVideoAdapter;
    private RecyclerView rv;
    private FindVideoPresenter findVideoPresenter;
    private ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_home,container,false);
        progressBar = rootView.findViewById(R.id.progressBar);
        initRv(rootView);
        return rootView;
    }

    private void initRv(View rootView) {
        rv = rootView.findViewById(R.id.rv);
        findVideoAdapter = new FindVideoAdapter();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rv.setLayoutManager(staggeredGridLayoutManager);
        rv.setAdapter(findVideoAdapter);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findVideoPresenter = new FindVideoPresenter();
        findVideoPresenter.attachView(this);
        findVideoPresenter.findVideo();
    }

    @Override
    public void onFindVideo(List<FindVideoDataBean> findVideoDataBeanList) {
        findVideoAdapter.updateData(findVideoDataBeanList);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //回收视图，避免内存泄漏
        findVideoPresenter.detachView();
    }

    @Override
    public void showError(ErrorBean errorBean) {
        Toast.makeText(getContext(), errorBean.getMessage(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }
}
