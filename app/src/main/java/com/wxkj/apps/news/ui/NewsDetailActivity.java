package com.wxkj.apps.news.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.wxkj.apps.news.AppContext;
import com.wxkj.apps.news.R;
import com.wxkj.apps.news.api.remote.SimpleApi;
import com.wxkj.apps.news.base.BaseBackActivity;
import com.wxkj.apps.news.entity.BaseResposeInfo;
import com.wxkj.apps.news.entity.ErrorCode;
import com.wxkj.apps.news.entity.NewsInfo;
import com.wxkj.apps.news.entity.NewsInfoJson;
import com.wxkj.apps.news.entity.UserInfo;
import com.wxkj.apps.news.utils.DialogHelp;
import com.wxkj.apps.news.utils.UIHelper;
import com.wxkj.apps.news.widget.EmptyLayout;

import java.lang.reflect.Type;

import butterknife.Bind;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

import static com.wxkj.apps.news.R.id.btn_collection;
import static com.wxkj.apps.news.R.id.btn_give;

/**
 * Created by taosong on 17/6/5.
 */

public class NewsDetailActivity  extends BaseBackActivity {



    @Bind(R.id.news_title)
    TextView  mNewsTitle;

    @Bind(R.id.news_desc)
    TextView  mNewsDesc;

    @Bind(R.id.news_content)
    WebView   mNewsContent;

    @Bind(R.id.error_layout)
    EmptyLayout  mErrorLayout;


    @Bind(R.id.btn_collection)
    Button  mCollectionBtn;

    @Bind(R.id.btn_give)
    Button  mGiveBtn;


    public final static String NEWS_INFO_DETAIL = "NEWS_INFO_DETAIL";

    private  String  newsId = null;

    private UserInfo  userInfo = null;

    private String  tempStoreType = "";




    public static void show(Context context, String  nId) {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra(NEWS_INFO_DETAIL,nId);
        context.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.app_news_detail;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        try {
            newsId  = getIntent().getStringExtra(NEWS_INFO_DETAIL);
        }catch (Exception e){
            e.printStackTrace();
        }


        // 获取WebView的设置
        WebSettings webSettings = mNewsContent.getSettings();
        // 将JavaScript设置为可用，这一句话是必须的，不然所做一切都是徒劳的
        webSettings.setJavaScriptEnabled(true);

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式


    }


    @Override
    protected void initData() {
        super.initData();





        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNewsDetail();
            }

    });
        //步骤3. 复写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器， 而是在本WebView中显示
        mNewsContent.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

      //  loadNewsDetail();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNewsDetail();
    }

    private  void loadNewsDetail(){

          userInfo = AppContext.getInstance().getLoginUser();

        String userName = "";
        String userId = "";

        if (null !=userInfo && userInfo.isLogin()) {

            userName =  userInfo.getUname();

            userId = userInfo.getId();
        }

        SimpleApi.newsDetail(userName,newsId,userId,mHandler);



    }


    private AsyncHttpResponseHandler  mHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onStart() {
            super.onStart();
            mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);

        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            Log.i("TAG","=mHandler===onSuccess==responseBody========"+new String(responseBody));

            Type type = new TypeToken<NewsInfoJson>() {
            }.getType();

            GsonBuilder gsonBuilder = new GsonBuilder();

            NewsInfoJson  newsInfoJson = gsonBuilder.create().fromJson(new String(responseBody),type) ;

            if (newsInfoJson != null) {

                Message  msg = new Message();

                msg.obj = newsInfoJson;

                uiHandler.handleMessage(msg);

            }else{
                AppContext.showToast(R.string.parse_data_error);
            }
        }



        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            requestFailureHint(error);
            mErrorLayout.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
        }

    };






    private Handler uiHandler  = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            NewsInfoJson  newsInfoJson = (NewsInfoJson) msg.obj;
            handleNewsInfoBean(newsInfoJson);
        }
    };





    private void handleNewsInfoBean(NewsInfoJson newsInfoJson) {

        String code = newsInfoJson.getCode();
        if (ErrorCode.SUCCESS.getValue().equals(code)) {

            NewsInfo  newsInfo = newsInfoJson.getObjOne();

            mNewsTitle.setText(newsInfo.getTitle());

            mNewsDesc.setText("时间:"+newsInfo.getCreateTime()+"   浏览数:"+newsInfo.getViewsNum()+"  发布者:"+newsInfo.getCreatorName());
            mNewsContent.loadData(newsInfo.getContent(),"text/html; charset=utf-8", "utf-8");
           mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);

            if (newsInfo.isStoreFlag()){

                mCollectionBtn.setText("已收藏");
            }else{

                mCollectionBtn.setText("未收藏");
            }

            if (newsInfo.isStoreFlag()){

                mGiveBtn.setText("已点赞");


            }else{

                mGiveBtn.setText("未点赞");
            }


        }else if(ErrorCode.NOTPARAMETER.getValue().equals(code)){
            AppContext.showToast(R.string.news_faile_error);
            mErrorLayout.setErrorType(EmptyLayout.NODATA);


        }else if(ErrorCode.FAIL.getValue().equals(code)){

            AppContext.showToast(R.string.request_error_hint);
            mErrorLayout.setErrorType(EmptyLayout.NODATA);
        }else {

            AppContext.showToast(R.string.parse_data_error);
            mErrorLayout.setErrorType(EmptyLayout.NODATA);
        }
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_share:

                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    @OnClick({btn_give,R.id.btn_collection})
    public  void onBtnClick(View view){


        int vId = view.getId();


        switch (vId){


            case btn_give:
                tempStoreType = "2";
                break;

            case btn_collection:

                tempStoreType = "1";
                break;



        }
        if (!"".equals(tempStoreType)) {
            clickLoveOrCollection(tempStoreType);
        }
    }



    public   void clickLoveOrCollection(String storetype){

        if (null !=userInfo && userInfo.isLogin()) {

            try {

                SimpleApi.newsCollection(newsId, userInfo.getId(), storetype, loveCollectionHandler);

            }catch (Exception e){
                e.printStackTrace();
            }

        }else {

            UIHelper.showLoginActivity(NewsDetailActivity.this);
        }
    }


    private  AsyncHttpResponseHandler  loveCollectionHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onStart() {
            super.onStart();
            showWaitDialog(R.string.data_loading);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            String jsonData = new String(responseBody);

            Log.i("TAG","======jsonData======="+jsonData);

            Type type = new TypeToken<BaseResposeInfo>() {
            }.getType();

            GsonBuilder gsonBuilder = new GsonBuilder();

            BaseResposeInfo baseResposeInfo = gsonBuilder.create().fromJson(jsonData,type) ;

            if (baseResposeInfo != null) {
                handleResultMsgBean(baseResposeInfo);
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


    private void handleResultMsgBean(BaseResposeInfo  baseInfo){

        String code = baseInfo.getCode();
        String message = "";
        if (ErrorCode.STORED.getValue().equals(code)){

            message  = ErrorCode.STOREDMSG.getValue();


        }else if (ErrorCode.LOVED.getValue().equals(code)){

            message  = ErrorCode.LOVEDMSG.getValue();

        }else if(ErrorCode.SUCCESS.getValue().equals(code)){

            if ("1".equals(tempStoreType)) {

                message = "收藏成功！";

            }else {

                message = "点赞成功！";
            }
        }

        DialogHelp.getMessageDialog(NewsDetailActivity.this, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                loadNewsDetail();
            }
        }).create().show();
    }




}
