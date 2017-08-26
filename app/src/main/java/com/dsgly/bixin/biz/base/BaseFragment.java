package com.dsgly.bixin.biz.base;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.dsgly.bixin.net.NetServiceMap;
import com.dsgly.bixin.net.NetworkListener;
import com.dsgly.bixin.net.NetworkParam;
import com.dsgly.bixin.net.RequestUtils;
import com.dsgly.bixin.wigets.ProgressHUD;
/*import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageListener;*/

/**
 * Created by dengxuan on 2017/7/2.
 */

public class BaseFragment extends Fragment implements NetworkListener/*, TIMMessageListener */{
    private ProgressHUD progressDialog;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //TIMManager.getInstance().addMessageListener(this);
    }

    @Override
    public void onNetStart(NetworkParam param) {
        showProgress(param);
    }

    @Override
    public void onNetEnd(NetworkParam param) {
        closeProgressDialog();
    }

    @Override
    public void onNetError(NetworkParam param) {
        closeProgressDialog();
    }

    @Override
    public void onNetCancel(NetworkParam param) {

    }

    @Override
    public void onMsgSearchComplete(NetworkParam param) {

    }

    private void showProgress(final NetworkParam param){
        if(getActivity() != null && param != null && !getActivity().isFinishing()) {
            progressDialog = ProgressHUD.show(getActivity(), param.progressMessage, param.cancelAble, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    onNetCancel(param);
                }
            });
        }
    }

    public void closeProgressDialog(){
        if(getActivity() != null && progressDialog != null && !getActivity().isFinishing()){
            if(progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    protected void starMoment(String momentId){
        NetworkParam networkParam = new NetworkParam(this);
        networkParam.key = NetServiceMap.STARMOMENT;
        RequestUtils.startGetRequestExt(networkParam,momentId);
    }

   /* @Override
    public boolean onNewMessages(List<TIMMessage> list) {
        return false;
    }*/
}
