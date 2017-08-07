package com.dsgly.bixin.biz.view.presenter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.widget.DatePicker;

import com.dsgly.bixin.biz.base.BasePresenter;
import com.dsgly.bixin.biz.view.CompleteProfileActivity;


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

    public void choosePic(){
        Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        mvpView.startActivityForResult(albumIntent, CompleteProfileActivity.REQUEST_ALBUM_CODE);
    }
}
