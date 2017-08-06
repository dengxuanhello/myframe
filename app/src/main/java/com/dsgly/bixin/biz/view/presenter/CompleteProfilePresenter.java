package com.dsgly.bixin.biz.view.presenter;

import android.app.DatePickerDialog;
import android.widget.DatePicker;

import com.dsgly.bixin.biz.base.BasePresenter;
import com.dsgly.bixin.biz.view.CompleteProfileActivity;

/**
 * Created by dengxuan on 2017/8/5.
 */

public class CompleteProfilePresenter extends BasePresenter<CompleteProfileActivity> {
    public void showDataPicker(){
        DatePickerDialog dialog = new DatePickerDialog(mvpView, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dateFormat = "%s-%s-%s";
                mvpView.mDateTextView.setText(String.format(dateFormat,year,month,dateFormat));
            }
        }, 2011, 1, 1);
        dialog.show();
    }
}
