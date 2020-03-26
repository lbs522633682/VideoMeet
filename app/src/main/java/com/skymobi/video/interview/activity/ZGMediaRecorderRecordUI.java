package com.skymobi.video.interview.activity;

import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bytemelody.video.R;
import com.bytemelody.video.helper.ZGBaseHelper;
import com.bytemelody.video.helper.ZGManager;
import com.bytemelody.video.utils.AppLogger;
import com.bytemelody.video.utils.ZegoUtil;
import com.bytemelody.video.widgets.TitleLayout;
import com.skymobi.video.framework.base.BaseUIActivity;
import com.skymobi.video.framework.utils.PLog;
import com.zego.zegoavkit2.mediarecorder.IZegoMediaRecordCallback;
import com.zego.zegoavkit2.mediarecorder.ZegoMediaRecordChannelIndex;
import com.zego.zegoavkit2.mediarecorder.ZegoMediaRecordFormat;
import com.zego.zegoavkit2.mediarecorder.ZegoMediaRecordType;
import com.zego.zegoavkit2.mediarecorder.ZegoMediaRecorder;
import com.zego.zegoavkit2.utils.ZegoLogUtil;
import com.zego.zegoliveroom.callback.IZegoInitSDKCompletionCallback;
import com.zego.zegoliveroom.constants.ZegoVideoViewMode;

import java.util.List;

/**
 * Author:boshuai.li
 * Time:2020/3/17   11:42
 * Description: 录制视频的act
 */
public class ZGMediaRecorderRecordUI extends BaseUIActivity implements View.OnClickListener {

    // 录制类
    private ZegoMediaRecorder mMediaRecorder = null;
    // 文件录制格式
    private ZegoMediaRecordFormat mRecordFormat = ZegoMediaRecordFormat.FLV;
    // 文件录制类型
    private ZegoMediaRecordType mRecordType = ZegoMediaRecordType.BOTH;
    // 预设置文件存储路径
    private String mRecordingPath = "";
    // 录制回调返回的存储路径
    private String mSavePath = "";
    // 文件存储路径目录
    private String mDirPath = "";

