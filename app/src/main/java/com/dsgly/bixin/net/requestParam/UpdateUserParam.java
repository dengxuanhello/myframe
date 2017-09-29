package com.dsgly.bixin.net.requestParam;

/**
 * Created by dengxuan on 2017/8/26.
 */

public class UpdateUserParam extends BaseParam {
    public String userStr;

    public static class UserInfo {
        public String nickName;
        public String college  ;
        public String collegeId  ;
        public String idealPartnerDescription  ;
        public String description  ;
    }

}
