package com.wxkj.apps.news.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by taosong on 17/6/5.
 */

public class NewsInfo  extends BaseEntity {

    private String  apptype,createTime,
            creatorName,deleteflag,content,
            lastUpdateTime,lastUpdateUserName,
            title,setType,type
            ;

    private int creator,id,lastUpdateUser,viewsNum;

    private boolean storeFlag,lovleFlag;


    public boolean isStoreFlag() {
        return storeFlag;
    }

    public void setStoreFlag(boolean storeFlag) {
        this.storeFlag = storeFlag;
    }

    public boolean isLovleFlag() {
        return lovleFlag;
    }

    public void setLovleFlag(boolean lovleFlag) {
        this.lovleFlag = lovleFlag;
    }

    private ArrayList<NewsImageInfo> pictures;

    public class NewsImageInfo implements Serializable{

        private String fileName,filePath;

        private int creator,deleteflag,fileSize,fileid,lastUpdateUser;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public int getCreator() {
            return creator;
        }

        public void setCreator(int creator) {
            this.creator = creator;
        }

        public int getDeleteflag() {
            return deleteflag;
        }

        public void setDeleteflag(int deleteflag) {
            this.deleteflag = deleteflag;
        }

        public int getFileSize() {
            return fileSize;
        }

        public void setFileSize(int fileSize) {
            this.fileSize = fileSize;
        }

        public int getFileid() {
            return fileid;
        }

        public void setFileid(int fileid) {
            this.fileid = fileid;
        }

        public int getLastUpdateUser() {
            return lastUpdateUser;
        }

        public void setLastUpdateUser(int lastUpdateUser) {
            this.lastUpdateUser = lastUpdateUser;
        }
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getApptype() {
        return apptype;
    }

    public void setApptype(String apptype) {
        this.apptype = apptype;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getDeleteflag() {
        return deleteflag;
    }

    public void setDeleteflag(String deleteflag) {
        this.deleteflag = deleteflag;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getLastUpdateUserName() {
        return lastUpdateUserName;
    }

    public void setLastUpdateUserName(String lastUpdateUserName) {
        this.lastUpdateUserName = lastUpdateUserName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSetType() {
        return setType;
    }

    public void setSetType(String setType) {
        this.setType = setType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(int lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    public int getViewsNum() {
        return viewsNum;
    }

    public void setViewsNum(int viewsNum) {
        this.viewsNum = viewsNum;
    }

    public ArrayList<NewsImageInfo> getPictures() {
        return pictures;
    }

    public void setPictures(ArrayList<NewsImageInfo> pictures) {
        this.pictures = pictures;
    }
}
