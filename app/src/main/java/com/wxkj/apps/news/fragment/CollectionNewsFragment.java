package com.wxkj.apps.news.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.reflect.TypeToken;
import com.wxkj.apps.news.AppContext;
import com.wxkj.apps.news.R;
import com.wxkj.apps.news.adapter.BaseListAdapter;
import com.wxkj.apps.news.adapter.NewsListAdapter;
import com.wxkj.apps.news.api.remote.SimpleApi;
import com.wxkj.apps.news.base.BaseGeneralListFragment;
import com.wxkj.apps.news.entity.NewsInfo;
import com.wxkj.apps.news.entity.PageBean;
import com.wxkj.apps.news.entity.UserInfo;
import com.wxkj.apps.news.ui.NewsDetailActivity;
import com.wxkj.apps.news.utils.DialogHelp;
import com.wxkj.apps.news.utils.UIHelper;

import java.lang.reflect.Type;

import static com.wxkj.apps.news.fragment.NewsViewPagerFragment.CATALOG_TJ;

/**
 * Created by taosong on 17/4/16.
 */

public class CollectionNewsFragment extends BaseGeneralListFragment<NewsInfo> {

    public String requestCategory = "";//请求类型
    public static final String CATEGORY_DEFAULE =CATALOG_TJ ;


    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        requestCategory = bundle.getString(CollectionViewPagerFragment.BUNDLE_KEY_REQUEST_CATALOG, CATEGORY_DEFAULE);

    }

    /**
     * fragment被销毁的时候重新调用，初始化保存的数据
     *
     * @param bundle onSaveInstanceState
     */
    @Override
    protected void onRestartInstance(Bundle bundle) {
        super.onRestartInstance(bundle);
        requestCategory = bundle.getString(CollectionViewPagerFragment.BUNDLE_KEY_REQUEST_CATALOG, CATEGORY_DEFAULE);

    }

    @Override
    protected void initData() {
        super.initData();



    }


    @Override
    protected void requestData() {
        super.requestData();
        loadNews();

    }
    public  void loadNews(){


        UserInfo userInfo =  AppContext.getInstance().getLoginUser();

        String title = "";

        String userId = "";

        if (null !=userInfo && userInfo.isLogin()) {


            userId = userInfo.getId();

            SimpleApi.newsClickLoveList(title,userId,requestCategory,"",mCurrentPage,AppContext.PAGE_SIZE,mHandler);

        }else {

            handler.sendEmptyMessage(0);

        }





    }

    private Handler handler  = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            DialogHelp.getConfirmDialog(getActivity(), getString(R.string.unlogin_tip), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    UIHelper.showLoginActivity(getActivity());

                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();

        }
    };



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NewsDetailActivity.show(getContext(), mAdapter.getItem(position).getId()+"");
    }





    @Override
    protected BaseListAdapter getListAdapter() {
        return new NewsListAdapter(this);
    }

    @Override
    protected Type getType() {
        return new TypeToken<PageBean<NewsInfo>>() {
        }.getType();
    }




    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(CollectionViewPagerFragment.BUNDLE_KEY_REQUEST_CATALOG, requestCategory);
        super.onSaveInstanceState(outState);
    }





}
