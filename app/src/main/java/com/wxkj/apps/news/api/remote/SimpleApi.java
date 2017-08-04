package com.wxkj.apps.news.api.remote;

import android.util.Base64;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wxkj.apps.news.AppConfig;
import com.wxkj.apps.news.api.ApiHttpClient;
import com.wxkj.apps.news.api.URLs;
import com.wxkj.apps.news.app.AppOperator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by taosong on 17/4/16.
 */

public class SimpleApi {


    public final static String TAG = SimpleApi.class.getName();

    // 将数据进行转码
    protected static String strToBase64(String str) {
        String result = "";
        try {

            result = Base64.encodeToString(str.getBytes(), Base64.NO_WRAP);

        } catch (Exception e) {


            return result;
        }
        return result;
    }

    // 将数据进行解码
    protected static byte[] base64ToStr(String str) {
        byte result[] = null;
        try {

            result = Base64.decode(str, Base64.NO_WRAP);

        } catch (Exception e) {


            return result;
        }
        return result;
    }

    /**
     * 获取JSON
     * <p/>
     * params "[键名]", "[键值]", ...
     *
     * @throws Exception
     */
    protected static String GetParams(Object... params) {

        String jsonResult = null;
        Map<String, Object> maps = new HashMap<String, Object>();
        try {

            for (int i = 0; i < params.length; i++) {

                maps.put(params[i].toString(), params[i + 1]);
                i++;
            }

            jsonResult = AppOperator.createGson().toJson(maps);
        } catch (Exception e) {
            e.printStackTrace();

        }
        Log.d("========GetParams=====", jsonResult);

        return jsonResult;
    }

    public static RequestParams postParams(String param){
        RequestParams  params = new RequestParams();

        String paramsJson  =  strToBase64(param);

        params.put(URLs.PARAMS, paramsJson);

        return  params;
    }


    /**
     * 用户登录
     * @param username
     * @param password
     * @param handler
     */
    public static void login(String username,String password, AsyncHttpResponseHandler handler){

       String paramStr =  GetParams("uname",username,"upass",password,"apptype", AppConfig.APP_CONFIG_TYPE);
       RequestParams httParams =   postParams(paramStr);

        String requestUrl = URLs.getUrlInstance()+URLs.GET_USER_LOGIN;

        ApiHttpClient.post(requestUrl,httParams,handler);
    }


    /**
     * 用户注册
     * @param username
     * @param password
     * @param handler
     */
    public static  void register(String username,String password, AsyncHttpResponseHandler handler){

        String paramStr =  GetParams("uname",username,"upass",password,"apptype", AppConfig.APP_CONFIG_TYPE);
        RequestParams httParams =   postParams(paramStr);

        String requestUrl = URLs.getUrlInstance()+URLs.GET_USER_REGISTE;

        ApiHttpClient.post(requestUrl,httParams,handler);


    }


    /**
     * 新闻列表
     * @param username
     * @param pageNum
     * @param pageSize
     * @param newType
     * @param title
     * @param handler
     */

    public static  void newsList(String username,int pageNum,int pageSize,String newType,String title,AsyncHttpResponseHandler handler){


        String paramStr =  GetParams("uname",username,"pageNum",pageNum,"pageSize",pageSize,"newType",newType,"title",title,"apptype", AppConfig.APP_CONFIG_TYPE);
        RequestParams httParams =   postParams(paramStr);

        String requestUrl = URLs.getUrlInstance()+URLs.GET_NEWS_QUERY;

        ApiHttpClient.post(requestUrl,httParams,handler);
    }


    /**
     * 新闻详情
     * @param username
     * @param id
     * @param userid
     * @param handler
     */

    public static  void newsDetail(String username,String id,String userid,AsyncHttpResponseHandler  handler){


        String paramStr =  GetParams("uname",username,"id",id,"userid",userid);
        RequestParams httParams =   postParams(paramStr);

        String requestUrl = URLs.getUrlInstance()+URLs.GET_NEWS_DETAIL;

        ApiHttpClient.post(requestUrl,httParams,handler);


    }


    /**
     * 新闻收藏点赞
     * @param id
     * @param userid
     * @param storetype
     * @param handler
     */

    public static void newsCollection(String id,String userid,String storetype,AsyncHttpResponseHandler handler){


        String paramStr =  GetParams("userid",userid,"id",id,"storetype",storetype);
        RequestParams httParams =   postParams(paramStr);

        String requestUrl = URLs.getUrlInstance()+URLs.GET_NEWS_COLLECTION;

        ApiHttpClient.post(requestUrl,httParams,handler);

    }


    /**
     * 获取收藏列表
     * @param title
     * @param userid
     * @param storetype
     * @param newType
     * @param pageNum
     * @param pageSize
     * @param handler
     */

    public static void newsClickLoveList(String title,String userid,String storetype,String newType,int pageNum,int pageSize,AsyncHttpResponseHandler handler){



        String paramStr =  GetParams("userid",userid,"title",title,"storetype",storetype,"newType",newType,"pageNum",pageNum,"pageSize",pageSize);
        RequestParams httParams =   postParams(paramStr);

        String requestUrl = URLs.getUrlInstance()+URLs.GET_NEWS_CLICKLOVE;

        ApiHttpClient.post(requestUrl,httParams,handler);

    }





    /**
     * 检查版本更新
     * @param handler
     */
    public static void checkUpdate(AsyncHttpResponseHandler  handler){




    }


    /**
     * （8）上传日志分析
     * @param appLog
     * @param loginName
     * @param loginPwd
     * @param  type 0 表示bug, 1 表示建议
     * @param httpResponseHandler
     */
    public static void loadAppLog(int type,String appLog,String loginName,String loginPwd,AsyncHttpResponseHandler httpResponseHandler){


    }



}
