package com.camera.camera_plugin_example;

import androidx.annotation.NonNull;

import com.camera.camera_plugin.plugin.MonitorFlutterPlugin;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;

public class MainActivity extends FlutterActivity {
    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        MonitorFlutterPlugin.registerWith(flutterEngine);
    }
}
