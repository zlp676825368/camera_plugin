package com.camera.camera_plugin.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;

import com.hichip.callback.ICameraIOSessionCallback;
import com.hichip.callback.ICameraPlayStateCallback;
import com.hichip.control.HiCamera;
import com.hichip.control.HiGLMonitor;
import com.hichip.sdk.HiChipSDK;
import com.hichip.sdk.HiManageLib;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.platform.PlatformView;

public class CameraGLMonitor implements PlatformView, MethodChannel.MethodCallHandler, ICameraPlayStateCallback, ICameraIOSessionCallback {
    private HiGLMonitor monitor;
    Context context;
    HiCamera hiCamera;
    //事件派发对象
    private EventChannel.EventSink eventSink =null;
    //事件派发流
    private EventChannel.StreamHandler streamHandler = new EventChannel.StreamHandler() {
        @Override
        public void onListen(Object arguments, EventChannel.EventSink events) {
            eventSink = events;
        }

        @Override
        public void onCancel(Object arguments) {
            eventSink =null;
        }
    };


    private void init(){
        new HiManageLib();
        HiChipSDK.init(new HiChipSDK.HiChipInitCallback() {
            @Override
            public void onSuccess(int i, int i1) {
                if (hiCamera==null){
                    hiCamera = new HiCamera(context,"SSAC-021648-DFACF","admin","admin");
                    hiCamera.registerIOSessionListener(CameraGLMonitor.this);
                    hiCamera.registerPlayStateListener(CameraGLMonitor.this);
                    hiCamera.connect();
                }
            }

            @Override
            public void onFali(int i, int i1) {

            }
        });
    }

    public CameraGLMonitor(Context context, BinaryMessenger messenger, int id){
        this.context =context;
        init();
        this.monitor = new HiGLMonitor(context);
        MethodChannel channel = new MethodChannel(messenger,"com.camera.native/monitor_"+id);
        channel.setMethodCallHandler(this);
        //初始化事件流监听
        EventChannel eventChannel = new EventChannel(messenger,"com.camera.native/state_"+id);
        eventChannel.setStreamHandler(streamHandler);

    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {

    }

    @Override
    public View getView() {
        return monitor;
    }

    @Override
    public void dispose() {
        if (hiCamera!=null){
            hiCamera.unregisterIOSessionListener();
            hiCamera.unregisterPlayStateListener();
            streamHandler.onCancel(null);
            eventSink =null;
            hiCamera.disconnect(1);
            HiChipSDK.uninit();
        }
    }
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (eventSink!=null){
                eventSink.success(msg.arg1);
            }
        }
    };

    @Override
    public void receiveSessionState(HiCamera hiCamera, int i) {
        System.out.println("====================================="+i);
        Message message = Message.obtain();
        message.arg1 =i;
        handler.sendMessage(message);
        if (i == HiCamera.CAMERA_CONNECTION_STATE_LOGIN){
            hiCamera.startLiveShow(1,monitor);
        }
    }

    @Override
    public void receiveIOCtrlData(HiCamera hiCamera, int i, byte[] bytes, int i1) {

    }

    @Override
    public void callbackState(HiCamera hiCamera, int i, int i1, int i2) {

    }

    @Override
    public void callbackPlayUTC(HiCamera hiCamera, int i) {

    }
}
