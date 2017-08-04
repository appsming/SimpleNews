package com.wxkj.apps.news.fragment;

import android.util.Log;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.wxkj.apps.news.AppContext;
import com.wxkj.apps.news.R;
import com.wxkj.apps.news.adapter.BaseRecyclerAdapter;
import com.wxkj.apps.news.adapter.DiscoverListAdapter;
import com.wxkj.apps.news.api.remote.SimpleApi;
import com.wxkj.apps.news.base.BaseRecyclerViewFragment;
import com.wxkj.apps.news.entity.NewsInfo;
import com.wxkj.apps.news.entity.PageBean;
import com.wxkj.apps.news.entity.UserInfo;
import com.wxkj.apps.news.interf.OnTabReselectListener;
import com.wxkj.apps.news.ui.SearchActivity;

import java.lang.reflect.Type;

/**
 * Created by taosong on 17/4/16.
 */

public class DiscoverFragment extends BaseRecyclerViewFragment<NewsInfo> implements OnTabReselectListener {



    @Override
    protected int getTitleRes() {
        return R.string.main_tab_name_discover;
    }

    @Override
    protected int getIconRes() {
        return R.mipmap.btn_search_normal;
    }

    @Override
    protected View.OnClickListener getIconClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.show(getContext());
            }
        };
    }


    @Override
    public void onTabReselect() {
        loadNews();
    }

    @Override
    protected BaseRecyclerAdapter<NewsInfo> getRecyclerAdapter() {
        return new DiscoverListAdapter(getContext(), BaseRecyclerAdapter.ONLY_FOOTER);
    }


    @Override
    protected void requestData() {
        loadNews();

    }


    @Override
    protected Type getType() {
        return new TypeToken<PageBean<NewsInfo>>() {
        }.getType();
    }




    public  void loadNews(){

        UserInfo userInfo =  AppContext.getInstance().getLoginUser();

        String userName = "";

        if (null !=userInfo) {
            userName  = userInfo.getUname();
        }

        SimpleApi.newsList(userName, mCurrentPage, AppContext.PAGE_SIZE, "07", "", mHandler);


    }




    @Override
    public void onItemClick(int position, long itemId) {

        Log.i("TAG","======onItemClick=========");
    }
}
