package com.dsgly.bixin.biz.base;

import android.os.Bundle;

/**
 * Created by bjdengxuan1 on 2017/6/29.
 */

public interface ActivityJumpInterface {
    /**
     * 打开新界面
     *
     * @param openClass 新开页面
     * @param bundle    参数
     */
    public void startActivity(Class<?> openClass, Bundle bundle);

    /**
     * 打开新界面，期待返回
     *
     * @param openClass 新界面
     * @param requestCode 请求码
     * @param bundle 参数
     */
    public void openActivityForResult(Class<?> openClass, int requestCode, Bundle bundle);

    /**
     * 返回到上个页面
     *
     * @param bundle 参数
     */
    public void goBackWithResult(Bundle bundle);
}
