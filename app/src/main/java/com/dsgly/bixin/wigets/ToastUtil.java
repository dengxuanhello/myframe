package com.dsgly.bixin.wigets;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by madong on 2017/9/26.
 */

public class ToastUtil {
    public static void showToast(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }
}
