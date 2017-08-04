package com.wxkj.apps.news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wxkj.apps.news.R;
import com.wxkj.apps.news.entity.NewsInfo;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by taosong on 17/6/6.
 */

public class DiscoverListAdapter  extends BaseRecyclerAdapter<NewsInfo> {


    public DiscoverListAdapter(Context context, int mode) {
        super(context, mode);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_news_item, parent, false));
    }

    @SuppressWarnings("all")
    @Override
    protected void onBindDefaultViewHolder(RecyclerView.ViewHolder h, NewsInfo item, int position) {
        ViewHolder vh = (ViewHolder) h;

        vh.mTitle.setText(item.getTitle());

        vh.mTime.setText(item.getCreateTime());
        vh.mNumber.setText("");

        ArrayList<NewsInfo.NewsImageInfo> picList =  item.getPictures();

        if (null !=picList && picList.size() >0){

          //  vh.mImageView.setImageForNet(picList.get(0).getFilePath());

        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_event_title)
        TextView  mTitle;

        @Bind(R.id.tv_event_pub_date)
        TextView  mTime;

        @Bind(R.id.tv_event_member)
        TextView  mNumber;

        @Bind(R.id.iv_event)
        ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}