package com.dsgly.bixin.biz.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.dsgly.bixin.R;
import com.dsgly.bixin.biz.base.BaseActivity;
import com.dsgly.bixin.biz.view.frg.ImagePreviewFragment;

/**
 * Created by madong on 2017/9/26.
 */

public class ImagePreviewActivity extends BaseActivity {

    private final static String FRAGMENT_NAME = "fragment_name";
    private final static String FRAGMENT_ARG = "fragment_arg";

    @Override
    public void initViews() {
        super.initViews();

        setContentView(R.layout.activity_image_preview);

        loadFragment();
    }

    private void loadFragment() {
        String fragmentName = getIntent().getStringExtra(FRAGMENT_NAME);

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragmentName);
        if (fragment == null) {
            fragment = Fragment.instantiate(this, fragmentName, getIntent().getBundleExtra(FRAGMENT_ARG));
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment, fragmentName).commit();
        } else if (fragment.isDetached()) {
            getSupportFragmentManager().beginTransaction().attach(fragment).commit();
        }

    }

    public static void startActivity(Context context, int position, String urlList) {
        Intent intent = new Intent(context, ImagePreviewActivity.class);
        intent.putExtra(FRAGMENT_NAME, ImagePreviewFragment.class.getName());
        Bundle bundle = new Bundle();
        bundle.putInt(ImagePreviewFragment.KEY_IMAGE_INDEX, position);
        bundle.putString(ImagePreviewFragment.KEY_IMAGE_LIST, urlList);
        intent.putExtra(FRAGMENT_ARG, bundle);
        context.startActivity(intent);

    }
}
