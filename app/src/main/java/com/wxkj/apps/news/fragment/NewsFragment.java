package com.wxkj.apps.news.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.reflect.TypeToken;
import com.wxkj.apps.news.AppContext;
import com.wxkj.apps.news.adapter.BaseListAdapter;
import com.wxkj.apps.news.adapter.NewsListAdapter;
import com.wxkj.apps.news.api.remote.SimpleApi;
import com.wxkj.apps.news.base.BaseGeneralListFragment;
import com.wxkj.apps.news.entity.NewsInfo;
import com.wxkj.apps.news.entity.PageBean;
import com.wxkj.apps.news.entity.UserInfo;
import com.wxkj.apps.news.ui.NewsDetailActivity;

import java.lang.reflect.Type;

import static com.wxkj.apps.news.fragment.NewsViewPagerFragment.CATALOG_TJ;

/**
 * Created by taosong on 17/4/16.
 */

public class NewsFragment extends BaseGeneralListFragment<NewsInfo> {

    public String requestCategory = "";//请求类型
    public static final String CATEGORY_DEFAULE =CATALOG_TJ ;


    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        requestCategory = bundle.getString(NewsViewPagerFragment.BUNDLE_KEY_REQUEST_CATALOG, CATEGORY_DEFAULE);

    }

    /**
     * fragment被销毁的时候重新调用，初始化保存的数据
     *
     * @param bundle onSaveInstanceState
     */
    @Override
    protected void onRestartInstance(Bundle bundle) {
        super.onRestartInstance(bundle);
        requestCategory = bundle.getString(NewsViewPagerFragment.BUNDLE_KEY_REQUEST_CATALOG, CATEGORY_DEFAULE);

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

        String userName = "";

        if (null !=userInfo) {
            userName  = userInfo.getUname();
        }

        SimpleApi.newsList(userName, mCurrentPage, AppContext.PAGE_SIZE, requestCategory, "", mHandler);




    }



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
        outState.putString(NewsViewPagerFragment.BUNDLE_KEY_REQUEST_CATALOG, requestCategory);
        super.onSaveInstanceState(outState);
    }





}
