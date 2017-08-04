package com.wxkj.apps.news.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.wxkj.apps.news.R;
import com.wxkj.apps.news.base.BaseGeneralListFragment;
import com.wxkj.apps.news.base.BaseViewPagerFragment;
import com.wxkj.apps.news.interf.OnTabReselectListener;
import com.wxkj.apps.news.ui.SearchActivity;

/**
 * Created by taosong on 17/6/5.
 */

public class NewsViewPagerFragment extends BaseViewPagerFragment implements OnTabReselectListener {

    public static final String BUNDLE_KEY_REQUEST_CATALOG = "BUNDLE_KEY_REQUEST_CATALOG";
    public static final String CATALOG_TJ = "07";
    public static final String CATALOG_KJ = "01";
    public static final String CATALOG_JJ = "02";
    public static final String CATALOG_SW = "03";
    public static final String CATALOG_WX = "04";
    public static final String CATALOG_JS = "05";
    public static final String CATALOG_LS = "06";


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
        return R.string.main_tab_name_home;
    }

    @Override
    protected PagerInfo[] getPagers() {

        String[] titles = getResources().getStringArray(R.array.news_viewpage_arrays);
        PagerInfo[] infoList = new PagerInfo[titles.length];
        infoList[0] = new PagerInfo(titles[0], NewsFragment.class,
                getBundle(CATALOG_TJ));
        infoList[1] = new PagerInfo(titles[1], NewsFragment.class,
                getBundle(CATALOG_KJ));
        infoList[2] = new PagerInfo(titles[2], NewsFragment.class,
                getBundle(CATALOG_JJ));

        infoList[3] = new PagerInfo(titles[3], NewsFragment.class,
                getBundle(CATALOG_SW));

        infoList[4] = new PagerInfo(titles[4], NewsFragment.class,
                getBundle(CATALOG_WX));

        infoList[5] = new PagerInfo(titles[5], NewsFragment.class,
                getBundle(CATALOG_JS));

        infoList[6] = new PagerInfo(titles[6], NewsFragment.class,
                getBundle(CATALOG_LS));

        return infoList;
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

}
