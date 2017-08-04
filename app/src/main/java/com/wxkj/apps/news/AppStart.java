package com.wxkj.apps.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.wxkj.apps.news.ui.MainActivity;


/**
 * 应用启动界面
 */
public class AppStart extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 防止第三方跳转时出现双实例
        Activity aty = AppManager.getActivity(MainActivity.class);
        if (aty != null && !aty.isFinishing()) {
            finish();
        }

        setContentView(R.layout.app_start);
        findViewById(R.id.app_start_view).postDelayed(new Runnable() {
            @Override
            public void run() {
                redirectTo();
            }
        }, 800);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * 跳转到...
     */
    private void redirectTo() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
