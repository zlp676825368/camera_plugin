package com.camera.camera_plugin.factory;

import android.content.Context;

import com.camera.camera_plugin.view.CameraGLMonitor;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

public class MonitorFactory extends PlatformViewFactory {
    private final BinaryMessenger messenger;
    public MonitorFactory(BinaryMessenger messenger) {
        super(StandardMessageCodec.INSTANCE);
        this.messenger = messenger;
    }
    @Override
    public PlatformView create(Context context, int viewId, Object args) {
        return new CameraGLMonitor(context, messenger, viewId);
    }
}
