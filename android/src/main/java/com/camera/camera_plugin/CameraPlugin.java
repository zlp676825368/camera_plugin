package com.camera.camera_plugin;

import android.content.Context;

import androidx.annotation.NonNull;

import com.hichip.control.HiCamera;
import com.hichip.sdk.HiChipSDK;
import com.hichip.sdk.HiManageLib;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * CameraPlugin
 */
public class CameraPlugin implements FlutterPlugin, MethodCallHandler {
    Context context;
    HiCamera hiCamera;
    //事件派发对象
    private EventChannel.EventSink eventSink = null;
    //事件派发流
    private EventChannel.StreamHandler streamHandler = new EventChannel.StreamHandler() {
        @Override
        public void onListen(Object arguments, EventChannel.EventSink events) {
            eventSink = events;
        }

        @Override
        public void onCancel(Object arguments) {
            eventSink = null;
        }
    };
    private MethodChannel channel;

    private void init() {
        new HiManageLib();
        HiChipSDK.init(new HiChipSDK.HiChipInitCallback() {
            @Override
            public void onSuccess(int i, int i1) {
                System.out.println("初始化成功..........");
            }

            @Override
            public void onFali(int i, int i1) {

            }
        });
    }

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        init();
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "camera_plugin");
        channel.setMethodCallHandler(this);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("getPlatformVersion")) {
            result.success("Android " + android.os.Build.VERSION.RELEASE);
        } else {
            result.notImplemented();
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }
}
