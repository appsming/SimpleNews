package com.wxkj.apps.news.base;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.TextHttpResponseHandler;
import com.wxkj.apps.news.AppContext;
import com.wxkj.apps.news.R;
import com.wxkj.apps.news.adapter.BaseListAdapter;
import com.wxkj.apps.news.app.AppOperator;
import com.wxkj.apps.news.entity.ErrorCode;
import com.wxkj.apps.news.entity.PageBean;
import com.wxkj.apps.news.utils.StringUtils;
import com.wxkj.apps.news.widget.EmptyLayout;
import com.wxkj.apps.news.widget.SuperRefreshLayout;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

/**
 * Created by taosong on 17/6/5.
 */

public abstract class BaseListFragment<T> extends BaseFragment implements
        SuperRefreshLayout.SuperRefreshLayoutListener,
        AdapterView.OnItemClickListener, BaseListAdapter.Callback,
        View.OnClickListener {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_LOADING = 1;
    public static final int TYPE_NO_MORE = 2;
    public static final int TYPE_ERROR = 3;
    public static final int TYPE_NET_ERROR = 4;
    protected String CACHE_NAME = getClass().getName();
    protected ListView mListView;
    protected SuperRefreshLayout mRefreshLayout;
    protected EmptyLayout mErrorLayout;
    protected BaseListAdapter<T> mAdapter;
    protected boolean mIsRefresh;
    protected TextHttpResponseHandler mHandler;
    protected PageBean<T> mBean;
    private View mFooterView;
    private ProgressBar mFooterProgressBar;
    private TextView mFooterText;
    private String mTime;

    protected int mCurrentPage = 1;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_base_list;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mListView = (ListView) root.findViewById(R.id.listView);
        mRefreshLayout = (SuperRefreshLayout) root.findViewById(R.id.superRefreshLayout);
        mRefreshLayout.setColorSchemeResources(
                R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                R.color.swiperefresh_color3, R.color.swiperefresh_color4);
        mErrorLayout = (EmptyLayout) root.findViewById(R.id.error_layout);
        mRefreshLayout.setSuperRefreshLayoutListener(this);
        mFooterView = LayoutInflater.from(getContext()).inflate(R.layout.layout_list_view_footer, null);
        mFooterText = (TextView) mFooterView.findViewById(R.id.tv_footer);
        mFooterProgressBar = (ProgressBar) mFooterView.findViewById(R.id.pb_footer);
        mListView.setOnItemClickListener(this);

        mErrorLayout.setOnLayoutClickListener(this);
        if (isNeedFooter())
            mListView.addFooterView(mFooterView);
    }


    @Override
    protected void initData() {
        super.initData();
        //when open this fragment,read the obj

        mAdapter = getListAdapter();
        mListView.setAdapter(mAdapter);


        mHandler = new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                onRequestError(statusCode);
                onRequestFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {

                    PageBean<T> resultBean = AppOperator.createGson().fromJson(responseString, getType());
                    if (resultBean != null && resultBean.getCode().equals(ErrorCode.SUCCESS.getValue()) && resultBean.getListOne() != null) {
                        onRequestSuccess(resultBean.getCode());
                        setListData(resultBean);
                    } else {
                        setFooterType(TYPE_NO_MORE);
                        //mRefreshLayout.setNoMoreData();
                    }


                    onRequestFinish();
                } catch (Exception e) {
                    e.printStackTrace();
                    onFailure(statusCode, headers, responseString, e);
                }

            }
        };


        AppOperator.runOnThread(new Runnable() {
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
                            mAdapter.addItem(mBean.getListOne());
                            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                            mRefreshLayout.setVisibility(View.VISIBLE);
                            onRefreshing();
                        }
                    });
                }
            }
        });
    }




    @Override
    public void onClick(View v) {
        mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        onRefreshing();
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

    /**
     * request network data
     */
    protected void requestData() {
        onRequestStart();
        setFooterType(TYPE_LOADING);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    protected void onRequestStart() {

    }

    protected void onRequestSuccess(String code) {

    }


    protected void onRequestError(int code) {
        setFooterType(TYPE_NET_ERROR);
        if (mAdapter.getDatas().size() == 0)
            mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
    }

    protected void onRequestFinish() {
        onComplete();
    }

    protected void onComplete() {
        mRefreshLayout.onLoadComplete();
        mIsRefresh = false;
    }



    @Override
    public Date getSystemTime() {
        return new Date();
    }

    protected abstract BaseListAdapter<T> getListAdapter();

    protected abstract Type getType();

    protected boolean isNeedFooter() {
        return true;
    }


    protected void setFooterType(int type) {
        try {
            switch (type) {
                case TYPE_NORMAL:
                case TYPE_LOADING:
                    mFooterText.setText(getResources().getString(R.string.footer_type_loading));
                    mFooterProgressBar.setVisibility(View.VISIBLE);
                    break;
                case TYPE_NET_ERROR:
                    mFooterText.setText(getResources().getString(R.string.footer_type_net_error));
                    mFooterProgressBar.setVisibility(View.GONE);
                    break;
                case TYPE_ERROR:
                    mFooterText.setText(getResources().getString(R.string.footer_type_error));
                    mFooterProgressBar.setVisibility(View.GONE);
                    break;
                case TYPE_NO_MORE:
                    mFooterText.setText(getResources().getString(R.string.footer_type_not_more));
                    mFooterProgressBar.setVisibility(View.GONE);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    protected void setListData(PageBean<T> resultBean) {

        mBean.setNextPageToken(resultBean.getNextPageToken());
        if (mIsRefresh) {
            //cache the time
            mTime = StringUtils.getCurrentTimeStr();

            mBean.setListOne(resultBean.getListOne());
            mAdapter.clear();
            mAdapter.addItem(mBean.getListOne());
            mBean.setPrevPageToken(resultBean.getPrevPageToken());
            mRefreshLayout.setCanLoadMore();
        } else {
            mAdapter.addItem(resultBean.getListOne());
        }
        if (resultBean.getListOne().size() < AppContext.PAGE_SIZE) {
            setFooterType(TYPE_NO_MORE);
            //mRefreshLayout.setNoMoreData();
        }
        if (mAdapter.getDatas().size() > 0) {
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            mRefreshLayout.setVisibility(View.VISIBLE);
        } else {
            mErrorLayout.setErrorType(EmptyLayout.NODATA);
        }

    }
}
