package com.dsgly.bixin.biz.view.presenter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.DatePicker;

import com.alibaba.fastjsonex.JSON;
import com.alibaba.fastjsonex.JSONObject;
import com.alibaba.fastjsonex.serializer.SerializerFeature;
import com.dsgly.bixin.biz.base.BasePresenter;
import com.dsgly.bixin.biz.view.CompleteProfileActivity;
import com.dsgly.bixin.biz.view.CompleteProfileUploadVideoActivity;
import com.dsgly.bixin.net.NetServiceMap;
import com.dsgly.bixin.net.NetworkParam;
import com.dsgly.bixin.net.RequestUtils;
import com.dsgly.bixin.net.requestParam.UpdateUserParam;
import com.dsgly.bixin.net.responseResult.UserInfo;
import com.dsgly.bixin.utils.UCUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;


/**
 * Created by dengxuan on 2017/8/5.
 */

public class CompleteProfilePresenter extends BasePresenter<CompleteProfileActivity> {
    public void showDatePicker(){
        DatePickerDialog dialog = new DatePickerDialog(mvpView, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dateFormat = "%s-%s-%s";
                mvpView.mDateTextView.setText(String.format(dateFormat,String.valueOf(year),String.valueOf(month),String.valueOf(dayOfMonth)));
            }
        }, 2011, 1, 1);
        dialog.show();
    }

    public void showHeightPicker(){
        String heightChoice[] = new String[40];
        for(int i=0;i<40;i++){
            heightChoice[i] = String.valueOf(150+i) + " cm";
        }
        final String heights[] = heightChoice;
        AlertDialog.Builder builder = new AlertDialog.Builder(mvpView);
        //设置标题
        builder.setTitle("请选择");
        builder.setItems(heightChoice, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mvpView.mHeightTextView.setText(heights[i]);
            }
        });
        builder.create();
        builder.show();
    }

    public void onMsgSearchComplete(NetworkParam param) {
        if(param == null){
            return;
        }
        if(param.key == NetServiceMap.UpdateUSER){
            if(param.baseResult!=null && "200".equals(param.baseResult.code)){
                CompleteProfileUploadVideoActivity.startCompleteProfileUploadVideoActivity(mvpView,CompleteProfileActivity.REQUEST_CODE_FOR_COMPLETE_VIDEO_AND_PHOTO);
            }else {
                mvpView.showToast(param.baseResult == null?"更新失败":param.baseResult.msg);
            }
        }
    }

    public void choosePic(){
        Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        mvpView.startActivityForResult(albumIntent, CompleteProfileActivity.REQUEST_ALBUM_CODE);
    }

    public void updateUserInfo(){
        UserInfo userInfo = UCUtils.getInstance().getUserInfo();
        userInfo.height = mvpView.mHeightTextView.getText().toString();
        userInfo.college = mvpView.mSchoolView.getText().toString();
        userInfo.description = mvpView.mDescView.getText().toString();
        userInfo.idealPartnerDescription = mvpView.mIdealPartnerView.getText().toString();

        UpdateUserParam updateUserParam = new UpdateUserParam(userInfo);

        //SerializerFeature[] featureArr = { SerializerFeature.WriteClassName };
        String userInfoStr = JSON.toJSONString(updateUserParam);
        Log.i("dx",userInfoStr);
        NetworkParam param = new NetworkParam(mvpView);
        param.key = NetServiceMap.UpdateUSER;
        String strUTF8 = null;
        try {
            strUTF8 = URLEncoder.encode(userInfoStr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String paramStr = "?meId=100920&targetUserId=&userStr="+strUTF8;
        RequestUtils.startGetRequestExt(param,paramStr);
    }
}
