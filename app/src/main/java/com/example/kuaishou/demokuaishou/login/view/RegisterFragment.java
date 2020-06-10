package com.example.kuaishou.demokuaishou.login.view;

import android.view.View;
import android.widget.EditText;

import com.example.kuaishou.demokuaishou.user.KSUserManager;
import com.example.kuaishou.demokuaishou.R;
import com.example.kuaishou.demokuaishou.base.BaseFragment;
import com.example.kuaishou.demokuaishou.common.ErrorBean;
import com.example.kuaishou.demokuaishou.home.view.MainActivity;
import com.example.kuaishou.demokuaishou.login.contract.RegisterContract;
import com.example.kuaishou.demokuaishou.login.mode.LoginBean;
import com.example.kuaishou.demokuaishou.login.mode.RegisterBean;
import com.example.kuaishou.demokuaishou.login.presenter.RegisterPresenterImpl;
import com.umeng.analytics.MobclickAgent;


public class RegisterFragment extends BaseFragment<RegisterContract.RegisterPresenter, RegisterContract.IRegisterView> implements View.OnClickListener, RegisterContract.IRegisterView
{

    private EditText passwordEditText;
    private EditText nameEditText;


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerButton:
                httpPresenter.register(nameEditText.getText().toString(), passwordEditText.getText().toString());
                break;
            case R.id.loginTv:
                ((LoginRegisterActiviy)getActivity()).switchFragment(0);//切换到登录Fragment
                break;
        }
    }

    @Override
    protected void initPresenter() {
        httpPresenter = new RegisterPresenterImpl();

    }

    @Override
    protected void initView(View rootView) {
        nameEditText = rootView.findViewById(R.id.name);
        passwordEditText = rootView.findViewById(R.id.password);
        rootView.findViewById(R.id.registerButton).setOnClickListener(this);
        rootView.findViewById(R.id.loginTv).setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onRegister(RegisterBean registerBean) {

    }

    @Override
    public void onLogin(LoginBean loginBean) {
        //修改当前用户登录的状态
        KSUserManager.getInstance().saveLoginBean(loginBean);
        KSUserManager.getInstance().saveToken(loginBean.getResult().getToken());
        getActivity().finish();
        MainActivity.launch(getActivity(), 1);

        MobclickAgent.onProfileSignIn(loginBean.getResult().getId());
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
}
