package com.example.kuaishou.demokuaishou.home.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kuaishou.demokuaishou.R;
import com.example.kuaishou.demokuaishou.home.contract.FocusVideoContract;
import com.example.kuaishou.demokuaishou.home.mode.FindVideoBean;
import com.example.kuaishou.demokuaishou.home.mode.FocusVideoBean;
import com.example.kuaishou.demokuaishou.home.presenter.FocusVideoPresenter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class FocusFragment extends Fragment implements FocusVideoContract.IFocusView {

    private FocusVideoAdapter focusVideoAdapter;
    private RecyclerView rv;

    private FocusVideoPresenter focusVideoPresenter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_focus,container,false);

        initRv(rootView);
        return rootView;
    }

    private void initRv(View rootView) {
        rv = rootView.findViewById(R.id.rv);
        focusVideoAdapter = new FocusVideoAdapter();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rv.setLayoutManager(staggeredGridLayoutManager);
        rv.setAdapter(focusVideoAdapter);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        focusVideoPresenter = new FocusVideoPresenter();
        focusVideoPresenter.attchView(this);
        focusVideoPresenter.focusVideo();

    }

    @Override
    public void onFocusVideo(FocusVideoBean focusVideoBean) {
        focusVideoAdapter.updateData(focusVideoBean.getResult());
    }
}
