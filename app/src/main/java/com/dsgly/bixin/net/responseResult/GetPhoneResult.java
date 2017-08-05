package com.dsgly.bixin.net.responseResult;

/**
 * Created by dengxuan on 2017/8/5.
 */

public class GetPhoneResult extends BaseResult {
    public PhoneData data;
    public static class PhoneData{
        public String diallingCode;//+86
        public String phone;
    }
}
