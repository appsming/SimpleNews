package com.wxkj.apps.news.ui;

import android.content.Intent;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.wxkj.apps.news.AppConfig;
import com.wxkj.apps.news.AppContext;
import com.wxkj.apps.news.R;
import com.wxkj.apps.news.api.ApiHttpClient;
import com.wxkj.apps.news.api.remote.SimpleApi;
import com.wxkj.apps.news.base.BaseBackActivity;
import com.wxkj.apps.news.entity.ErrorCode;
import com.wxkj.apps.news.entity.UserInfo;
import com.wxkj.apps.news.entity.UserInfoJson;
import com.wxkj.apps.news.utils.AccountValidatorUtil;
import com.wxkj.apps.news.utils.TDevice;
import com.wxkj.apps.news.utils.UIHelper;

import org.kymjs.kjframe.http.HttpConfig;

import java.lang.reflect.Type;

import butterknife.Bind;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.client.protocol.ClientContext;
import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.protocol.HttpContext;

import static com.wxkj.apps.news.R.id.btn_login;
import static com.wxkj.apps.news.R.id.btn_register;


public class LoginActivity  extends BaseBackActivity implements View.OnClickListener {

    @Bind(R.id.et_username)
    AppCompatEditText mEtUserName;

    @Bind(R.id.et_password)
    AppCompatEditText mEtPassword;


    private String mUserName = "";
    private String mPassword = "";


    @Override
    protected int getContentView() {
        return R.layout.app_login;
    }



    @OnClick({btn_register, btn_login})
    @Override
    public void onClick(View view) {

        int vId  = view.getId();


        switch (vId){

            case btn_login:
                handleLogin();
                break;

            case btn_register:
                UIHelper.showRegisterActivity(LoginActivity.this, AppConfig.REQUEST_REGISTER_CODE);
                break;
        }
    }



    private void handleLogin() {

        if (prepareForLogin()) {
            return;
        }
        mUserName = mEtUserName.getText().toString();
        mPassword = mEtPassword.getText().toString();


        SimpleApi.login(mUserName, mPassword, mHandler);
    }


    private final AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {


        @Override
        public void onStart() {
            super.onStart();
            showWaitDialog(R.string.data_loading);
        }


        @Override
        public void onSuccess(int arg0, Header[] arg1, byte[] responseBody) {
            Type type = new TypeToken<UserInfoJson>() {
            }.getType();

            GsonBuilder gsonBuilder = new GsonBuilder();

            UserInfoJson  userInfoJson = gsonBuilder.create().fromJson(new String(responseBody),type) ;

            if (userInfoJson != null) {
                handleLoginBean(userInfoJson, arg1);
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


    private boolean prepareForLogin() {
        if (!TDevice.hasInternet()) {
            AppContext.showToastShort(R.string.tip_network_error);
            return true;
        }
        if (mEtUserName.length() == 0) {
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

        return false;
    }


    // 处理loginBean
    private void handleLoginBean(UserInfoJson userInfoJson, Header[] headers) {

        String code = userInfoJson.getCode();
        if (ErrorCode.SUCCESS.getValue().equals(code)){

            AsyncHttpClient client = ApiHttpClient.getHttpClient();
            HttpContext httpContext = client.getHttpContext();
            CookieStore cookies = (CookieStore) httpContext
                    .getAttribute(ClientContext.COOKIE_STORE);
            if (cookies != null) {
                String tmpcookies = "";
                for (Cookie c : cookies.getCookies()) {

                    tmpcookies += (c.getName() + "=" + c.getValue()) + ";";
                }
                if (TextUtils.isEmpty(tmpcookies)) {

                    if (headers != null) {
                        for (Header header : headers) {
                            String key = header.getName();
                            String value = header.getValue();
                            if (key.contains("Set-Cookie"))
                                tmpcookies += value + ";";
                        }
                        if (tmpcookies.length() > 0) {
                            tmpcookies = tmpcookies.substring(0, tmpcookies.length() - 1);
                        }
                    }
                }

                AppContext.getInstance().setProperty(AppConfig.CONF_COOKIE,
                        tmpcookies);
                ApiHttpClient.setCookie(ApiHttpClient.getCookie(AppContext
                        .getInstance()));
                HttpConfig.sCookie = tmpcookies;
            }

            UserInfo userInfo  = userInfoJson.getObjOne();
            userInfo.setLogin(true);
            AppContext.getInstance().saveUserInfo(userInfo);
            handleLoginSuccess();


        }else if(ErrorCode.NOTFOUND.getValue().equals(code)){


            AppContext.showToast(R.string.login_faile_error);

        }else if(ErrorCode.FAIL.getValue().equals(code)){
            AppContext.showToast(R.string.request_error_hint);

        }else{
            AppContext.showToast(R.string.parse_data_error);

        }


    }



    private void handleLoginSuccess() {

        Intent data = new Intent();
        setResult(RESULT_OK, data);
        TDevice.hideSoftKeyboard(getWindow().getDecorView());
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (null == data){

            return;
        }

        if (requestCode  == AppConfig.REQUEST_REGISTER_CODE && resultCode == RESULT_OK){


            String  userName = data.getStringExtra("username");

            String userPasswd = data.getStringExtra("userpass");

            mEtUserName.setText(userName);

            mEtPassword.setText(userPasswd);

            handleLogin();



        }
    }
}