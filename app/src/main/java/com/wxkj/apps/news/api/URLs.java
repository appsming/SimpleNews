package com.wxkj.apps.news.api;


/**
 * Created by cxiosapp on 16/2/15.
 */
public class URLs {


    private String API_VERSION = "/app/news";// API版本
    private String HTTPS = "http://";
    public static  String HOST = "cmw.butterfly.mopaasapp.com";


    public static  String PARAMS = "reqmsg";


    private static URLs urlInstance;

    public static String getUrlInstance() {
        if (null == urlInstance) {
            urlInstance = new URLs();
        }
        return urlInstance.HTTPS + HOST
                + urlInstance.API_VERSION;
    }



    /*****************************接口方法*********************************/

    public static final String GET_USER_LOGIN = "/login";

    public static final String GET_USER_REGISTE = "/registe";

    public static final String GET_NEWS_QUERY = "/queryNews";

    public static final String GET_NEWS_DETAIL = "/queryNewDetail";

    public static final String GET_NEWS_COLLECTION = "/storeLoveNew";

    public static final String GET_NEWS_CLICKLOVE = "/queryStoreOrLoveNews";



}
