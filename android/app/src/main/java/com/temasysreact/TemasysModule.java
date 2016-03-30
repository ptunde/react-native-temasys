package com.temasysreact;

import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableMapKeySetIterator;

import com.temasysreact.sdk.TemasysCallBack;
import com.temasysreact.sdk.TemasysClient;
import com.temasysreact.sdk.Utils;


import sg.com.temasys.skylink.sdk.rtc.SkylinkConnection;

import javax.annotation.Nullable;
import java.util.Date;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import android.util.Log;



public class TemasysModule extends ReactContextBaseJavaModule implements TemasysCallBack
{
    public static final String REACT_MODULE = "TemasysModule";
    private static final String TAG = "TemasysModule";

    private TemasysClient client;
    private ReactApplicationContext reactContext;



    public TemasysModule(ReactApplicationContext reactContext)
    {
        super(reactContext);

        this.reactContext = reactContext;

        this.client = TemasysClient.instance();
        this.client.setCallback(this);
        this.client.setAndroidContext(reactContext);
    }



    @Override
    public String getName()
    {
        return REACT_MODULE;
    }



    // @ReactMethod
    // public void connect(String skylinkConnectionString, String chatRoomName, String chatRoomID)
    // {
    //     this.client.connect();
    // }

    @ReactMethod
    public void connect()
    {
        // TODO remove hardcoded values
        this.client.connect(Utils.getSkylinkConnectionString("videoRoom", "408a05c2-1658-426b-aad6-6f7fb07e5cd1",
        "yn6d073dkhp8p", new Date(), SkylinkConnection.DEFAULT_DURATION), "videoRoom", "test");
    }



    @ReactMethod
    public void toggleAudio(boolean b)
    {
        this.client.toggleAudio(b);
    }



    @ReactMethod
    public void toggleVideo(boolean b)
    {
        this.client.toggleVideo(b);
    }



    @ReactMethod
    public void switchCamera()
    {
        this.client.switchCamera();
    }



    /***
     * Interface Callbacks
     */
    @Override
    public void onRoomConnected(boolean isSuccess, String message)
    {
        WritableMap params = Arguments.createMap();
        params.putBoolean("isSuccess", isSuccess);
        params.putString("message", message);
        sendEvent(this.reactContext, "OnRoomConnected", params);
    }



    @Override
    public void onRoomDisconnect(int errorCode, String message)
    {
        WritableMap params = Arguments.createMap();
        params.putInt("errorCode", errorCode);
        params.putString("message", message);
        sendEvent(this.reactContext, "OnRoomDisconnected", params);
    }



    @Override
    public void onLocalMediaCapture()
    {
        WritableMap params = Arguments.createMap();
        sendEvent(this.reactContext, "OnLocalMediaCaptured", params);
    }



    private void sendEvent(ReactContext reactContext, String eventName, @Nullable WritableMap params) {
       reactContext
          .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
          .emit(eventName, params);
   }
}
