package com.dsgly.bixin.wigets;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

/**
 * Created by bjdengxuan1 on 2017/7/12.
 */

public class WrapHeightLayoutManager extends GridLayoutManager {

    public WrapHeightLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }
    @Override
    public boolean canScrollVertically() {
        return false;
    }
}
