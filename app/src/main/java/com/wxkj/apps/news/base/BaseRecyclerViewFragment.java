package com.wxkj.apps.news.base;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.loopj.android.http.TextHttpResponseHandler;
import com.wxkj.apps.news.R;
import com.wxkj.apps.news.adapter.BaseGeneralRecyclerAdapter;
import com.wxkj.apps.news.adapter.BaseRecyclerAdapter;
import com.wxkj.apps.news.app.AppOperator;
import com.wxkj.apps.news.entity.PageBean;
import com.wxkj.apps.news.widget.EmptyLayout;
import com.wxkj.apps.news.widget.RecyclerRefreshLayout;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

/**
 * 基本列表类，重写getLayoutId()自定义界面
 * Created by huanghaibin_dev
 * on 2016/4/12.
 */
@SuppressWarnings("unused")
public abstract class BaseRecyclerViewFragment<T> extends BaseTitleFragment implements
        RecyclerRefreshLayout.SuperRefreshLayoutListener,
        BaseRecyclerAdapter.OnItemClickListener,
        View.OnClickListener,
        BaseGeneralRecyclerAdapter.Callback {
    protected BaseRecyclerAdapter<T> mAdapter;
    protected RecyclerView mRecyclerView;
    protected RecyclerRefreshLayout mRefreshLayout;
    protected boolean mIsRefresh;
    protected TextHttpResponseHandler mHandler;
    protected PageBean<T> mBean;
    protected String CACHE_NAME = getClass().getName();
    protected EmptyLayout mErrorLayout;
    protected  int mCurrentPage = 1;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_base_recycler_view;
    }

    @Override
    protected void initWidget(View root) {
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        mRefreshLayout = (RecyclerRefreshLayout) root.findViewById(R.id.refreshLayout);
        mErrorLayout = (EmptyLayout) root.findViewById(R.id.error_layout);
    }

    @Override
    public void initData() {
        mBean = new PageBean<>();
        mAdapter = getRecyclerAdapter();
        mAdapter.setState(BaseRecyclerAdapter.STATE_HIDE, false);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mErrorLayout.setOnLayoutClickListener(this);
        mRefreshLayout.setSuperRefreshLayoutListener(this);
        mAdapter.setState(BaseRecyclerAdapter.STATE_HIDE, false);
        mRecyclerView.setLayoutManager(getLayoutManager());
        mRefreshLayout.setColorSchemeResources(
                R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                R.color.swiperefresh_color3, R.color.swiperefresh_color4);


        mHandler = new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                onRequestError(statusCode);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    PageBean<T> resultBean = AppOperator.createGson().fromJson(responseString, getType());
                    if (resultBean != null  && resultBean.getListOne() != null) {
                        int size = resultBean.getListOne().size();
                        setListData(resultBean);
                        onRequestSuccess(resultBean.getCode());
                    } else {
                        mAdapter.setState(BaseRecyclerAdapter.STATE_NO_MORE, true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    onFailure(statusCode, headers, responseString, e);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                onRequestFinish();
            }
        };

        boolean isNeedEmptyView = isNeedEmptyView();
        if (isNeedEmptyView) {
            mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
            mRefreshLayout.setVisibility(View.GONE);
            AppOperator.runOnThread(new Runnable() {
                @SuppressWarnings("unchecked")
                @Override
                public void run() {

                    //if is the first loading
                    if (mBean == null) {
                        mBean = new PageBean<>();
                        mBean.setListOne(new ArrayList<T>());
                        onRefreshing();
                    } else {
                        mRoot.post(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.addAll(mBean.getListOne());
                                mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                                mRefreshLayout.setVisibility(View.VISIBLE);
                                mRefreshLayout.setRefreshing(true);
                                onRefreshing();
                            }
                        });
                    }
                }
            });
        } else {
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            mRefreshLayout.setVisibility(View.VISIBLE);
            mRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(true);
                    onRefreshing();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        onRefreshing();
    }


    @Override
    public void onItemClick(int position, long itemId) {

    }
    @Override
    public void onRefreshing() {
        mIsRefresh = true;
        mCurrentPage = 1;
        requestData();
    }

    @Override
    public void onLoadMore() {
        mCurrentPage++;
        requestData();
    }

    protected void requestData() {
    }

    protected void onRequestStart() {

    }

    protected void onRequestSuccess(String code) {

    }

    protected void onRequestFinish() {
        onComplete();
    }

    protected void onRequestError(int code) {
        if (mAdapter.getItems().size() == 0) {
            if (isNeedEmptyView()) mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
            mAdapter.setState(BaseRecyclerAdapter.STATE_LOAD_ERROR, true);
        }
    }

    protected void onComplete() {
        mRefreshLayout.onComplete();
        mIsRefresh = false;
    }

    protected void setListData(PageBean<T> resultBean) {
        //is refresh
        mBean.setNextPageToken((resultBean == null ? null : resultBean.getNextPageToken()));
        if (mIsRefresh) {
            //cache the time
            mBean.setListOne(resultBean.getListOne());
            mAdapter.clear();
            mAdapter.addAll(mBean.getListOne());
            mBean.setPrevPageToken((resultBean == null ? null : resultBean.getPrevPageToken()));
            mRefreshLayout.setCanLoadMore(true);

        } else {
            mAdapter.addAll(resultBean.getListOne());
        }

        mAdapter.setState(resultBean.getListOne() == null || resultBean.getListOne().size() < 20 ? BaseRecyclerAdapter.STATE_NO_MORE : BaseRecyclerAdapter.STATE_LOADING, true);

        if (mAdapter.getItems().size() > 0) {
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            mRefreshLayout.setVisibility(View.VISIBLE);
        } else {
            mErrorLayout.setErrorType(isNeedEmptyView() ? EmptyLayout.NODATA : EmptyLayout.HIDE_LAYOUT);
        }
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    protected abstract BaseRecyclerAdapter<T> getRecyclerAdapter();

    protected abstract Type getType();

    @Override
    public Date getSystemTime() {
        return new Date();
    }



    /**
     * 需要空的View
     *
     * @return isNeedEmptyView
     */
    protected boolean isNeedEmptyView() {
        return true;
    }
}
