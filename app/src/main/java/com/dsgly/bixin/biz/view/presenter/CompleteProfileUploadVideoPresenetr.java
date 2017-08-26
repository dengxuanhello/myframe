package com.dsgly.bixin.biz.view.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.dsgly.bixin.biz.base.BasePresenter;
import com.dsgly.bixin.biz.view.CompleteProfileUploadVideoActivity;
import com.dsgly.bixin.net.NetServiceMap;
import com.dsgly.bixin.net.RequestUtils;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by dengxuan on 2017/8/7.
 */

public class CompleteProfileUploadVideoPresenetr extends BasePresenter<CompleteProfileUploadVideoActivity> {

    public void setUpExoPlayer(Context context){
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context, "Bixin"), defaultBandwidthMeter);
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        // This is the MediaSource representing the media to be played.
        //MediaSource videoSource = new ExtractorMediaSource(Uri.parse(videoUrl), dataSourceFactory, extractorsFactory, null, null);
        // SimpleExoPlayer
        mvpView.simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
        // Prepare the player with the source.
        //simpleExoPlayer.prepare(videoSource);
        //player.setVideoSurface(surface);
        //player.setPlayWhenReady(true);
    }

    public void prepareDataSource(String videoSourceStr){
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mvpView, Util.getUserAgent(mvpView, "Bixin"), defaultBandwidthMeter);
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource(Uri.parse(videoSourceStr), dataSourceFactory, extractorsFactory, null, null);
        mvpView.simpleExoPlayer.prepare(videoSource);
        mvpView.simpleExoPlayer.setPlayWhenReady(true);
    }

    public void uploadVideo(String videoPath){
        /*RequestUtils.uploadFile(NetServiceMap.UploadVideo.getHostPath() + NetServiceMap.SendMoment.getApi(),
                videoPath, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("dxvideo",e.toString());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.isSuccessful()) {
                            Log.i("dxvideo", response.body().string());
                        }else {
                            Log.i("dxvideo", "shibai");
                        }
                    }
                });*/
    }

    public void choosePic(){
        Intent intent = new Intent();
                /* 开启Pictures画面Type设定为image */
        intent.setType("image/*");
                /* 使用Intent.ACTION_GET_CONTENT这个Action */
        intent.setAction(Intent.ACTION_GET_CONTENT);
                /* 取得相片后返回本画面 */
        mvpView.startActivityForResult(intent, CompleteProfileUploadVideoActivity.REQUEST_CODE_FOR_PIC);
    }
}
