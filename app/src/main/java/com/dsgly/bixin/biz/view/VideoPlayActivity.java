package com.dsgly.bixin.biz.view;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.SurfaceView;
import android.view.ViewGroup;

import com.dsgly.bixin.R;
import com.dsgly.bixin.biz.base.BaseActivity;
import com.dsgly.bixin.wigets.MediaController;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.TextRenderer;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

/**
 * Created by dengxuan on 17-7-24.
 */

public class VideoPlayActivity extends BaseActivity {

    private static final String PLAY_URL = "play_url";
    public static final String TEST_RUL = "http://r8---sn-oguesn7y.c.youtube.com/videoplayback?id=604ed5ce52eda7ee&itag=22&source=youtube&sparams=expire,id,ip,ipbits,mm,mn,ms,mv,nh,pl,source&ip=202.32.147.10&ipbits=0&expire=19000000000&signature=2742A45C170667515836A9FDD75659086F71D917.3D65834800082AC81319F720B62A91E56BD8B4C3&key=cms1&cms_redirect=yes&mm=31&mn=sn-oguesn7y&ms=au&mt=1456997821&mv=m&nh=IgpwcjAyLm5ydDEwKgkxMjcuMC4wLjE&pl=23";


    private String playUrl;
//    private SimpleExoPlayerView playerView;
    private AspectRatioFrameLayout playerLayout;
    private SimpleExoPlayer simpleExoPlayer;
    private MediaController controller;

    private ComponentListener componentListener;

    @Override
    public void initViews() {
        super.initViews();
//        playerView = new SimpleExoPlayerView(this);
        setContentView(R.layout.activity_video_play);
        playerLayout = (AspectRatioFrameLayout) findViewById(R.id.layout_video_play);
        controller = (MediaController) findViewById(R.id.controller_video_play);
    }

    @Override
    public void initData() {
        super.initData();
        componentListener = new ComponentListener();
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
        SurfaceView surfaceView = new SurfaceView(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        surfaceView.setLayoutParams(params);
        playerLayout.addView(surfaceView);
        playerLayout.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

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
        simpleExoPlayer.setVideoSurfaceView(surfaceView);
        simpleExoPlayer.setVideoListener(componentListener);
        simpleExoPlayer.setTextOutput(componentListener);
        simpleExoPlayer.addListener(componentListener);

        controller.setMediaPlayer(new MediaController.MediaPlayerControl() {
            @Override
            public void start() {
                simpleExoPlayer.setPlayWhenReady(true);
            }

            @Override
            public void pause() {
                simpleExoPlayer.setPlayWhenReady(false);
            }

            @Override
            public long getDuration() {
                return simpleExoPlayer.getDuration();
            }

            @Override
            public long getCurrentPosition() {
                return simpleExoPlayer.getCurrentPosition();
            }

            @Override
            public void seekTo(long pos) {
                simpleExoPlayer.seekTo(pos);
            }

            @Override
            public boolean isPlaying() {
                return simpleExoPlayer.getPlayWhenReady();
            }

            @Override
            public void manualPause(boolean paused) {

            }

            @Override
            public int getBufferPercentage() {
                return simpleExoPlayer.getBufferedPercentage();
            }

            @Override
            public boolean canPause() {
                return !simpleExoPlayer.isLoading();
            }

        });
//        playerView.setPlayer(simpleExoPlayer);
//        playerView.setControlDispatcher(new PlaybackControlView.ControlDispatcher() {
//            @Override
//            public boolean dispatchSetPlayWhenReady(ExoPlayer player, boolean playWhenReady) {
//                return false;
//            }
//
//            @Override
//            public boolean dispatchSeekTo(ExoPlayer player, int windowIndex, long positionMs) {
//                return false;
//            }
//        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        controller.updateScreenMode(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE);
    }

    @Override
    public void onBackPressed() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(simpleExoPlayer!=null) {
            simpleExoPlayer.release();
        }
    }

    private final class ComponentListener implements SimpleExoPlayer.VideoListener,
            TextRenderer.Output, ExoPlayer.EventListener {

        // TextRenderer.Output implementation

        @Override
        public void onCues(List<Cue> cues) {
//            if (subtitleView != null) {
//                subtitleView.onCues(cues);
//            }
        }

        // SimpleExoPlayer.VideoListener implementation

        @Override
        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees,
                                       float pixelWidthHeightRatio) {
            if (playerLayout != null) {
                float aspectRatio = height == 0 ? 1 : (width * pixelWidthHeightRatio) / height;
                playerLayout.setAspectRatio(aspectRatio);
            }
        }

        @Override
        public void onRenderedFirstFrame() {
//            if (shutterView != null) {
//                shutterView.setVisibility(INVISIBLE);
//            }
        }

        @Override
        public void onTracksChanged(TrackGroupArray tracks, TrackSelectionArray selections) {
//            updateForCurrentTrackSelections();
        }

        // ExoPlayer.EventListener implementation

        @Override
        public void onLoadingChanged(boolean isLoading) {
            // Do nothing.
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//            maybeShowController(false);
            if (playbackState == ExoPlayer.STATE_ENDED) {
                simpleExoPlayer.seekTo(0);
            }

        }

        @Override
        public void onPlayerError(ExoPlaybackException e) {
            // Do nothing.
        }

        @Override
        public void onPositionDiscontinuity() {
            // Do nothing.
        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            // Do nothing.
        }

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {
            // Do nothing.
        }

    }
}
