package com.wxkj.apps.news.fragment;

import android.view.View;
import android.widget.TextView;

import com.wxkj.apps.news.AppContext;
import com.wxkj.apps.news.R;
import com.wxkj.apps.news.base.BaseTitleFragment;
import com.wxkj.apps.news.entity.UserInfo;
import com.wxkj.apps.news.interf.OnTabReselectListener;
import com.wxkj.apps.news.ui.MyCollectionActivity;
import com.wxkj.apps.news.ui.SearchActivity;
import com.wxkj.apps.news.utils.UIHelper;

import butterknife.Bind;
import butterknife.OnClick;

import static com.wxkj.apps.news.R.id.lv_user_info;
import static com.wxkj.apps.news.R.id.rl_about;
import static com.wxkj.apps.news.R.id.rl_adverise;
import static com.wxkj.apps.news.R.id.rl_collection;
import static com.wxkj.apps.news.R.id.rl_share;
import static com.wxkj.apps.news.R.id.rl_update;

public class UserInfoFragment extends BaseTitleFragment implements View.OnClickListener,OnTabReselectListener {


    @Bind(R.id.tv_nickname)
    TextView mTvNickname;

    @Bind(R.id.tv_ndesc)
    TextView mTvDesc;

    @Bind(R.id.tv_unlogin)
    TextView mTvUnlogin;

    private   UserInfo userInfo = null ;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected int getTitleRes() {
        return R.string.main_tab_name_my;
    }


    @Override
    protected int getIconRes() {
        return R.mipmap.btn_search_normal;
    }

    @Override
    protected View.OnClickListener getIconClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.show(getContext());
            }
        };
    }


    private  void initLoginData(){

        userInfo = AppContext.getInstance().getLoginUser();
        if (null !=userInfo) {
            fillUI(userInfo);
            updateUserView(userInfo.isLogin());
        }else {

            updateUserView(false);
        }

    }



    private void fillUI(UserInfo userInfo) {
        if (userInfo == null)
            return;

        mTvNickname.setText(userInfo.getNickName());
        mTvDesc.setText(userInfo.getUname());
    }


    public  void updateUserView(boolean  islogin){

        if (islogin) {
            mTvUnlogin.setVisibility(View.GONE);
            mTvDesc.setVisibility(View.VISIBLE);
            mTvNickname.setVisibility(View.VISIBLE);
        } else {
            mTvUnlogin.setVisibility(View.VISIBLE);
            mTvDesc.setVisibility(View.GONE);
            mTvNickname.setVisibility(View.GONE);
        }


    }


    @Override
    public void onTabReselect() {
        initLoginData();
    }

    @OnClick({lv_user_info,rl_collection,rl_adverise,rl_share,rl_update,rl_about})
    @Override
    public void onClick(View view) {

        int   vId = view.getId();

        switch (vId) {
            case lv_user_info:

                userInfo = AppContext.getInstance().getLoginUser();
                if (null != userInfo) {

                    if (!userInfo.isLogin()) {

                        UIHelper.showLoginActivity(getActivity());
                    } else {


                    }


                } else {
                    UIHelper.showLoginActivity(getActivity());

                }

                break;

            case rl_collection:

                userInfo = AppContext.getInstance().getLoginUser();
                if (null != userInfo) {

                    if (!userInfo.isLogin()) {

                        UIHelper.showLoginActivity(getActivity());
                    } else {

                        MyCollectionActivity.show(getActivity());

                    }


                } else {
                    UIHelper.showLoginActivity(getActivity());

                }


                break;

            case rl_adverise:
                break;
            case  rl_share:
                break;
            case rl_update:
                break;

            case  rl_about:
                break;


        }

    }

    @Override
    public void onResume() {
        super.onResume();
        initLoginData();
    }

}