package com.skymobi.video.interview.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;

import com.bytemelody.video.R;
import com.bytemelody.video.utils.AppLogger;
import com.bytemelody.video.widgets.TitleLayout;
import com.skymobi.video.framework.base.BaseUIActivity;
import com.zego.zegoavkit2.IZegoMediaPlayerCallback;
import com.zego.zegoavkit2.ZegoMediaPlayer;

/**
 * Author:boshuai.li
 * Time:2020/3/17   11:42
 * Description: 观看回放的act
 */
public class ZGMediaRecorderReplayUI extends BaseUIActivity implements View.OnClickListener {

    /* 媒体播放器 */
    private ZegoMediaPlayer zegoMediaPlayer = null;

    private String mFilePath = "";
    private Button go_back;
    private TitleLayout title;
    private TextureView play_view;
    private Button replay_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_replay);
        initView();

        // 创建播放器对象
        zegoMediaPlayer = new ZegoMediaPlayer();
        // 初始化播放器
        zegoMediaPlayer.init(ZegoMediaPlayer.PlayerTypePlayer);
        // 设置播放器回调监听
        setPlayerCallback();

        // 录制文件存储路径
        mFilePath = getIntent().getStringExtra("filePath");

    }

    @Override
    public void finish() {
        super.finish();

        // 去除播放器回调监听
        zegoMediaPlayer.setCallback(null);
        // 释放播放器
        zegoMediaPlayer.uninit();
        zegoMediaPlayer = null;
    }

    private void dealReplay() {
        if (replay_btn.getText().toString().equals(getString(R.string.tx_begin_play))) {
            if (zegoMediaPlayer != null) {
                zegoMediaPlayer.setView(play_view);
            }

            if (!mFilePath.equals("")) {
                zegoMediaPlayer.setVolume(100);
                zegoMediaPlayer.start(mFilePath, false);
            }
        } else {
            if (zegoMediaPlayer != null) {
                zegoMediaPlayer.setView(null);
                zegoMediaPlayer.stop();
            }
        }
    }

    /**
     * 供其他Activity调用，进入本专题的方法
     *
     * @param activity
     * @param filePath 录制文件路径
     */
    public static void actionStart(Activity activity, String filePath) {
        Intent intent = new Intent(activity, ZGMediaRecorderReplayUI.class);
        intent.putExtra("filePath", filePath);
        activity.startActivity(intent);
    }

    // 设置播放器回调监听
    public void setPlayerCallback() {
        zegoMediaPlayer.setCallback(new IZegoMediaPlayerCallback() {
            @Override
            public void onPlayStart() {
                replay_btn.setText(getString(R.string.tx_end_play));
            }

            @Override
            public void onPlayPause() {
                replay_btn.setText(getString(R.string.tx_begin_play));
            }

            @Override
            public void onPlayStop() {
                replay_btn.setText(getString(R.string.tx_begin_play));
            }

            @Override
            public void onPlayResume() {
                replay_btn.setText(getString(R.string.tx_end_play));
            }

            @Override
            public void onPlayError(int i) {
                AppLogger.getInstance().e(ZGMediaRecorderReplayUI.class, "回放出错，err: %d", i);

            }

            @Override
            public void onVideoBegin() {

            }

            @Override
            public void onAudioBegin() {

            }

            @Override
            public void onPlayEnd() {
                replay_btn.setText(getString(R.string.tx_begin_play));
            }

            @Override
            public void onBufferBegin() {

            }

            @Override
            public void onBufferEnd() {

            }

            @Override
            public void onSeekComplete(int i, long l) {

            }

            @Override
            public void onSnapshot(Bitmap bitmap) {

            }

            @Override
            public void onLoadComplete() {

            }

            @Override
            public void onProcessInterval(long l) {

            }
        });
    }

    private void initView() {
        go_back = (Button) findViewById(R.id.go_back);
        title = (TitleLayout) findViewById(R.id.title);
        play_view = (TextureView) findViewById(R.id.play_view);
        replay_btn = (Button) findViewById(R.id.replay_btn);

        go_back.setOnClickListener(this);
        replay_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.go_back) {
            finish();
        } else if (id == R.id.replay_btn) {
            dealReplay();
        }
    }
}
