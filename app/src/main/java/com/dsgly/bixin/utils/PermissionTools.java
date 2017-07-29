package com.dsgly.bixin.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengxuan.deng on 16-8-10.
 */
public class PermissionTools {
    public static final int PERMISSION_REQUESTCODE_READ_PHONE_STATE = 0x02;
    public static final int PERMISSION_REQUESTCODE_READ_PHONE_STATE_ON_COMBINE_LOGIN = 0x04;

    /**
     * sdk版本是否需要考虑权限适配
     * @return
     */
    public static boolean sdkCarePermission(){
        return android.os.Build.VERSION.SDK_INT >= 23;
    }

    public static String[] getPermissionsArray(List<String> permissions){
        if(ArrayUtils.isEmpty(permissions)){
            return null;
        }

        return permissions.toArray(new String[permissions.size()]);
    }

    /**
     * 从permissions中得到未取得授权的权限
     * @param context
     * @param permissions
     * @return
     */
    public static List<String> getDeniedPermissions(Context context, String... permissions) {
        List<String> deniedPermissions = new ArrayList<String>();
        for (String permission : permissions) {
            int permissionResult = ContextCompat.checkSelfPermission(context, permission);
            if (permissionResult == PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(permission);
            }
        }
        return deniedPermissions;
    }

    /**
     * 从授权结果中找出没有授权成功的权限
     * @param permissions
     * @param grantResults
     * @return
     */
    public static List<String> getDeniedPermissionsOnResult(String[] permissions, int[] grantResults){

        List<String> deniedPermissions = new ArrayList<String>();
        for (int i = 0; i < grantResults.length; i++) {
            if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                deniedPermissions.add(permissions[i]);
            }
        }

        return deniedPermissions;
    }

    /**
     * 使用该默认提示，需要保证请求权限的时候所需要的权限在Permission枚举中
     * @param deniedPermissions
     * @return
     */
    public static String getDefaultPermissionTip(List<String> deniedPermissions){
        String deniedPermissionsDesc = getDeniedPermissionsDesc(deniedPermissions);
        if(!TextUtils.isEmpty(deniedPermissionsDesc)){
            return "请到系统设置里面打开" + deniedPermissionsDesc;
        }

        return null;
    }

    /**
     * 获得被拒绝权限的名称列表描述
     * @param deniedPermissions
     * @return
     */
    public static String getDeniedPermissionsDesc(List<String> deniedPermissions){
        if(ArrayUtils.isEmpty(deniedPermissions)){
            return null;
        }

        List<String> tipMsgs = new ArrayList<String>();
        for(String permissionName : deniedPermissions){
            Permission permission = Permission.findPermissionByKey(permissionName);
            if(permission != null){
                tipMsgs.add(permission.getDesc());
            }
        }

        if(!ArrayUtils.isEmpty(tipMsgs)){
            StringBuilder sb = new StringBuilder();
            int size = tipMsgs.size();
            for(int i = 0; i < size; i++){
                if(i > 0) {
                    if (i == size - 1) {
                        sb.append("和");
                    } else {
                        sb.append("、");
                    }
                }
                sb.append(tipMsgs.get(i));
            }
            return sb.toString();
        }

        return null;
    }

}
