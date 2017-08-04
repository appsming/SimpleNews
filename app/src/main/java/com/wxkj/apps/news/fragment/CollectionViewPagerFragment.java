package com.wxkj.apps.news.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.wxkj.apps.news.R;
import com.wxkj.apps.news.base.BaseGeneralListFragment;
import com.wxkj.apps.news.base.BaseViewPagerFragment;
import com.wxkj.apps.news.interf.OnTabReselectListener;

/**
 * Created by taosong on 17/6/5.
 */

public class CollectionViewPagerFragment extends BaseViewPagerFragment implements OnTabReselectListener {

    public static final String BUNDLE_KEY_REQUEST_CATALOG = "BUNDLE_KEY_REQUEST_CATALOG";
    public static final String CATALOG_COLLECTION = "01";
    public static final String CATALOG_LOVE = "02";



    /**
     * @param requestCategory 请求类型

     * @return Bundle
     */
    private Bundle getBundle(String requestCategory) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_REQUEST_CATALOG, requestCategory);
        return bundle;
    }

    @Override
    protected void initData() {
        super.initData();
        mTitleBar.setVisibility(View.GONE);
    }

    @Override
    public void onTabReselect() {

        if (mBaseViewPager != null) {
            BaseViewPagerAdapter pagerAdapter = (BaseViewPagerAdapter) mBaseViewPager.getAdapter();
            Fragment fragment = pagerAdapter.getCurFragment();
            if (fragment != null && fragment instanceof BaseGeneralListFragment) {
                ((BaseGeneralListFragment) fragment).onTabReselect();
            }
        }
    }

    @Override
    protected int getTitleRes() {
        return R.string.more_collection;
    }

    @Override
    protected PagerInfo[] getPagers() {

        String[] titles = getResources().getStringArray(R.array.collection_viewpage_arrays);
        PagerInfo[] infoList = new PagerInfo[titles.length];
        infoList[0] = new PagerInfo(titles[0], CollectionNewsFragment.class,
                getBundle(CATALOG_COLLECTION));
        infoList[1] = new PagerInfo(titles[1], CollectionNewsFragment.class,
                getBundle(CATALOG_LOVE));

        return infoList;
    }


    @Override
    protected int getIconRes() {
        return R.mipmap.btn_search_normal;
    }

}
