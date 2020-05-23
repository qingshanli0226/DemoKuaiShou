package com.example.kuaishou.demokuaishou.login.view;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kuaishou.demokuaishou.user.KSUserManager;
import com.example.kuaishou.demokuaishou.R;
import com.example.kuaishou.demokuaishou.base.BaseFragment;
import com.example.kuaishou.demokuaishou.common.ErrorBean;
import com.example.kuaishou.demokuaishou.home.view.MainActivity;
import com.example.kuaishou.demokuaishou.login.contract.LoginContract;
import com.example.kuaishou.demokuaishou.login.mode.LoginBean;
import com.example.kuaishou.demokuaishou.login.presenter.LoginPresenterImpl;


public class LoginFragment extends BaseFragment<LoginContract.LoginPresenter, LoginContract.ILoginView> implements View.OnClickListener,
        LoginContract.ILoginView {

    private EditText passwordEditText;
    private EditText nameEditText;


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginButton:
                httpPresenter.login(nameEditText.getText().toString(), passwordEditText.getText().toString());
                break;
            case R.id.registerTv:
                ((LoginRegisterActiviy)getActivity()).switchFragment(1);//切换到注册Fragment
                break;

        }
    }

    @Override
    protected void initPresenter() {
        httpPresenter = new LoginPresenterImpl();
    }

    @Override
    protected void initView(View rootView) {
         nameEditText = rootView.findViewById(R.id.name);
         passwordEditText = rootView.findViewById(R.id.password);
         rootView.findViewById(R.id.loginButton).setOnClickListener(this);
         rootView.findViewById(R.id.registerTv).setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onLogin(LoginBean loginBean) {
        //修改当前用户登录的状态
        KSUserManager.getInstance().saveLoginBean(loginBean);
        KSUserManager.getInstance().saveToken(loginBean.getResult().getToken());
        getActivity().finish();
        MainActivity.launch(getActivity(), 1);
    }

    @Override
    public void showError(ErrorBean errorBean) {
        Toast.makeText(getActivity(), errorBean.getMessage(),Toast.LENGTH_SHORT).show();
        if (errorBean.getCode().equals("1001")) {//代表的用户没有注册过，跳转到注册界面
            ((LoginRegisterActiviy)getActivity()).switchFragment(1);
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
