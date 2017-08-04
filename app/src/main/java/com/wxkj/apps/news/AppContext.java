package com.wxkj.apps.news;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.wxkj.apps.news.api.ApiHttpClient;
import com.wxkj.apps.news.base.BaseApplication;
import com.wxkj.apps.news.entity.UserInfo;
import com.wxkj.apps.news.utils.TLog;

import org.kymjs.kjframe.http.HttpConfig;
import org.kymjs.kjframe.utils.KJLoger;

import java.util.Properties;
import java.util.UUID;

/**
 * Created by taosong on 17/6/5.
 */

public class AppContext extends BaseApplication {

    public static final int PAGE_SIZE = 15;// 默认分页大小
    private static AppContext instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        init();
    }
    private void init() {
//        AppCrashHandler handler = AppCrashHandler.getInstance();
//        handler.init(this);

        // 初始化网络请求
        AsyncHttpClient client = new AsyncHttpClient();
        PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
        client.setCookieStore(myCookieStore);
        ApiHttpClient.setHttpClient(client);
        ApiHttpClient.setCookie(ApiHttpClient.getCookie(this));

        // Log控制器
        KJLoger.openDebutLog(BuildConfig.DEBUG);
        TLog.DEBUG = BuildConfig.DEBUG;

        // Bitmap缓存地址
        HttpConfig.CACHEPATH = "SimpleNews/ImageCache";
    }





    /**
     * 获得当前app运行的AppContext
     *
     * @return
     */
    public static AppContext getInstance() {
        return instance;
    }

    public boolean containsProperty(String key) {
        Properties props = getProperties();
        return props.containsKey(key);
    }

    public void setProperties(Properties ps) {
        AppConfig.getAppConfig(this).set(ps);
    }

    public Properties getProperties() {
        return AppConfig.getAppConfig(this).get();
    }

    public void setProperty(String key, String value) {
        AppConfig.getAppConfig(this).set(key, value);
    }

    /**
     * 获取cookie时传AppConfig.CONF_COOKIE
     *
     * @param key
     * @return
     */
    public String getProperty(String key) {
        return AppConfig.getAppConfig(this).get(key);
    }

    public void removeProperty(String... key) {
        AppConfig.getAppConfig(this).remove(key);
    }

    /**
     * 获取App唯一标识
     *
     * @return
     */
    public String getAppId() {
        String uniqueID = getProperty(AppConfig.CONF_APP_UNIQUEID);
        if (TextUtils.isEmpty(uniqueID)) {
            uniqueID = UUID.randomUUID().toString();
            setProperty(AppConfig.CONF_APP_UNIQUEID, uniqueID);
        }
        return uniqueID;
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }



    private UserInfo  userInfo;

    public UserInfo getLoginUser() {
        return userInfo;
    }

    public void saveUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

//    /**
//     * 保存登录信息
//     *
//     * @param user 用户信息
//     */
//    @SuppressWarnings("serial")
//    public void saveUserInfo(final UserInfo user) {
//        setProperties(new Properties() {
//            {
//                setProperty("user.id", user.getId());
//                setProperty("user.name", user.getUname());
//                setProperty("user.nickname", user.getNickName());
//                setProperty("user.pwd",
//                        CyptoUtils.encode("SimpleNews",user.getUpasswd()));
//                setProperty("user.login",String.valueOf(user.isLogin()));
//                setProperty("user.phone", user.getMobilePhone());
//                setProperty("user.mail", user.getMail());
//                setProperty("user.desc",user.getDescription());
//                setProperty("user.apptype",user.getApptype());
//                setProperty("user.createtime",user.getCreateTime());
//                setProperty("user.logintime",user.getLastLoginTime());
//                setProperty("user.updatetime",user.getLastUpdateTime());
//
//            }
//        });
//    }
//
//
//
//    /**
//     * 获得登录用户的信息
//     *
//     * @return
//     */
//    public UserInfo getLoginUser() {
//        UserInfo user = new UserInfo();
//
//        user.setId(getProperty("user.id"));
//        user.setUname(getProperty("user.name"));
//        user.setUpasswd(CyptoUtils.decode("SimpleNews", AppContext
//                .getInstance().getProperty("user.pwd")));
//
//        user.setNickName(getProperty("user.nickname"));
//        user.setLogin(Boolean.valueOf(getProperty("user.login")));
//
//        user.setMobilePhone(getProperty("user.phone"));
//
//        user.setApptype(getProperty("user.apptype"));
//        user.setCreateTime(getProperty("user.createtime"));
//
//        user.setDescription(getProperty("user.desc"));
//        user.setMail(getProperty("user.mail"));
//        user.setLastLoginTime(getProperty("user.logintime"));
//
//        user.setLastUpdateTime(getProperty("user.updatetime"));
//        return user;
//    }





}
