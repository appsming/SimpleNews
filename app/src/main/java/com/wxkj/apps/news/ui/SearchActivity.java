package com.wxkj.apps.news.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;

import com.wxkj.apps.news.R;
import com.wxkj.apps.news.base.BaseActivity;
import com.wxkj.apps.news.fragment.NewsViewPagerFragment;
import com.wxkj.apps.news.fragment.SearchListFragment;

import org.kymjs.kjframe.utils.StringUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by taosong on 17/4/16.
 */

public class SearchActivity   extends BaseActivity {

    @Bind(R.id.view_searcher)
    SearchView mSearchView;


    private SearchListFragment  searchListFragment = null;


    @Override
    protected int getContentView() {
        return R.layout.app_search;
    }


    public static void show(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void initWidget() {
        super.initWidget();


       mSearchView.setQueryHint(getString(R.string.search_news_hit));
//        TextView textView = (TextView) mSearchView
//                .findViewById(R.id.search_src_text);
//        textView.setTextColor(Color.BLACK);
//        textView.setTextSize(R.dimen.size_12);

        mSearchView
                .setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                    @Override
                    public boolean onQueryTextSubmit(String arg0) {

                        searchContent(arg0);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String arg0) {

                        return false;
                    }
                });

        if (null ==searchListFragment){
            searchListFragment = new SearchListFragment();
            Bundle  bundle = new Bundle();
            bundle.putString(NewsViewPagerFragment.BUNDLE_KEY_REQUEST_CATALOG, "");
            searchListFragment.setArguments(bundle);
        }
        replaceFragment(R.id.search_fragment,searchListFragment);

    }


    public  void searchContent(String searchStr) {

        if (searchStr == null || StringUtils.isEmpty(searchStr)) {
            return;
        }
         searchListFragment.loadNews(searchStr);
    }




    @OnClick(R.id.tv_cancel)
    void onClickCancel() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
