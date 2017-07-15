package com.netease.liverecordlight.net.responseResult;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by bjdengxuan1 on 2017/6/19.
 */

public class MainPageDataResult extends BaseResult {
    private static final long serialVersionUID = 1L;

    private String scaledImageUrl; //url
    private String authorName;     // 作者姓名
    private String authorConstellation;// 作者星座
    private String issuesContent;// 发布内容
    private String issuesContentTime;// 发布时间
    private String issuesLikeNum;// 点赞数
    private String issuesCommentNum;// 评论数
    private String sex;//0 未知 1男 2女
    public static ArrayList<MainPageDataResult> getTestData(){
        ArrayList<MainPageDataResult> list = new ArrayList<MainPageDataResult>();
        for (int i=0;i<10;i++){
            MainPageDataResult result = new MainPageDataResult();
            result.setScaledImageUrl("");
            result.setAuthorName("邓选"+i);
            result.setSex(String.valueOf(i%2+1));
            Log.e("dx",String.valueOf(i%2));
            result.setAuthorConstellation("双子座");
            result.setIssuesContent("今天的天气真好");
            result.setIssuesContentTime("2017-06-15 17:20");
            result.setIssuesLikeNum("66");
            result.setIssuesCommentNum("16");
            list.add(result);
        }
        return list;
    }

    public String getScaledImageUrl() {
        return scaledImageUrl;
    }

    public void setScaledImageUrl(String scaledImageUrl) {
        this.scaledImageUrl = scaledImageUrl;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAuthorConstellation() {
        return authorConstellation;
    }

    public void setAuthorConstellation(String authorConstellation) {
        this.authorConstellation = authorConstellation;
    }

    public String getIssuesContent() {
        return issuesContent;
    }

    public void setIssuesContent(String issuesContent) {
        this.issuesContent = issuesContent;
    }

    public String getIssuesContentTime() {
        return issuesContentTime;
    }

    public void setIssuesContentTime(String issuesContentTime) {
        this.issuesContentTime = issuesContentTime;
    }

    public String getIssuesLikeNum() {
        return issuesLikeNum;
    }

    public void setIssuesLikeNum(String issuesLikeNum) {
        this.issuesLikeNum = issuesLikeNum;
    }

    public String getIssuesCommentNum() {
        return issuesCommentNum;
    }

    public void setIssuesCommentNum(String issuesCommentNum) {
        this.issuesCommentNum = issuesCommentNum;
    }
}
