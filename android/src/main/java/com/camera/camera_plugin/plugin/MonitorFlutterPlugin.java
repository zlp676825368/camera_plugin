package com.camera.camera_plugin.plugin;

import com.camera.camera_plugin.factory.MonitorFactory;

import io.flutter.embedding.engine.FlutterEngine;

public class MonitorFlutterPlugin {
    private static String NATIVE_VIEW_TYPE_ID = "com.camera.native/monitor";

    public static void registerWith(FlutterEngine flutterEngine) {
        flutterEngine.getPlatformViewsController().getRegistry()
                .registerViewFactory(NATIVE_VIEW_TYPE_ID, 
                        new MonitorFactory(flutterEngine.getDartExecutor().getBinaryMessenger()));
    }
}
