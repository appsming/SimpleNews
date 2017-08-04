package com.wxkj.apps.news.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.wxkj.apps.news.AppConfig;
import com.wxkj.apps.news.R;
import com.wxkj.apps.news.base.BaseActivity;
import com.wxkj.apps.news.base.BaseApplication;
import com.wxkj.apps.news.fragment.NavFragment;
import com.wxkj.apps.news.interf.OnTabReselectListener;
import com.wxkj.apps.news.widget.NavigationButton;

import butterknife.Bind;

public class MainActivity extends BaseActivity implements NavFragment.OnNavigationReselectListener {
    public static final String ACTION_NOTICE = "ACTION_NOTICE";
    private long mBackPressedTime;

    @Bind(R.id.activity_main_ui)
    FrameLayout mMainUi;

    private NavFragment mNavBar;

    @Override
    protected int getContentView() {
        return R.layout.app_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        FragmentManager manager = getSupportFragmentManager();
        mNavBar = ((NavFragment) manager.findFragmentById(R.id.fag_nav));
        mNavBar.setup(this, manager, R.id.main_container, this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doNewIntent(getIntent(), true);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        doNewIntent(intent, false);
    }

    private void doNewIntent(Intent intent, boolean isCreate) {
        if (intent == null || intent.getAction() == null)
            return;
        String action = intent.getAction();
        Log.e("TAG", "onNewIntent action:" + action + " isCreate:" + isCreate);
        if (action.equals(ACTION_NOTICE)) {
            NavFragment bar = mNavBar;
            if (bar != null) {
                bar.select(3);
            }
        }
    }

    @Override
    public void onReselect(NavigationButton navigationButton) {
        Fragment fragment = navigationButton.getFragment();
        if (fragment != null
                && fragment instanceof OnTabReselectListener) {
            OnTabReselectListener listener = (OnTabReselectListener) fragment;
            listener.onTabReselect();
        }
    }

    @Override
    protected void initData() {
        super.initData();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        boolean isDoubleClick = BaseApplication.get(AppConfig.KEY_DOUBLE_CLICK_EXIT, true);
        if (isDoubleClick) {
            long curTime = SystemClock.uptimeMillis();
            if ((curTime - mBackPressedTime) < (3 * 1000)) {
                finish();
            } else {
                mBackPressedTime = curTime;
                Toast.makeText(this, R.string.tip_double_click_exit, Toast.LENGTH_LONG).show();
            }
        } else {
            finish();
        }
    }
}
