package com.dsgly.bixin.net.responseResult;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by bjdengxuan1 on 2017/6/19.
 */

public class MainPageDataResult extends BaseResult {
    private static final long serialVersionUID = 1L;
    public ArrayList<MomentData> data;
    public String cursor;
    public String msg;
    public String pageNo;
    public String pageSize;
    public String totalCount;
    public String totalPages;
    public String traceId;
    public static class MomentData{
        public String previewPic; //url
        public String authorName;     // 作者姓名
        public String authorConstellation;// 作者星座
        public String content;// 发布内容
        public String gmtCreated;// 发布时间
        public String gmtModified;//"2017-07-26T11:38:09.324Z",
        public String starNum;// 点赞数
        public String commentNum;// 评论数
        public String gender;//0 未知 1男 2女
        public String hasStared;
        public String id;
        public String userId;
        public String videoPath;
        public String videoPathOrigin;

    }

    private String previewPic; //url
    private String authorName;     // 作者姓名
    private String authorConstellation;// 作者星座
    private String content;// 发布内容
    private String gmtCreated;// 发布时间
    private String gmtModified;//"2017-07-26T11:38:09.324Z",
    private String starNum;// 点赞数
    private String commentNum;// 评论数
    private String gender;//0 未知 1男 2女
    private String hasStared;
    private String id;
    private String userId;
    private String videoPath;
    private String videoPathOrigin;

    public static ArrayList<MainPageDataResult> getTestData(){
        ArrayList<MainPageDataResult> list = new ArrayList<MainPageDataResult>();
        for (int i=0;i<10;i++){
            MainPageDataResult result = new MainPageDataResult();
            result.setPreviewPic("");
            result.setAuthorName("邓选"+i);
            result.setGender(String.valueOf(i%2+1));
            Log.e("dx",String.valueOf(i%2));
            result.setAuthorConstellation("双子座");
            result.setContent("今天的天气真好");
            result.setGmtCreated("2017-06-15 17:20");
            result.setStarNum("66");
            result.setCommentNum("16");
            list.add(result);
        }
        return list;
    }

    public String getPreviewPic() {
        return previewPic;
    }

    public void setPreviewPic(String previewPic) {
        this.previewPic = previewPic;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAuthorConstellation() {
        return authorConstellation;
    }

    public void setAuthorConstellation(String authorConstellation) {
        this.authorConstellation = authorConstellation;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGmtCreated() {
        return gmtCreated;
    }

    public void setGmtCreated(String gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    public String getStarNum() {
        return starNum;
    }

    public void setStarNum(String starNum) {
        this.starNum = starNum;
    }

    public String getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }
}
