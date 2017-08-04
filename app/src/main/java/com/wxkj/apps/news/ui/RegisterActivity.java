package com.wxkj.apps.news.ui;

import android.content.Intent;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;

import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.wxkj.apps.news.AppContext;
import com.wxkj.apps.news.R;
import com.wxkj.apps.news.api.remote.SimpleApi;
import com.wxkj.apps.news.base.BaseBackActivity;
import com.wxkj.apps.news.entity.BaseResposeInfo;
import com.wxkj.apps.news.entity.ErrorCode;
import com.wxkj.apps.news.utils.AccountValidatorUtil;
import com.wxkj.apps.news.utils.TDevice;

import butterknife.Bind;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by taosong on 17/4/16.
 */

public class RegisterActivity  extends BaseBackActivity implements View.OnClickListener {


    @Bind(R.id.et_username)
    AppCompatEditText mEtUserName;

    @Bind(R.id.et_password)
    AppCompatEditText mEtPassword;

    @Bind(R.id.et_repassword)
    AppCompatEditText  mEtRePassword;


    private String mUserName = "";
    private String mPassword = "";

    private String mRePassword = "";


    @Override
    protected int getContentView() {
        return R.layout.app_register;
    }


    @OnClick({R.id.btn_register})
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_register:

                handleRegister();

                break;
        }
    }


    private void handleRegister() {

        if (prepareForRegister()) {
            return;
        }
        mUserName = mEtUserName.getText().toString();
        mPassword = mEtPassword.getText().toString();


        SimpleApi.register(mUserName, mPassword, mHandler);
    }


    private final AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {


        @Override
        public void onStart() {
            super.onStart();
            showWaitDialog(R.string.data_loading);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            GsonBuilder gsonBuilder = new GsonBuilder();

            BaseResposeInfo baseResposeEntity = gsonBuilder.create().fromJson(new String(responseBody), BaseResposeInfo.class) ;


            if (baseResposeEntity != null) {
                handlerRegisterBean(baseResposeEntity);
            }else{
                AppContext.showToast(R.string.parse_data_error);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            requestFailureHint(error);
        }

        @Override
        public void onFinish() {
            super.onFinish();
            hideWaitDialog();
        }


        @Override
        public void onCancel() {
            super.onCancel();
            hideWaitDialog();
        }

    };


    private boolean prepareForRegister() {
        if (!TDevice.hasInternet()) {
            AppContext.showToastShort(R.string.tip_network_error);
            return true;
        }
        if (mEtUserName.length() == 0 ) {
            mEtUserName.setError("请输入手机号");
            mEtUserName.requestFocus();
            return true;
        }

        if (!AccountValidatorUtil.isMobile(mEtUserName.getText().toString())){
            mEtUserName.setError("请输入有效手机号");
            mEtUserName.requestFocus();
            return true;

        }


        if (mEtPassword.length() == 0) {
            mEtPassword.setError("请输入密码");
            mEtPassword.requestFocus();
            return true;
        }

        if (mEtRePassword.length() == 0){
            mEtRePassword.setError("确认一下密码");
            mEtRePassword.requestFocus();
            return true;

        }

        if (!(mPassword.equals(mRePassword))){

            mEtRePassword.setError("两次输入的密码不一样");
            mEtRePassword.requestFocus();
            return true;
        }

        return false;
    }


    public  void handlerRegisterBean(BaseResposeInfo baseResposeEntity){

        String code = baseResposeEntity.getCode();
        if (ErrorCode.REGSUCCESS.getValue().equals(code)){

            handleLoginSuccess(mUserName,mPassword);


        }else if(ErrorCode.REGFAIL.getValue().equals(code)){
            AppContext.showToast(R.string.register_faile_error);

        }else if(ErrorCode.REPEATREG.getValue().equals(code)){
            AppContext.showToast(R.string.register_re_faile);
        }else{
            AppContext.showToast(R.string.parse_data_error);

        }


    }



    private void handleLoginSuccess(String username,String userpass) {

        Intent data = new Intent();
        data.putExtra("username",username);
        data.putExtra("userpass",userpass);
        setResult(RESULT_OK, data);
        TDevice.hideSoftKeyboard(getWindow().getDecorView());
        finish();
    }


}
