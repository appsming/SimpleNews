package com.wxkj.apps.news.adapter;

import com.wxkj.apps.news.R;
import com.wxkj.apps.news.entity.NewsInfo;

import java.util.ArrayList;

/**
 * Created by taosong on 17/6/5.
 */

public class NewsListAdapter extends BaseListAdapter<NewsInfo> {

    public NewsListAdapter(Callback callback) {
        super(callback);
    }

    @Override
    protected void convert(ViewHolder vh, NewsInfo item, int position) {

        vh.setText(R.id.tv_event_title, item.getTitle());

        vh.setText(R.id.tv_event_pub_date,item.getCreateTime());
        vh.setText(R.id.tv_event_member, "浏览数:"+item.getViewsNum());

       ArrayList<NewsInfo.NewsImageInfo> picList =  item.getPictures();

        if (null !=picList && picList.size() >0){

            vh.setImageForNet(R.id.iv_event, picList.get(0).getFilePath());
        }





    }

    @Override
    protected int getLayoutId(int position, NewsInfo item) {
        return R.layout.list_news_item;
    }


}
