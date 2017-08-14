package com.dsgly.bixin.biz.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dsgly.bixin.R;
import com.dsgly.bixin.biz.base.BaseActivity;
import com.dsgly.bixin.biz.view.adapter.MainPageAdapter;
import com.dsgly.bixin.net.NetServiceMap;
import com.dsgly.bixin.net.NetworkParam;
import com.dsgly.bixin.net.RequestUtils;
import com.dsgly.bixin.net.responseResult.GalleryResult;
import com.dsgly.bixin.net.responseResult.GetPhoneResult;
import com.dsgly.bixin.net.responseResult.MainPageDataResult;
import com.dsgly.bixin.net.responseResult.UserInfo;
import com.dsgly.bixin.utils.UCUtils;
import com.dsgly.bixin.wigets.PicGridView;
import com.dsgly.bixin.wigets.PicGridViewAdapter;
import com.dsgly.bixin.wigets.ScaledImageView;
import com.dsgly.bixin.wigets.WrapHeightLayoutManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 个人主页
 * Created by dengxuan on 2017/8/1.
 */

public class SelfMainPageActivity extends BaseActivity implements MainPageAdapter.ViewClickedListener, PicGridViewAdapter.OnItemChooseListener {
    private static final int REQUEST_CODE_FOR_SELECT_PIC = 0x1001;

    private UserInfo localUserInfo;

    private ScaledImageView videoPicIv;//
    private ImageView startVideoIv;//
    private ImageView avatarIv;
    private TextView userName;
    private TextView userProfileTv;
    private TextView selfIntroduceTv;
    private TextView likeManTypeTv;
    private TextView likeManTypeDescTv;
    private PicGridView photosView;
    private RecyclerView videosView;

    private List<MainPageDataResult.MomentData> mMainPageDataList;
    public List<GalleryResult.GalleryInfo> galleryInfoList;
    private MainPageAdapter mMainPageAdapter;
    private PicGridViewAdapter mPicGridViewAdapter;

    public static void startSelfMainPageActivity(Context context){
        if(context != null) {
            Intent intent = new Intent();
            intent.setClass(context, SelfMainPageActivity.class);
            context.startActivity(intent);
        }
    }

    @Override
    public void initViews() {
        setContentView(R.layout.self_main_page_activity);
        videoPicIv = (ScaledImageView) findViewById(R.id.main_page_video_pic);
        startVideoIv = (ImageView) findViewById(R.id.start_video);
        startVideoIv.setOnClickListener(this);
        avatarIv = (ImageView) findViewById(R.id.user_avatar);
        userName = (TextView) findViewById(R.id.user_name);
        userProfileTv = (TextView) findViewById(R.id.user_profiles);
        selfIntroduceTv = (TextView) findViewById(R.id.selfIntroduceTv);
        likeManTypeTv = (TextView) findViewById(R.id.like_boy_type);
        likeManTypeDescTv = (TextView) findViewById(R.id.like_boy_type_desc);
        photosView = (PicGridView) findViewById(R.id.photos);
        videosView = (RecyclerView) findViewById(R.id.videos);
        LinearLayoutManager manager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        videosView.setLayoutManager(manager1);
        photosView.setLayoutManager(new WrapHeightLayoutManager(this, 3));
    }

    @Override
    public void onClick(View v) {
        if(v.equals(startVideoIv)){
            if(localUserInfo!=null){
                VideoPlayActivity.startVideoPlay(this,localUserInfo.showVideo);
            }
        }
    }

