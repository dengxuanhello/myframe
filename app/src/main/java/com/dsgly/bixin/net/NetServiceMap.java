package com.dsgly.bixin.net;

import com.dsgly.bixin.net.responseResult.BaseResult;
import com.dsgly.bixin.net.responseResult.GetPKeyResult;
import com.dsgly.bixin.net.responseResult.LoginResult;
import com.dsgly.bixin.net.responseResult.MainPageDataResult;
import com.dsgly.bixin.net.responseResult.RegistResult;

/**
 * 所有的后端Api在此申明
 * Created by dengxuan on 2017/7/22.
 */

public enum NetServiceMap{

    GetPKeyServiceMap("publicKey", GetPKeyResult.class),
    LoginServiceMap("login", LoginResult.class),
    RegistServiceMap("signIn",RegistResult.class),
    SendVCodeMap("sendCode",BaseResult.class),
    GetServerTime("serverTime",BaseResult.class),
    LogOut("logOut",BaseResult.class),
    ThirdPartyLogin("thirdPartyLogin",BaseResult.class),
    MonentList("moment/list", MainPageDataResult.class);


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
