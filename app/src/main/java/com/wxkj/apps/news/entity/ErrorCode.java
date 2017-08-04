package com.wxkj.apps.news.entity;

/**
 * Created by taosong on 17/6/3.
 */

public enum  ErrorCode {

    SUCCESS("0000"),SUCCESSMSG("请求成功!"),
    FAIL("9999"),FAILMSG("请求失败!"),
    NOTFOUND("0001"),NOTFOUNDMSG("未查询到数据"),
    NOTPARAMETER("0002"),NOTPARAMETERMSG("缺少必要参数信息"),
    REGSUCCESS("0003"),REGSUCCESSMSG("注册成功!"),
    REPEATREG("0004"),REPEATREGMSG("账号已被注册!"),
    REGFAIL("0005"),REGFAILMSG("注册失败!"),
    STORED("0006"),STOREDMSG("您已收藏!"),
    LOVED("0007"),LOVEDMSG("您已点赞!");

    private String code;
    private ErrorCode(String code){
        this.code = code;
    }
    public String getValue() {
        return this.code;
    }
}

