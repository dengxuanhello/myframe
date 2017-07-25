package com.netease.liverecordlight.net;

import com.netease.liverecordlight.net.responseResult.BaseResult;
import com.netease.liverecordlight.net.responseResult.GetPKeyResult;
import com.netease.liverecordlight.net.responseResult.LoginResult;

/**
 * 所有的后端Api在此申明
 * Created by dengxuan on 2017/7/22.
 */

public enum NetServiceMap{

    GetPKeyServiceMap("publicKey", GetPKeyResult.class),
    LoginServiceMap("login", LoginResult.class),
    MonentList("moment/list", BaseResult.class);


    private static final String HOSTPATH = "http://139.199.20.201/";
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

    public String getApi(){
        return hostApi;
    }

    public String getHostPath(){
        return hostPath;
    }

    public Class<? extends BaseResult> getResultClz(){
        return resultType;
    }
}
