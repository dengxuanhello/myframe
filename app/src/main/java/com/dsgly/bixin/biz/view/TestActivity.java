package com.dsgly.bixin.biz.view;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mob.tools.utils.UIHandler;
import com.dsgly.bixin.R;
import com.dsgly.bixin.biz.base.BaseActivity;
import com.dsgly.bixin.biz.presenter.TestPresenter;
import com.dsgly.bixin.constant.Config;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMTextElem;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.qcloud.presentation.business.LoginBusiness;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import life.knowledge4.videotrimmer.K4LVideoTrimmer;
import life.knowledge4.videotrimmer.interfaces.OnK4LVideoListener;
import life.knowledge4.videotrimmer.interfaces.OnTrimVideoListener;
import life.knowledge4.videotrimmer.utils.FileUtils;


import java.util.HashMap;


/**
 * Created by bjdengxuan1 on 2017/6/19.
 */

public class TestActivity extends BaseActivity implements Handler.Callback,
        View.OnClickListener, PlatformActionListener, OnTrimVideoListener, OnK4LVideoListener {

    private TestPresenter presenter;
    private TIMConversation conversation;
    private K4LVideoTrimmer k4LVideoTrimmer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginBusiness.loginIm("dengxuan", Config.QQ_SDK_SIG, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                Log.i("dx",s);
            }

            @Override
            public void onSuccess() {
                Log.i("dx","login success");
            }
        });
        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //presenter.doShare();
                //authorize(new Wechat());
                testIMMsg();
            }
        });
       /* k4LVideoTrimmer = ((K4LVideoTrimmer) findViewById(R.id.timeLine));
        if (k4LVideoTrimmer != null) {
            k4LVideoTrimmer.setMaxDuration(5);
            //k4LVideoTrimmer.setVideoURI(Uri.parse("storage/emulated/0/ksy_sv_compose_test/1497947721646.mp4"));
            k4LVideoTrimmer.setVideoInformationVisibility(true);
            k4LVideoTrimmer.setOnTrimVideoListener(this);
            k4LVideoTrimmer.setOnK4LVideoListener(this);
        }*/
        //pickFromGallery();
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
        String peer = "dengxuan1";  //获取与用户 "dengxuan" 的会话
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

    /*@Override
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
    }*/


    final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 0x111;
    final int REQUEST_VIDEO_TRIMMER = 0x112;
    private void pickFromGallery() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, "Storage read permission is needed to pick files.", REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
            Intent intent = new Intent();
            intent.setTypeAndNormalize("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_VIDEO_TRIMMER);
        }
    }

    private void requestPermission(final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("授权");
            builder.setMessage(rationale);
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(TestActivity.this, new String[]{permission}, requestCode);
                }
            });
            builder.setNegativeButton("cancel", null);
            builder.show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_VIDEO_TRIMMER) {
                final Uri selectedUri = data.getData();
                if (selectedUri != null) {
                    ///storage/emulated/0/ksy_sv_compose_test/1497947721646.mp4
                    String path = FileUtils.getPath(this, selectedUri);
                    k4LVideoTrimmer.setVideoURI(Uri.parse(path));
                }
            }
        }
    }

    @Override
    public void onTrimStarted() {

    }

    @Override
    public void getResult(Uri uri) {

    }

    @Override
    public void cancelAction() {

    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void onVideoPrepared() {

    }
}
