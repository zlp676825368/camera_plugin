import 'dart:io';

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:camera_plugin/camera_plugin.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  MethodChannel _channel;
  StreamSubscription _subscription;
  String _statusText = "连接中...";
  @override
  void initState() {
    super.initState();
  }

  // ignore: missing_return
  Widget _createView() {
    if (Platform.isAndroid) {
      return AndroidView(
        viewType: "com.camera.native/monitor",
        creationParamsCodec: const StandardMessageCodec(),
        onPlatformViewCreated: this._onMyViewCreated,
      );
    } else if (Platform.isIOS) {
      return UiKitView(
        viewType: "com.camera.native/monitor",
        creationParamsCodec: const StandardMessageCodec(),
        onPlatformViewCreated: this._onMyViewCreated,
      );
    }
  }
  void _onMyViewCreated(int id) {
    _channel = MethodChannel('com.camera.native/monitor_$id');
    _subscription = EventChannel("com.camera.native/state_$id")
        .receiveBroadcastStream()
        .listen((event) {
      switch (event) {
        case 1:
          _statusText = "加载中...";
          break;
        case 4:
          _statusText = "连接成功...";
          break;
      }
      setState(() {});
    }, onError: (error) => {});
  }
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Container(
          width: double.infinity,
          height: 200,
          child: _createView(),
        ),
      ),
    );
  }
  @override
  void dispose() {
    _channel = null;
    if(_subscription!=null){
      _subscription.cancel();
    }
    super.dispose();
  }
}
