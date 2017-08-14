package com.dsgly.bixin.net.responseResult;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by bjdengxuan1 on 2017/6/19.
 */

public class MainPageDataResult extends BaseResult {
    private static final long serialVersionUID = 1L;
    public ArrayList<MomentData> data;
    public String cursor;
    public String pageNo;
    public String pageSize;
    public String totalCount;
    public String totalPages;
    public String traceId;
    public static class MomentData implements Serializable{
        private static final long serialVersionUID = 1L;
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

    public static ArrayList<MomentData> getTestData(){
        ArrayList<MomentData> list = new ArrayList<MomentData>();
        for (int i=0;i<10;i++){
            MomentData result = new MomentData();
            result.previewPic=("");
            result.authorName = ("邓选"+i);
            result.gender = (String.valueOf(i%2+1));
            result.authorConstellation = ("双子座");
            result.content = ("今天的天气真好");
            result.gmtCreated = ("2017-06-15 17:20");
            result.starNum = ("66");
            result.commentNum = ("16");
            list.add(result);
        }
        return list;
    }
}
