package com.dsgly.bixin.net.responseResult;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dengxuan on 2017/8/13.
 */

public class GalleryResult extends BaseResult {

    public List<GalleryInfo> data;
    public String pageNo;
    public String pageSize;
    public String totalCount;
    public String totalPages;
    public String traceId;

    public static class GalleryInfo implements Serializable{
        private static final long serialVersionUID = 1L;
        public GalleryInfo(String pic){
            this.pic = pic;
        }
        public String gmtCreated;
        public String gmtModified;
        public String pic;
        public String picId;
        public String picOrigin;
        public String picThumb;
        public String userId;
    }
}
