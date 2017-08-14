package com.dsgly.bixin.net.responseResult;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dengxuan on 2017/8/10.
 */

public class CommentsResult extends BaseResult {

    public List<UserComment> data;

    public static class UserComment implements Serializable{
        private static final long serialVersionUID = 1L;
        public String content;
        public String forUserId;
        public UserInfo forUserModel;
        public String gmtCreated;
        public String gmtModified;
        public String id;
        public String momentId;
        public String userId;
        public UserInfo userModel;
    }
}
