package com.dsgly.bixin.biz.view;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dsgly.bixin.R;
import com.dsgly.bixin.biz.base.BaseActivity;
import com.dsgly.bixin.net.NetServiceMap;
import com.dsgly.bixin.net.NetworkParam;
import com.dsgly.bixin.net.RequestUtils;
import com.dsgly.bixin.net.requestParam.FeedbackParam;

/**
 * Created by dengxuan on 2017/8/27.
 */

public class FeedBackActivity extends BaseActivity {

    private EditText feedBackEt;
    private EditText contactEt;
    private Button commitBtn;

    public static void startFeedBackActivity(Context context){
        if(context != null) {
            Intent intent = new Intent();
            intent.setClass(context, FeedBackActivity.class);
            context.startActivity(intent);
        }
    }

    @Override
    public void initViews() {
        super.initViews();
        setContentView(R.layout.feed_back_layout);
        feedBackEt = (EditText) findViewById(R.id.feedback_content);
        contactEt = (EditText) findViewById(R.id.contact);
        commitBtn = (Button) findViewById(R.id.commit);
        commitBtn.setOnClickListener(this);
        findViewById(R.id.back_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.equals(commitBtn)){
            commit();
        }
    }

    private void commit(){
        if(TextUtils.isEmpty(feedBackEt.getText().toString())){
            showToast("请填写反馈内容");
            return;
        }
        FeedbackParam feedbackParam = new FeedbackParam();
        feedbackParam.comment = feedBackEt.getText().toString();
        feedbackParam.contact = contactEt.getText().toString();
        NetworkParam param = new NetworkParam(this);
        param.key = NetServiceMap.FeedBack;
        param.param = feedbackParam;
        RequestUtils.startPostRequest(param);
    }

    @Override
    public void onMsgSearchComplete(NetworkParam param) {
        super.onMsgSearchComplete(param);
        if(param.key == NetServiceMap.FeedBack){
            if(param.baseResult!=null&&"200".equals(param.baseResult.code)){
                showToast("提交成功，感谢您的反馈");
            }
            finish();
        }
    }
}
