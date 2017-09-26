package com.dsgly.bixin.biz.view.frg;

import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dsgly.bixin.R;
import com.dsgly.bixin.biz.view.adapter.ImagePreviewAdapter;
import com.dsgly.bixin.utils.AfterPermissionGranted;
import com.dsgly.bixin.utils.ImageUtil;
import com.dsgly.bixin.wigets.CommonPopupWindow;
import com.dsgly.bixin.wigets.CustomPopup;
import com.dsgly.bixin.wigets.ToastUtil;
import com.netease.svsdk.tools.Permissions;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * 左右滑动预览
 * Created by madong on 2016/12/2.
 */

public class ImagePreviewFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener, Permissions.PermissionCallbacks {

    public static final String KEY_IMAGE_LIST = "key_list";
    public static final String KEY_IMAGE_INDEX = "key_index";


    private ViewPager viewPager;
    private ImagePreviewAdapter mAdapter;
    private List<String> currentList = new ArrayList<>();
    private TextView indexTv;

    private View mRootView;
    private CommonPopupWindow popupWindow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_image_preview, container, false);
        return mRootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() == null) {
            return;
        }

        view.findViewById(R.id.bt_back).setOnClickListener(this);
        viewPager = (ViewPager) view.findViewById(R.id.vp_image_preview);
        if (getArguments() != null && getArguments().containsKey(KEY_IMAGE_LIST)) {
            //多张网络图片
            String img = getArguments().getString(KEY_IMAGE_LIST);
            currentList = Arrays.asList(img.split(","));
        }

        if (mAdapter == null) {
            mAdapter = new ImagePreviewAdapter(currentList);
            mAdapter.setOnClickListener(this);
            mAdapter.setOnLongClickListener(this);
        }
        viewPager.setAdapter(mAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (indexTv != null) {
                    indexTv.setText((position + 1) + "/" + currentList.size());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (getArguments() != null && getArguments().containsKey(KEY_IMAGE_INDEX)) {
            viewPager.setCurrentItem(getArguments().getInt(KEY_IMAGE_INDEX));
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bt_back || id == R.id.photo_view) {
            getActivity().finish();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (popupWindow == null) {
            CustomPopup.Builder builder = new CustomPopup.Builder(mRootView);
            builder.setLayoutId(R.layout.layout_popup_image_preview)
                    .setResourceId(R.id.bt_popup_action0)
                    .setText("下载")
                    .setOnItemClickListener(new CustomPopup.OnItemClickListener() {
                        @Override
                        public void onItemClick(int index) {
                            if (Permissions.hasPermissions(getActivity(), Permissions.PERMISSION_STORAGE)) {
                                downloadImage();
                            } else {
                                Permissions.requestPermissions(getActivity(), "", Permissions.REQ_CODE_STORAGE, Permissions.PERMISSION_STORAGE);
                            }
                        }
                    });
            popupWindow = builder.create();
        }
        popupWindow.show();
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(Permissions.REQ_CODE_STORAGE)
    private void downloadImage() {
        String path = currentList.get(viewPager.getCurrentItem());
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    File file = ImageUtil.fetchImage(getActivity(), params[0]);
                    if (file == null || TextUtils.isEmpty(file.getPath())) {
                        return null;
                    }
                    File targetFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/bixin");

                    if (!targetFileDir.exists() && !targetFileDir.mkdirs()) {
                        return null;
                    }
                    String fileName = "bixin_" + genImageNamePrefix() + ".jpg";
                    File targetFile = new File(targetFileDir, fileName);
                    while (targetFile.exists()) {
                        fileName = "bixin_" + genImageNamePrefix() + ".jpg";
                        targetFile = new File(targetFileDir, fileName);
                    }

                    boolean result = ImageUtil.saveBitmapFile(BitmapFactory.decodeFile(file.getAbsolutePath()), targetFile);
                    return !result ? null : targetFile.getPath();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (getActivity() == null || !isAdded()) {
                    return;
                }
                if (TextUtils.isEmpty(result)) {
                    ToastUtil.showToast(getActivity(), "下载失败");
                    return;
                }
                MediaScannerConnection.scanFile(getActivity(), new String[]{result}, null, null);

                ToastUtil.showToast(getActivity(), "成功下载到相册" + result);
            }
        }.execute(path);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
//        AlertUtil.message(getActivity(), 0, R.string.denied_write_storage_state, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//            }
//        });
    }

    private String genImageNamePrefix() {
        String dateStr = DateFormat.format("yyyyMMddhhmmss", new Date()).toString();
        String randomStr = getRandomStr(10);
        return dateStr + randomStr;
    }

    private static String getRandomStr(int length) {
        String dic = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder builder = new StringBuilder(dic);
        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        int range = builder.length();
        for (int i = 0; i < length; i++) {
            sb.append(builder.charAt(r.nextInt(range)));
        }
        return sb.toString();
    }
}
