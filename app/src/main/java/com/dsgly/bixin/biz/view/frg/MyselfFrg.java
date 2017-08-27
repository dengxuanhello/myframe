package com.dsgly.bixin.biz.view.frg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dsgly.bixin.R;
import com.dsgly.bixin.biz.base.BaseFragment;
import com.dsgly.bixin.biz.view.CompleteProfileActivity;
import com.dsgly.bixin.biz.view.HomeActivity;
import com.dsgly.bixin.biz.view.MyVideoMomentActivity;
import com.dsgly.bixin.biz.view.MyVideoMomentsActivity;
import com.dsgly.bixin.biz.view.SelfMainPageActivity;
import com.dsgly.bixin.biz.view.SettingActivity;
import com.dsgly.bixin.net.responseResult.UserInfo;
import com.dsgly.bixin.utils.UCUtils;
import com.tencent.qcloud.ui.CircleImageView;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by dengxuan on 2017/7/16.
 */

public class MyselfFrg extends BaseFragment implements View.OnClickListener {

    private CircleImageView avatar;
    private TextView username;
    private TextView userIntroduce;
    private TextView editSelfMainPage;
    private LinearLayout myVideoMoments;
    private LinearLayout shareToMyFrends;
    private LinearLayout mySettings;

    private UserInfo userInfo;

    public static MyselfFrg newInstance(Bundle bundle){
        MyselfFrg frg = new MyselfFrg();
        if(bundle != null) {
            frg.setArguments(bundle);
        }
        return frg;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.myself_frg_fragment,container,false);
        avatar = (CircleImageView) v.findViewById(R.id.user_avatar);
        username = (TextView) v.findViewById(R.id.user_name);
        userIntroduce = (TextView) v.findViewById(R.id.user_introduce);
        editSelfMainPage = (TextView) v.findViewById(R.id.edit_self_main_page);
        myVideoMoments = (LinearLayout) v.findViewById(R.id.my_video_moments);
        shareToMyFrends = (LinearLayout) v.findViewById(R.id.share_to_my_frends);
        mySettings = (LinearLayout) v.findViewById(R.id.my_settings);
        avatar.setOnClickListener(this);
        username.setOnClickListener(this);
        userIntroduce.setOnClickListener(this);
        editSelfMainPage.setOnClickListener(this);
        myVideoMoments.setOnClickListener(this);
        shareToMyFrends.setOnClickListener(this);
        mySettings.setOnClickListener(this);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userInfo = UCUtils.getInstance().getUserInfo();
        if(userInfo != null){
            username.setText(userInfo.nickName);
            Glide.with(getContext()).load(userInfo.headImgUrl).into(avatar);
            userIntroduce.setText(userInfo.description);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.equals(mySettings)){
            SettingActivity.startSettingActivity(getActivity());
        }else if(v.equals(shareToMyFrends)){
            showShare();
        }else if(v.equals(editSelfMainPage)){
            CompleteProfileActivity.startCompleteProfileActivity(getActivity(), HomeActivity.REQ_FOR_COMPLETE_INFO);
        }else if(v.equals(avatar)){
            SelfMainPageActivity.startSelfMainPageActivity(getActivity());
        }else if(v.equals(myVideoMoments)){
            //MyVideoMomentsActivity.startMyVideoMomentsActivity(getActivity());
            MyVideoMomentActivity.startMyVideoMomentActivity(getActivity());
        }
    }
    public void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不     调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("share");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getActivity().getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
        // 启动分享GUI
        oks.show(getActivity());
    }
}
