package com.wxkj.apps.news.ui;

import android.content.Context;
import android.content.Intent;

import com.wxkj.apps.news.R;
import com.wxkj.apps.news.base.BaseBackActivity;
import com.wxkj.apps.news.fragment.CollectionViewPagerFragment;

/**
 * Created by taosong on 17/6/10.
 */

public class MyCollectionActivity  extends BaseBackActivity {

    CollectionViewPagerFragment  collectionViewPagerFragment;


    public static void show(Context context) {
        Intent intent = new Intent(context, MyCollectionActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected int getContentView() {
        return R.layout.app_collection;
    }


    @Override
    protected void initWidget() {
        super.initWidget();

        if (null ==collectionViewPagerFragment){
            collectionViewPagerFragment  = new CollectionViewPagerFragment();
        }


        replaceFragment(R.id.container,collectionViewPagerFragment);
    }
}
