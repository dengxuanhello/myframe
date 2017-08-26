package com.dsgly.bixin.biz.view;

import android.Manifest;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dsgly.bixin.R;
import com.netease.svsdk.biz.BaseActivity;
import com.netease.svsdk.biz.VideoCutActivity;
import com.netease.svsdk.biz.adapter.VideoPickListener;
import com.netease.svsdk.constants.RequestCode;
import com.netease.svsdk.entity.LocalVideoInfo;
import com.netease.svsdk.tools.AfterPermissionGranted;
import com.netease.svsdk.tools.Permissions;
import com.netease.svsdk.tools.ViewUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImportVideoActivity extends BaseActivity implements VideoPickListener, MediaPlayer.OnPreparedListener, SurfaceHolder.Callback {

    private GridView mGridView;
    private ArrayList<LocalVideoInfo> mAllVideoData = new ArrayList<>();
    private VideoGridAdapter mImageAdapter;
    private static final int CURSOR_LOADER = 1;
    private int mImageSize;
    private SurfaceView surfaceView;
    private MediaPlayer mediaPlayer;
    private String videoPlayUrl;
    private static final String[] STORE_IMAGES = {
            MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media._ID, MediaStore.Video.Media.DATA, MediaStore.Video.Media.DURATION
    };

    public static void startPickVideosActivity(Context context,int requestCode){
        if(context != null) {
            Intent intent = new Intent();
            intent.setClass(context, ImportVideoActivity.class);
            if(context instanceof Activity){
                ((Activity)context).startActivityForResult(intent,requestCode);
            }else {
                context.startActivity(intent);
            }
        }
    }

    @Override
    protected void initViews() {
        super.initViews();
        setContentView(R.layout.pick_video_activity);
        mGridView = (GridView) findViewById(R.id.svsdk_select_video_list);
        surfaceView = (SurfaceView) findViewById(R.id.player_surface);
        surfaceView.getHolder().addCallback(this);
        findViewById(R.id.svsdk_ic_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mImageAdapter = new VideoGridAdapter(this);
        mImageAdapter.setVideoPickListener(this);
        mGridView.setAdapter(mImageAdapter);
        findViewById(R.id.svsdk_activity_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(videoPlayUrl)){
                    showToast("请选择视频");
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putString("cut_video_source_path",videoPlayUrl);
                    VideoCutActivity.startVideoCutActivity(ImportVideoActivity.this,bundle,RequestCode.REQ_FOR_CUT_VIDEO);
                }
            }
        });
        if(Permissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            startCursorLoader(CURSOR_LOADER, null);
        }else {
            Permissions.requestPermissions(this, "", RequestCode.REQ_FOR_READ_EXT_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private void play(String sourcePath){
        if(TextUtils.isEmpty(sourcePath)){
            return;
        }
        if(mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        try {
            mediaPlayer.reset();
            if(!TextUtils.isEmpty(sourcePath)) {
                mediaPlayer.setDataSource(sourcePath);
                mediaPlayer.setDisplay(surfaceView.getHolder());
                mediaPlayer.prepare();
                mediaPlayer.setLooping(true);
                mediaPlayer.setOnPreparedListener(this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterPermissionGranted(RequestCode.REQ_FOR_READ_EXT_STORAGE)
    public void startCursorLoader(int id, Bundle args) {
        Loader<Cursor> loader = getLoaderManager().getLoader(id);
        if (loader == null) {
            getLoaderManager().initLoader(id, args, mCallBacks);
        } else {
            getLoaderManager().restartLoader(id, args, mCallBacks);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getLoaderManager().destroyLoader(CURSOR_LOADER);
    }

    protected LoaderManager.LoaderCallbacks<Cursor> mCallBacks = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            String order = MediaStore.Video.Media.DATE_ADDED + " desc";
            CursorLoader cursorLoader = new CursorLoader(ImportVideoActivity.this, MediaStore.Video.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES, null, null, order);
            return cursorLoader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            if (cursor == null) {
                return;
            }
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                LocalVideoInfo bean = new LocalVideoInfo();
                String path=cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                long duration=cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                bean.videoPath = path;
                bean.videoDuration = duration;
                File file = new File(bean.videoPath);
                if (!file.exists()||duration<=0)
                    continue;
                mAllVideoData.add(bean);
            }
            mImageAdapter.notifyDataSetChanged();
            getLoaderManager().destroyLoader(CURSOR_LOADER);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    private int getPhotoSize() {
        if (mImageSize != 0) {
            return mImageSize;
        }
        if (mGridView == null) {
            return -1;
        }
        Context context = mGridView.getContext();
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mGridView.getLayoutParams();
        int contentWidth = ViewUtil.getScreenWidth(context) - params.leftMargin - params.rightMargin - mGridView.getPaddingLeft()
                - mGridView.getPaddingRight();
        int numCol = 4;
        int hSpace = ViewUtil.dip2px(context, 0);
        mImageSize = (contentWidth - (numCol - 1) * hSpace) / numCol;
        return mImageSize;
    }

    @Override
    public void onVideoPicked(LocalVideoInfo info) {
        if(info == null){return;}
        /*if(jugdeLen(info.videoDuration)){
            *//*Bundle bundle = new Bundle();
            bundle.putString(StringValues.CUT_VIDEO_SOURCE_PATH,info.videoPath);*//*

            //VideoCutActivity.startVideoCutActivity(this,bundle, RequestCode.REQ_FOR_CUT_VIDEO);
        }*/
        videoPlayUrl = info.videoPath;
        play(videoPlayUrl);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if(mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        play(videoPlayUrl);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
    }

    private class VideoGridAdapter extends BaseAdapter {

        private Context mContext;
        private VideoPickListener videoPickListener;
        int size= getPhotoSize();

        public VideoGridAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return mAllVideoData == null ? 0 : mAllVideoData.size();
        }

        @Override
        public Object getItem(int position) {
            if(mAllVideoData != null)
                return mAllVideoData.get(position);
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(ImportVideoActivity.this).inflate(R.layout.pick_video_item_layout, null);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView.findViewById(R.id.svsdk_video_item_image);
                holder.image.setLayoutParams(new RelativeLayout.LayoutParams(size, size));//之前没有size 并且也没有imageviewaware的设定也可以？
                holder.mask = convertView.findViewById(R.id.svsdk_half_transparent_layer);
                holder.mask.setLayoutParams(new RelativeLayout.LayoutParams(size, size));
                holder.time = (TextView) convertView.findViewById(R.id.svsdk_video_time_len);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final LocalVideoInfo bean = mAllVideoData.get(position);
            String path = bean.videoPath;
            if (jugdeLen(bean.videoDuration)) {
                holder.mask.setVisibility(View.GONE);
            } else {
                holder.mask.setVisibility(View.VISIBLE);
            }
            String mediaTime= ViewUtil.parseTimeToString(bean.videoDuration);
            holder.time.setText(mediaTime);
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(videoPickListener != null){
                        videoPickListener.onVideoPicked(bean);
                    }
                }
            });
            Glide.with(mContext).load(path).centerCrop().into(holder.image);
            return convertView;
        }

        public void setVideoPickListener(VideoPickListener videoPickListener) {
            this.videoPickListener = videoPickListener;
        }
    }

    private class ViewHolder {
        public ImageView image;
        public View mask;
        public TextView time;
    }



    private Boolean jugdeLen(long time){
        long second = time / 1000;
        if(second>=10 && second<=600){
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RequestCode.REQ_FOR_CUT_VIDEO){
            if(resultCode == RESULT_OK){
                String videoPath = data.getStringExtra("cutVideo");
                Log.i("dx",videoPath);
                PublishMomentActivity.startPublishMomentActivity(this,videoPath);
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
