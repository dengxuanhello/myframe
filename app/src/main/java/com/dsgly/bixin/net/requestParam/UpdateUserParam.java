package com.dsgly.bixin.net.requestParam;

import com.dsgly.bixin.net.responseResult.UserInfo;

/**
 * Created by dengxuan on 2017/8/26.
 */

public class UpdateUserParam extends BaseParam {
    public String userId ;
    public String nickName ;
    public String gender ;
    public String birthYear ;
    public String birthMonth ;
    public String birthDay  ;
    public String constellation;
    public String height  ;
    public String weight  ;
    public String college  ;
    public String collegeId  ;
    public String idealPartnerDescription  ;
    public String description  ;

    public UpdateUserParam(UserInfo userInfo){
        this.userId = userInfo.userId;
        this.nickName = userInfo.nickName;
        this.gender = userInfo.gender;
        this.birthYear = userInfo.birthYear;
        this.birthMonth = userInfo.birthMonth;
        this.birthDay = userInfo.birthDay;
        this.constellation = userInfo.constellation;
        this.height = userInfo.height;
        this.weight = userInfo.weight;
        this.college = userInfo.college;
        this.collegeId = userInfo.collegeId;
        this.idealPartnerDescription = userInfo.idealPartnerDescription;
        this.description = userInfo.description;
    }
}
