package com.wxkj.apps.news.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.wxkj.apps.news.interf.ICallbackResult;
import com.wxkj.apps.news.ui.LoginActivity;
import com.wxkj.apps.news.ui.MainActivity;
import com.wxkj.apps.news.ui.RegisterActivity;


/**
 * Created by taosong on 17/3/9.
 */

public class UIHelper {


    /**（1）
     * 显示登录界面
     *
     * @param context
     */
    public static void showLoginActivity(Activity context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    /**（2）
     * 显示注册
     *
     * @param context
     */
    public static void showRegisterActivity(Activity context,int requestCode) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivityForResult(intent,requestCode);
    }



//    /**
//     * (3) APP下载升级功能
//     * @param context
//     * @param downurl
//     * @param tilte
//     */
//
//    public static void openDownLoadService(Context context, String downurl,
//                                           String tilte) {
//        final ICallbackResult callback = new ICallbackResult() {
//
//            @Override
//            public void OnBackResult(Object s) {
//            }
//        };
//        ServiceConnection conn = new ServiceConnection() {
//
//            @Override
//            public void onServiceDisconnected(ComponentName name) {
//            }
//
//            @Override
//            public void onServiceConnected(ComponentName name, IBinder service) {
//                DownloadService.DownloadBinder binder = (DownloadService.DownloadBinder) service;
//                binder.addCallback(callback);
//                binder.start();
//
//            }
//        };
//        Intent intent = new Intent(context, DownloadService.class);
//        intent.putExtra(DownloadService.BUNDLE_KEY_DOWNLOAD_URL, downurl);
//        intent.putExtra(DownloadService.BUNDLE_KEY_TITLE, tilte);
//        context.startService(intent);
//        context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
//    }
//
//
//    public static void showSimpleBack(Context context, SimpleBackPage page) {
//        Intent intent = new Intent(context, SimpleBackActivity.class);
//        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
//        context.startActivity(intent);
//    }
//
//    public static void showSimpleBack(Context context, SimpleBackPage page,
//                                      Bundle args) {
//        Intent intent = new Intent(context, SimpleBackActivity.class);
//        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_ARGS, args);
//        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
//        context.startActivity(intent);
//    }


    /**
     * 发送App异常崩溃报告
     *
     * @param context
     */
    public static void sendAppCrashReport(final Context context) {

        DialogHelp.getConfirmDialog(context, "程序发生异常", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 退出
                System.exit(-1);
            }
        }).show();
    }



    public static  void showMainActivity(Context context){

        Intent  intent = new Intent(context, MainActivity.class);

        context.startActivity(intent);
    }
}
