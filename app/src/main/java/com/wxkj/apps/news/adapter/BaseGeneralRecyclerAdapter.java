package com.wxkj.apps.news.adapter;

import android.content.Context;

import com.bumptech.glide.RequestManager;

import java.util.Date;

/**
 * Created by taosong on 17/6/6.
 */

public abstract class BaseGeneralRecyclerAdapter<T> extends BaseRecyclerAdapter<T> {
    protected Callback mCallBack;

    public BaseGeneralRecyclerAdapter(Callback callback, int mode) {
        super(callback.getContext(), mode);
        mCallBack = callback;
        setState(STATE_LOADING, true);
    }

    public interface Callback {
        RequestManager getImgLoader();

        Context getContext();

        Date getSystemTime();
    }

}
