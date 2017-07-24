package com.netease.liverecordlight.net;

import com.netease.liverecordlight.net.responseResult.BaseResult;
import com.netease.liverecordlight.net.responseResult.GetPKeyResult;
import com.netease.liverecordlight.net.responseResult.LoginResult;

/**
 * Created by dengxuan on 2017/7/22.
 */

public enum NetServiceMap{

    LoginServiceMap("publicKey", GetPKeyResult.class),
    GetPKeyServiceMap("login", LoginResult.class);

    private static final String HOSTPATH = "";
    private String hostPath;
    private String hostApi;
    private Class<? extends BaseResult> resultType;

    NetServiceMap(String hostApi,Class<? extends BaseResult> cls){
        this(HOSTPATH,hostApi,cls);
    }

    NetServiceMap(String hostPath,String hostApi,Class<? extends BaseResult> cls){
        this.hostPath = hostPath;
        this.hostApi = hostApi;
        this.resultType = cls;
    }
}
