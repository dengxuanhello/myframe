package com.dsgly.bixin.biz.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.dsgly.bixin.biz.base.BaseActivity;

/**
 * Created by dengxuan on 17-7-24.
 */

public class VideoPlayActivity extends BaseActivity {

    private static final String PLAY_URL = "play_url";
    public static final String TEST_RUL = "http://r8---sn-oguesn7y.c.youtube.com/videoplayback?id=604ed5ce52eda7ee&itag=22&source=youtube&sparams=expire,id,ip,ipbits,mm,mn,ms,mv,nh,pl,source&ip=202.32.147.10&ipbits=0&expire=19000000000&signature=2742A45C170667515836A9FDD75659086F71D917.3D65834800082AC81319F720B62A91E56BD8B4C3&key=cms1&cms_redirect=yes&mm=31&mn=sn-oguesn7y&ms=au&mt=1456997821&mv=m&nh=IgpwcjAyLm5ydDEwKgkxMjcuMC4wLjE&pl=23";


    private String playUrl;
    private SimpleExoPlayerView playerView;
    private SimpleExoPlayer simpleExoPlayer;

    @Override
    public void initViews() {
        super.initViews();
        playerView = new SimpleExoPlayerView(this);
        setContentView(playerView);
    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        if(intent!=null && intent.getExtras()!=null){
            playUrl = intent.getExtras().getString(PLAY_URL);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPlayer();
    }

    public static void startVideoPlay(Context context, String url){
        if(context == null || TextUtils.isEmpty(url)){
            return;
        }
        Intent intent = new Intent();
        intent.setClass(context,VideoPlayActivity.class);
        intent.putExtra(PLAY_URL,url);
        context.startActivity(intent);
    }

    private void initPlayer(){
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "bixin"), defaultBandwidthMeter);
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource(Uri.parse(playUrl), dataSourceFactory, extractorsFactory, null, null);
        // SimpleExoPlayer
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        // Prepare the player with the source.
        simpleExoPlayer.prepare(videoSource);
        simpleExoPlayer.setPlayWhenReady(true);
        playerView.setPlayer(simpleExoPlayer);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(simpleExoPlayer!=null) {
            simpleExoPlayer.release();
        }
    }
}
