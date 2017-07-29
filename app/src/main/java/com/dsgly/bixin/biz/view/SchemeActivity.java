package com.dsgly.bixin.biz.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.FrameLayout;

import com.dsgly.bixin.biz.base.BaseActivity;
import com.dsgly.bixin.constant.RequestCode;
import com.dsgly.bixin.utils.IntentUtils;

import java.util.Map;
import java.util.Set;

/**
 * Created by dengxuan on 2017/7/2.
 */

public class SchemeActivity extends BaseActivity {
    private Intent comingIntent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new FrameLayout(this));
        comingIntent = getIntent();
        if(comingIntent != null && comingIntent.getData() != null){
            dispatchUri(comingIntent.getData());
        }else {
            finish();
        }
    }

    private void dispatchUri(Uri uri){
        String schemeStr = uri.getScheme();//bixinscheme://dispatcher/login?username=aaa
        String host = uri.getEncodedAuthority();//dispatcher
        if(TextUtils.isEmpty(schemeStr) || TextUtils.isEmpty(host)){
            return;
        }
        String type = uri.getLastPathSegment();//login
        Map<String, String> args = IntentUtils.splitParams1(uri);//username=aaa
        if("login".equalsIgnoreCase(type)){
            Intent intent = new Intent();
            intent.setClass(this,LoginActivity.class);
            startActivityForResult(intent, RequestCode.SCHEME_DISPATCHER_REQUEST_CODE,getBundleFormMap(args));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(resultCode, data);
        finish();
    }

    private Bundle getBundleFormMap(Map<String, String> args){
        Bundle bundle = new Bundle();
        if(args != null){
            Set<String> keySet = args.keySet();
            for(String key : keySet){
                String value = args.get(key);
                bundle.putString(key,value);
            }
        }
        return bundle;
    }
}
