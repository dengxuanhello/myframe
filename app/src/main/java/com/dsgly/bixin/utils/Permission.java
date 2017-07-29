package com.dsgly.bixin.utils;

import android.Manifest;

/**
 * 权限描述类
 * Created by dengxuan.deng on 16-8-10.
 */
public enum Permission {
    CAMERA(Manifest.permission.CAMERA,"相机权限"),
    READ_PHONE_STATE(Manifest.permission.READ_PHONE_STATE, "电话权限"),
    ACCESS_FINE_LOCATION(Manifest.permission.ACCESS_FINE_LOCATION, "位置信息权限"),
    READ_CONTACT_STATE(Manifest.permission.READ_CONTACTS, "通讯录权限");

    private String mPermissionKey;
    private String mPermissionDesc;

    Permission(String permissionKey, String permissionDesc){
        mPermissionKey = permissionKey;
        mPermissionDesc = permissionDesc;
    }

    public String getKey(){
        return mPermissionKey;
    }

    public String getDesc(){
        return mPermissionDesc;
    }

    public static Permission findPermissionByKey(String permissionKey){
        for(Permission permission : Permission.values()){
            if(permission.getKey().equalsIgnoreCase(permissionKey)){
                return permission;
            }
        }

        return null;
    }
}
