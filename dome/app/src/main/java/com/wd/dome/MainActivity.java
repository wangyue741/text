package com.wd.dome;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import io.agora.rtc2.ChannelMediaOptions;
import io.agora.rtc2.Constants;
import io.agora.rtc2.IRtcEngineEventHandler;
import io.agora.rtc2.RtcEngine;
import io.agora.rtc2.RtcEngineConfig;
import io.agora.rtc2.video.VideoCanvas;

public class MainActivity extends AppCompatActivity {

    private FrameLayout zhi;
    private static final int PERMISSION_REQ_ID = 22;
    private String appId = "2f70757210584b5aa42ad8bb8f2a1cc9";
    private String channelName = "1";
    private String token = "007eJxTYHhwhCF+v++JFxYB521Vlvr/PCqxxC1e7OPnx/JdW9kKC9coMBilmRuYm5obGRqYWpgkmSYmmhglplgkJVmkGSUaJidbbvbjTjZewJPs7e7EzMgAgSA+I4MhAwMAD/4eUQ==";

    //启动应用程序时，检查是否已在 app 中授予了实现直播所需的权限。
    // 如果未授权，使用内置的 Android 功能申请权限；如果已授权，则返回 true。
    //开启摄像头方法
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,//语音
            Manifest.permission.CAMERA//摄像头
    };
    private RtcEngineConfig rtcEngineConfig;
    private RtcEngine mRtcEngine;

    private boolean checkSelfPermission(String permission, int requestCode) {
        //是否是摄像头和麦克风的权限
        if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //判断REQUESTED_PERMISSIONS[0]（语音）是否开启，REQUESTED_PERMISSIONS[1]（摄像头)是否开启
        // 如有一方未开启，则不能开启声网直播
        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID)) {
            initializeAndJoinChannel();
        }
    }
//开启直播
    private void initializeAndJoinChannel() {

        try {
            //配置RTC引擎
            rtcEngineConfig = new RtcEngineConfig();
            rtcEngineConfig.mContext = getBaseContext();
            rtcEngineConfig.mAppId = appId;
            rtcEngineConfig.mEventHandler = iRtcEngineEventHandler;
            mRtcEngine = RtcEngine.create(rtcEngineConfig);
        } catch (Exception e) {
            //开启直播失败
            throw new RuntimeException("Check the error.");
        }
        // 视频默认禁用，你需要调用 enableVideo 启用视频流。
        mRtcEngine.enableVideo();
        //开启摄像头预览
        mRtcEngine.startPreview();
        // 创建一个 SurfaceView 对象，并将其作为 FrameLayout 的子对象。
        SurfaceView surfaceView = new SurfaceView(getBaseContext());
        //控件加载试图
        zhi.addView(surfaceView);
        //渲染本地视频
        mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, 0));

        ChannelMediaOptions options = new ChannelMediaOptions();
        // 根据场景将用户角色设置为 BROADCASTER 或 AUDIENCE。
        options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER;
        // 极速直播下的观众，需设置用户级别为 AUDIENCE_LATENCY_LEVEL_LOW_LATENCY。
        options.audienceLatencyLevel = Constants.AUDIENCE_LATENCY_LEVEL_LOW_LATENCY;
        // 直播场景下，设置频道场景为 BROADCASTING。
        options.channelProfile = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING;

        // 使用临时 Token 加入频道。
        // 你需要自行指定用户 ID，并确保其在频道内的唯一性。
        mRtcEngine.joinChannel(token, channelName, 0, options);
    }
private IRtcEngineEventHandler iRtcEngineEventHandler = new IRtcEngineEventHandler() {
        //加载失败
    @Override
    public void onError(int err) {
        super.onError(err);
    }
        //用户加入直播
    @Override
    public void onUserOffline(int uid, int reason) {
        super.onUserOffline(uid, reason);
    }
        //用户加入直播间成功
    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
        super.onJoinChannelSuccess(channel, uid, elapsed);
    }
};
    private void initView() {
        zhi = (FrameLayout) findViewById(R.id.zhi);
    }
}