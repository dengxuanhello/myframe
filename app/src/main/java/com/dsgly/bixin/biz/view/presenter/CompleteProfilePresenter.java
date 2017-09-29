package com.dsgly.bixin.biz.view.presenter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.DatePicker;

import com.alibaba.fastjsonex.JSON;
import com.dsgly.bixin.biz.base.BasePresenter;
import com.dsgly.bixin.biz.view.CompleteProfileActivity;
import com.dsgly.bixin.biz.view.CompleteProfileUploadVideoActivity;
import com.dsgly.bixin.net.NetServiceMap;
import com.dsgly.bixin.net.NetworkParam;
import com.dsgly.bixin.net.RequestUtils;
import com.dsgly.bixin.net.requestParam.UpdateUserParam;
import com.dsgly.bixin.utils.UCUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by dengxuan on 2017/8/5.
 */

public class CompleteProfilePresenter extends BasePresenter<CompleteProfileActivity> {

    private boolean isNext;

    public void showDatePicker(){
        DatePickerDialog dialog = new DatePickerDialog(mvpView, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                String dateFormat = "%s-%s-%s";
                mvpView.mDateTextView.setText(String.format(dateFormat,String.valueOf(year),String.valueOf(month + 1),String.valueOf(dayOfMonth)));

                updateBirth(year, month + 1, dayOfMonth);

            }
        }, 1990, 0, 1);
        dialog.show();
    }

    public void showHeightPicker(){
        String heightChoice[] = new String[40];
        for(int i=0;i<40;i++){
            heightChoice[i] = String.valueOf(150+i);
        }
        final String heights[] = heightChoice;
        AlertDialog.Builder builder = new AlertDialog.Builder(mvpView);
        //设置标题
        builder.setTitle("请选择");
        builder.setItems(heightChoice, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mvpView.mHeightTextView.setText(heights[i]);
                updateHeight(Integer.parseInt(heights[i]));
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
                if (isNext)
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

    public void updateNickName(String nickName) {

    }

    public void updateGender(int gender) {

        Map<String, Integer> map = new HashMap<>();
        map.put("gender", gender);
        updateUserInfo(JSON.toJSONString(map));
    }

    public void updateHeight(int height) {

        Map<String, Integer> map = new HashMap<>();
        map.put("height", height);
        updateUserInfo(JSON.toJSONString(map));
    }

    public void updateBirth(int year, int month, int dayOfMonth) {

        Map<String, Integer> map = new HashMap<>();
        map.put("birthYear", year);
        map.put("birthMonth", month);
        map.put("birthDay", dayOfMonth);
        updateUserInfo(JSON.toJSONString(map));
    }

    public void updateCollege(String college) {

    }

    public void updateAvatar(String avatar) {

    }

    public void updateOther(String nickName, String college, String description, String idealPartnerDescription) {
        Map<String, String> map = new HashMap<>();
        map.put("nickName", nickName);
        map.put("college", college);
        map.put("description", description);
        map.put("idealPartnerDescription", idealPartnerDescription);
        updateUserInfo(JSON.toJSONString(map));
    }

    private void updateUserInfo(String userInfoStr) {
        isNext = userInfoStr.contains("nickName");
        UpdateUserParam updateUserParam = new UpdateUserParam();

        //SerializerFeature[] featureArr = { SerializerFeature.WriteClassName };
        Log.i("updateUserInfo", userInfoStr);
        updateUserParam.userStr = userInfoStr;

        NetworkParam param = new NetworkParam(mvpView);
        param.key = NetServiceMap.UpdateUSER;
        param.param = updateUserParam;
//        String strUTF8 = null;
//        try {
//            strUTF8 = URLEncoder.encode(userInfoStr, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        String paramStr = "?meId=" + UCUtils.meId + "&targetUserId="+UCUtils.meId;
        RequestUtils.startPostRequestExt(param,paramStr);
    }

    public void updateUserInfo(){

        UpdateUserParam.UserInfo userInfo = new UpdateUserParam.UserInfo();
        userInfo.nickName = mvpView.mNicknameEt.getText().toString();
        userInfo.college = mvpView.mSchoolView.getText().toString();
        if (mvpView.mSchoolView.getTag() != null) {
            userInfo.collegeId = mvpView.mSchoolView.getTag().toString();
        }
        userInfo.description = mvpView.mDescView.getText().toString();
        userInfo.idealPartnerDescription = mvpView.mIdealPartnerView.getText().toString();

        updateUserInfo(JSON.toJSONString(userInfo));
    }

    public void showGenderPicker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mvpView);
        //设置标题
        builder.setTitle("请选择");
        final String[] genders = new String[]{"男", "女"};
        builder.setItems(genders, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mvpView.mGenderTv.setText(genders[i]);
                updateGender(i + 1);
            }
        });
        builder.create();
        builder.show();
    }
}