    // 是否成功初始化 SDK
    private boolean isInitSDKSuccess = false;
    private Button go_back;
    private TitleLayout title;
    private TextureView pre_view;
    private Spinner sp_format;
    private RelativeLayout format;
    private Spinner sp_record_type;
    private RelativeLayout type;
    private Button record_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_avrecording);
        initView();

        // 创建录制类
        mMediaRecorder = new ZegoMediaRecorder();

        // 录制文件存储路径目录
        mDirPath = this.getExternalCacheDir().getPath();

        // 步骤 1 初始化 SDK
        ZGBaseHelper.sharedInstance().initZegoSDK(ZegoUtil.getAppID(), ZegoUtil.getAppSign(), ZegoUtil.getIsTestEnv(), new IZegoInitSDKCompletionCallback() {
            @Override
            public void onInitSDK(int errorcode) {
                PLog.i("onInitSDK errorcode = " + errorcode);
                if (0 == errorcode) {
                    isInitSDKSuccess = true;

                    // 步骤 2 设置媒体录制回调监听
                    setMediaRecorderCallback();

                    ZGManager.sharedInstance().api().setFrontCam(false);

                    // 此处的启动预览是为了进入录制模块就是预览效果
                    ZGManager.sharedInstance().api().setPreviewView(pre_view);
                    ZGManager.sharedInstance().api().setPreviewViewMode(ZegoVideoViewMode.ScaleAspectFill);
                    ZGManager.sharedInstance().api().enableCamera(true);
                    ZGManager.sharedInstance().api().startPreview();
                } else {
                    Toast.makeText(ZGMediaRecorderRecordUI.this, "初始化失败：" + errorcode, Toast.LENGTH_LONG).show();
                    AppLogger.getInstance().e(ZGMediaRecorderRecordUI.class, "初始化失败，errorcode： %d", errorcode);
                }
            }
        });

    }


    // 获取设置的录制参数
    public void getChoosedRecordingParam() {
        // 获取录制格式
        switch (sp_format.getSelectedItemPosition()) {
            case 0:
                mRecordFormat = ZegoMediaRecordFormat.FLV;
                break;
            case 1:
                mRecordFormat = ZegoMediaRecordFormat.MP4;
                break;
            default:
                break;
        }

        // 获取录制类型
        switch (sp_record_type.getSelectedItemPosition()) {
            case 0:
                mRecordType = ZegoMediaRecordType.BOTH; // 录制音视频
                break;
            case 1:
                mRecordType = ZegoMediaRecordType.VIDEO; // 只录制视频
                break;
            case 2:
                mRecordType = ZegoMediaRecordType.AUDIO; // 只录制音频
                break;
            default:
                break;
        }
    }

    // 录制处理
    private void dealRecording() {

        if (record_btn.getText().toString().equals(getString(R.string.tx_start_record))) {

            if (isInitSDKSuccess) {
                // 获取录制配置参数
                getChoosedRecordingParam();

                // 步骤 3 录制之前需要启动麦克风、摄像头即启动预览
                ZGBaseHelper.sharedInstance().getZegoLiveRoom().setPreviewView(pre_view);
                ZGBaseHelper.sharedInstance().getZegoLiveRoom().setPreviewViewMode(ZegoVideoViewMode.ScaleAspectFill);
                ZGBaseHelper.sharedInstance().getZegoLiveRoom().startPreview();

                // 设置文件存储路径
                mRecordingPath = mDirPath + "/" + generateAVFileName();

                PLog.i("dealRecording mRecordingPath =" + mRecordingPath);
                // 步骤 4 启动录制
                // 必须在初始化 SDK 之后调用
                /**
                 * 启动录制的相关配置如下：
                 * 启用每3秒触发录制信息更新回调（enableStatusCallback=true, interval=3000）
                 * 采用主通道进行录制，如果需要录制辅通道，采用 ZegoMediaRecordChannelIndex.AUX
                 * 对录制文件进行分片（isFragment=true），能保证录制发生异常中断等问题时，已保存的录制文件可以正常播放，这儿的分片仅是 SDK 内部处理录制文件的一个逻辑概念，不是将录制文件按间隔时间分成多个小文件存储
                 */
                mMediaRecorder.startRecord(ZegoMediaRecordChannelIndex.MAIN, mRecordType, mRecordingPath,
                        true, 3000, mRecordFormat, true);
            } else {
                Toast.makeText(ZGMediaRecorderRecordUI.this, "初始化失败，不能启动录制", Toast.LENGTH_LONG).show();
            }
        } else {

            // 停止预览
            ZGBaseHelper.sharedInstance().getZegoLiveRoom().stopPreview();
            ZGBaseHelper.sharedInstance().getZegoLiveRoom().setPreviewView(null);


            // 步骤 5 停止录制
            // 停止录制的录制通道需要与启动录制时填写的录制通道一致
            boolean ret = mMediaRecorder.stopRecord(ZegoMediaRecordChannelIndex.MAIN);

            if (ret) {
                // 修改界面标识
                record_btn.setText(getString(R.string.tx_start_record));

                // 跳转到播放页面
                ZGMediaRecorderReplayUI.actionStart(ZGMediaRecorderRecordUI.this, mSavePath);
            } else {
                Toast.makeText(ZGMediaRecorderRecordUI.this, "停止录制失败", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();

        // 置空录制回调监听
        mMediaRecorder.setZegoMediaRecordCallback(null);
        // 销毁媒体录制类
        mMediaRecorder = null;

        // 释放 SDK
        ZGBaseHelper.sharedInstance().unInitZegoSDK();
    }

    // 生成录制文件名
    public String generateAVFileName() {
        String fileName;
        // 设置视频格式
        if (mRecordFormat == ZegoMediaRecordFormat.FLV) {
            fileName = "zgmedia_" + System.currentTimeMillis() + ".flv";
        } else {
            fileName = "zgmedia_" + System.currentTimeMillis() + ".mp4";
        }

        return fileName;
    }

    // 设置录制回调监听
    public void setMediaRecorderCallback() {

        mMediaRecorder.setZegoMediaRecordCallback(new IZegoMediaRecordCallback() {
            /**
             * 媒体录制回调
             * @param errCode 错误码，0-录制成功，1：存储路径太长，2：初始化 avcontext 失败，3：打开文件失败，4：写文件失败
             * @param zegoMediaRecordChannelIndex  录制通道
             * @param storagePath 录制文件存储路径
             */
            @Override
            public void onMediaRecord(int errCode, ZegoMediaRecordChannelIndex zegoMediaRecordChannelIndex, String storagePath) {

                if (errCode == 0) {
                    mSavePath = storagePath;
                    //更新界面按钮标识
                    record_btn.setText(getString(R.string.tx_stop_record));
                } else {
                    Toast.makeText(ZGMediaRecorderRecordUI.this, "启动录制失败，err: " + errCode, Toast.LENGTH_LONG);
                    AppLogger.getInstance().e(ZGMediaRecorderRecordUI.class, "启动录制失败， err: %d", errCode);
                }
            }

            /**
             * 媒体录制信息更新回调
             * @param zegoMediaRecordChannelIndex 录制通道
             * @param storagePath 录制文件存储路径
             * @param duration  录制时长，单位：ms
             * @param fileSize  文件大小，单位：字节
             * @apiNote 此回调的触发频率由 startRecord() 中的 interval 参数值决定，此 demo 采用每 3s 触发
             */
            @Override
            public void onRecordStatusUpdate(ZegoMediaRecordChannelIndex zegoMediaRecordChannelIndex, String storagePath, long duration, long fileSize) {

            }
        });
    }

    private void initView() {

        requestPermiss();

        go_back = (Button) findViewById(R.id.go_back);
        title = (TitleLayout) findViewById(R.id.title);
        pre_view = (TextureView) findViewById(R.id.pre_view);
        sp_format = (Spinner) findViewById(R.id.sp_format);
        format = (RelativeLayout) findViewById(R.id.format);
        sp_record_type = (Spinner) findViewById(R.id.sp_record_type);
        type = (RelativeLayout) findViewById(R.id.type);
        record_btn = (Button) findViewById(R.id.record_btn);

        go_back.setOnClickListener(this);
        record_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.go_back) {
            finish();
        } else if (id == R.id.record_btn) {
            dealRecording();
        }
    }

    /**
     * 请求权限
     */
    private void requestPermiss() {
        //危险权限
        request(new OnPermissionsResult() {
            @Override
            public void OnSuccess() {
                PLog.i("OnPermissionsResult OnSuccess");
            }

            @Override
            public void OnFail(List<String> noPermissions) {
                PLog.i("OnPermissionsResult OnFail");
            }

        });
    }
}

