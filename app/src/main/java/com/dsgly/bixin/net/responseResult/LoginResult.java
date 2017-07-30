package com.dsgly.bixin.net.responseResult;

/**
 * Created by dengxuan on 2017/7/22.
 */

public class LoginResult extends BaseResult {

    public LoginData data;
    public String traceId;

    public static class LoginData{
        public String meId;
        public String imToken;
        public String imId;
        public String token;
    }
}
