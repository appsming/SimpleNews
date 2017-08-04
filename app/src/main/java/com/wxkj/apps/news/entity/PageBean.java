package com.wxkj.apps.news.entity;

import java.util.List;

/**
 * Created by taosong on 17/6/5.
 */

public class PageBean<T> extends BaseResposeInfo {
    private List<T> listOne;
    private String nextPageToken;
    private String prevPageToken;


    public List<T> getListOne() {

//        Set set = new HashSet();
//        List newList = new ArrayList();
//        for (Iterator iter = listOne.iterator(); iter.hasNext();) {
//            Object element = iter.next();
//            if (set.add(element))
//                newList.add(element);
//        }
        return listOne;
    }

    public void setListOne(List<T> listOne) {
        this.listOne = listOne;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public String getPrevPageToken() {
        return prevPageToken;
    }

    public void setPrevPageToken(String prevPageToken) {
        this.prevPageToken = prevPageToken;
    }
}