    @Override
    public void initData() {
        localUserInfo = UCUtils.getInstance().getUserInfo();
        if(localUserInfo == null){
            return;
        }
        Glide.with(this).load(localUserInfo.headImgUrl).into(avatarIv);
        Glide.with(this).load(localUserInfo.showVideo).into(videoPicIv);
        userName.setText(localUserInfo.nickName);
        userProfileTv.setText(localUserInfo.birthYear+"年"+localUserInfo.constellation+" | "+localUserInfo.college);
        selfIntroduceTv.setText(localUserInfo.description);
        String likeType = "1".equals(localUserInfo.gender)?"女生":"男生";
        likeManTypeTv.setText("喜欢的" + likeType);
        likeManTypeDescTv.setText(localUserInfo.idealPartnerDescription);
        mMainPageDataList = new ArrayList<MainPageDataResult.MomentData>();
        mMainPageAdapter = new MainPageAdapter(mMainPageDataList,this);
        mMainPageAdapter.setOnViewClickedListener(this);
        videosView.setAdapter(mMainPageAdapter);
        galleryInfoList = new ArrayList<GalleryResult.GalleryInfo>();
        //galleryInfoList.add(new GalleryResult.GalleryInfo("add"));
        mPicGridViewAdapter = new PicGridViewAdapter(galleryInfoList,this);
        mPicGridViewAdapter.setItemChooseListener(this);
        photosView.setAdapter(mPicGridViewAdapter);
        getSelfMoment();
        getGalleryList();
    }

    private void getSelfMoment(){
        if(localUserInfo == null){
            return;
        }
        NetworkParam param = new NetworkParam(this);
        param.key = NetServiceMap.GetUserMoments;
        RequestUtils.startGetRequestExt(param,localUserInfo.userId);
    }

    private void getGalleryList(){
        if(localUserInfo == null){
            return;
        }
        NetworkParam param = new NetworkParam(this);
        param.key = NetServiceMap.GetUserGallery;
        param.block = false;
        //TODO  RequestUtils.startGetRequestExt(param,localUserInfo.userId);
        RequestUtils.startGetRequestExt(param,"1");// mock api
    }

    @Override
    public void onMsgSearchComplete(NetworkParam param) {
        super.onMsgSearchComplete(param);
        if(param == null){
            return;
        }
        if(param.key == NetServiceMap.GetUserMoments){
            if(param.baseResult instanceof MainPageDataResult) {
                MainPageDataResult result = (MainPageDataResult) param.baseResult;
                mMainPageDataList = result.data;
                mMainPageAdapter.setMainPageDataList(mMainPageDataList);
                mMainPageAdapter.notifyDataSetChanged();
            }
        }else if(param.key == NetServiceMap.GetUserGallery){
            if(param.baseResult != null && param.baseResult instanceof GalleryResult){
                GalleryResult result = (GalleryResult) param.baseResult;
                galleryInfoList = result.data;
                galleryInfoList.add(new GalleryResult.GalleryInfo("add"));
                mPicGridViewAdapter.setDataList(galleryInfoList);
                mPicGridViewAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onImageViewClicked(MainPageDataResult.MomentData result) {
        if(result!=null && !TextUtils.isEmpty(result.videoPathOrigin)){
            VideoPlayActivity.startVideoPlay(this,result.videoPathOrigin);
        }
    }

    @Override
    public void onNameClicked(MainPageDataResult.MomentData result) {

    }

    @Override
    public void onStarClicked(MainPageDataResult.MomentData result) {

    }

    @Override
    public void onCommentClicked(MainPageDataResult.MomentData result) {

    }

    @Override
    public void onWholeItemClicked(MainPageDataResult.MomentData result) {

    }

    @Override
    public void onItemChoosed(GalleryResult.GalleryInfo data) {
        if(data == null){
            return;
        }
        if("add".equals(data.pic)){
            MultiImageSelector.create(this)
                    .start(this, REQUEST_CODE_FOR_SELECT_PIC);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_FOR_SELECT_PIC){
            if(resultCode == RESULT_OK){
                ArrayList<String> stringArrayListExtra = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                for(String s : stringArrayListExtra){
                    Log.i("dx",s);
                    uploadPic(s);
                }
            }
        }
    }

    private void uploadPic(String imagePath){
        String api = NetServiceMap.UploadUserGallery.getHostPath()+NetServiceMap.UploadUserGallery.getApi();
        RequestUtils.uploadFile(api, imagePath, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("dxup",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("dxup",response.body().string());
            }
        });
    }

}
