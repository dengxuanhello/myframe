package com.netease.liverecordlight.biz.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mob.tools.utils.UIHandler;
import com.netease.liverecordlight.R;
import com.netease.liverecordlight.biz.base.BaseActivity;
import com.netease.liverecordlight.biz.presenter.TestPresenter;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMElemType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageListener;
import com.tencent.imsdk.TIMTextElem;
import com.tencent.imsdk.TIMValueCallBack;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;


import java.util.HashMap;
import java.util.List;


/**
 * Created by bjdengxuan1 on 2017/6/19.
 */

public class TestActivity extends BaseActivity implements Handler.Callback,
        View.OnClickListener, PlatformActionListener{

    private TestPresenter presenter;
    private TIMConversation conversation;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //presenter.doShare();
                //authorize(new Wechat());
                testIMMsg();
            }
        });
    }

    @Override
    public void initViews() {
        setContentView(R.layout.test_layout);
    }

    @Override
    public void createPresenter() {
        presenter = new TestPresenter();
        presenter.attachView(this);
    }

    private static final int MSG_USERID_FOUND = 1;
    private static final int MSG_LOGIN = 2;
    private static final int MSG_AUTH_CANCEL = 3;
    private static final int MSG_AUTH_ERROR= 4;
    private static final int MSG_AUTH_COMPLETE = 5;
    private void authorize(Platform plat) {
        if(plat.isAuthValid()) {
            String userId = plat.getDb().getUserId();
            if (!TextUtils.isEmpty(userId)) {
                UIHandler.sendEmptyMessage(MSG_USERID_FOUND, this);
                login(plat.getName(), userId, null);
                return;
            }
        }
        plat.setPlatformActionListener(this);
        plat.SSOSetting(true);
        plat.showUser(null);
    }

    public void onComplete(Platform platform, int action,
                           HashMap<String, Object> res) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_COMPLETE, this);
            login(platform.getName(), platform.getDb().getUserId(), res);
        }
        System.out.println(res);
        System.out.println("------User Name ---------" + platform.getDb().getUserName());
        System.out.println("------User ID ---------" + platform.getDb().getUserId());
    }

    public void onError(Platform platform, int action, Throwable t) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_ERROR, this);
        }
        t.printStackTrace();
    }

    public void onCancel(Platform platform, int action) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_CANCEL, this);
        }
    }

    private void login(String plat, String userId, HashMap<String, Object> userInfo) {
        Message msg = new Message();
        msg.what = MSG_LOGIN;
        msg.obj = plat;
        UIHandler.sendMessage(msg, this);
    }

    public boolean handleMessage(Message msg) {
        switch(msg.what) {
            case MSG_USERID_FOUND: {
                Toast.makeText(this, "userid_found", Toast.LENGTH_SHORT).show();
            }
            break;
            case MSG_LOGIN: {

                String text = (String) msg.obj;
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                System.out.println("---------------");

//				Builder builder = new Builder(this);
//				builder.setTitle(R.string.if_register_needed);
//				builder.setMessage(R.string.after_auth);
//				builder.setPositiveButton(R.string.ok, null);
//				builder.create().show();
            }
            break;
            case MSG_AUTH_CANCEL: {
                Toast.makeText(this, "auth_cancel", Toast.LENGTH_SHORT).show();
                System.out.println("-------MSG_AUTH_CANCEL--------");
            }
            break;
            case MSG_AUTH_ERROR: {
                Toast.makeText(this, "auth_error", Toast.LENGTH_SHORT).show();
                System.out.println("-------MSG_AUTH_ERROR--------");
            }
            break;
            case MSG_AUTH_COMPLETE: {
                Toast.makeText(this, "auth_error", Toast.LENGTH_SHORT).show();
                System.out.println("--------MSG_AUTH_COMPLETE-------");
            }
            break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {

    }


    private void testIMMsg(){
        String peer = "dengxuan";  //获取与用户 "dengxuan" 的会话
        conversation = TIMManager.getInstance().getConversation(
                TIMConversationType.C2C,    //会话类型：单聊
                peer);
        TIMMessage timMessage = new TIMMessage();
        //添加文本内容
        TIMTextElem elem = new TIMTextElem();
        elem.setText("a new msg");
        timMessage.addElement(elem);
        conversation.sendMessage(timMessage, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                Log.i("dx","send failed:"+s);
            }

            @Override
            public void onSuccess(TIMMessage timMessage) {
                Log.i("dx","send success");
            }
        });
    }

    @Override
    public boolean onNewMessages(List<TIMMessage> list) {
        Log.i("dx", String.valueOf(list.size()));
        for(TIMMessage msg:list){
            for(int i = 0; i < msg.getElementCount(); ++i) {
                TIMElem elem = msg.getElement(i);
                //获取当前元素的类型
                TIMElemType elemType = elem.getType();
                if (elemType == TIMElemType.Text) {
                    //处理文本消息
                    Log.i("dx msg",((TIMTextElem)elem).getText());
                } else if (elemType == TIMElemType.Image) {
                    //处理图片消息
                }//...处理更多消息
            }
        }
        return false;
    }
}
