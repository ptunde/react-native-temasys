package com.temasysreact.sdk;

import sg.com.temasys.skylink.sdk.listener.LifeCycleListener;
import sg.com.temasys.skylink.sdk.listener.MediaListener;
import sg.com.temasys.skylink.sdk.rtc.SkylinkConnection;
import sg.com.temasys.skylink.sdk.config.SkylinkConfig;

import android.view.ViewGroup;
import android.view.View;
import android.opengl.GLSurfaceView;
import android.graphics.Point;
import android.util.Log;

import java.util.List;

import com.facebook.react.bridge.ReactApplicationContext;



public class TemasysClient implements LifeCycleListener, MediaListener
{
  public static final int TIME_OUT = 60;

  // TODO remove this
  private static final String APP_KEY = "408a05c2-1658-426b-aad6-6f7fb07e5cd1";
  private static final String TAG = "TemasysClient";
  private static TemasysClient instance;

  private SkylinkConnection skylinkConnection;
  private String skylinkConnectionString;
  private String myRoomID;
  private String chatRoomName;
  private TemasysCallBack temasysCallBack;
  private ViewGroup localPreview;
  private ViewGroup remotePreview;
  private ReactApplicationContext reactContext;



  private TemasysClient()
  {
      // no-op
  }



  public static synchronized TemasysClient instance()
  {
      Log.d(TAG, "TemasysClient instance");

      if (instance == null)
      {
          Log.d(TAG, "new TemasysClient");
          instance = new TemasysClient();
      }

      return instance;
  }



  public void setAndroidContext(ReactApplicationContext reactContext)
  {
      this.reactContext = reactContext;
  }



  public void setCallback(TemasysCallBack temasysCallBack)
  {
      this.temasysCallBack = temasysCallBack;
  }



  public void setLocalPreview(String userID, ViewGroup localPreview)
  {
      this.localPreview = localPreview;
      Log.d(TAG, "setLocalPreview");
  }



  public void setRemoteView(String userID, ViewGroup remotePreview)
  {
      this.remotePreview = remotePreview;
      Log.d(TAG, "setRemoteView");
  }



  public void connect(String skylinkConnectionString, String chatRoomName, String myRoomID)
  {
      this.skylinkConnectionString = skylinkConnectionString;
      this.chatRoomName = chatRoomName;
      this.myRoomID = myRoomID;

      // Initialize the skylink connection
      initializeSkylinkConnection();

      // Initialize the audio router
      //initializeAudioRouter();

      skylinkConnection.connectToRoom(skylinkConnectionString, myRoomID);

      // Use the Audio router to switch between headphone and headset
      //audioRouter.startAudioRouting(getActivity().getApplicationContext());
  }



  public void toggleAudio(boolean b)
  {
      skylinkConnection.muteLocalAudio(b);
  }



  public void toggleVideo(boolean b)
  {
      skylinkConnection.muteLocalVideo(b);
  }



  public void switchCamera()
  {
      skylinkConnection.switchCamera();
  }



  /***
   * Lifecycle Listener Callbacks -- triggered during events that happen during the SDK's lifecycle
   */
  @Override
  public void onConnect(boolean isSuccess, String message)
  {
      if (isSuccess)
      {
          Log.d(TAG, "Connected to room " + chatRoomName + " as " + myRoomID);
      }
      else
      {
          Log.e(TAG, "Skylink Failed " + message);
      }

      temasysCallBack.onRoomConnected(isSuccess, message);
  }



  @Override
  public void onDisconnect(int errorCode, String message)
  {
      Log.d(TAG, message + " disconnected");

      temasysCallBack.onRoomDisconnect(errorCode, message);
  }



  @Override
  public void onLockRoomStatusChange(String remotePeerId, boolean lockStatus)
  {
      Log.d(TAG, "Peer " + remotePeerId + " has changed Room locked status to " + lockStatus);
  }



  @Override
  public void onWarning(int errorCode, String message)
  {
      Log.d(TAG, message + "warning");
  }



  @Override
  public void onReceiveLog(String message)
  {
      Log.d(TAG, message + " on receive log");
  }



  /**
   * Media Listeners Callbacks - triggered when receiving changes to Media Stream from the remote peer
   */
  @Override
  public void onLocalMediaCapture(GLSurfaceView videoView)
  {
      temasysCallBack.onLocalMediaCapture();

      if (videoView != null) {
          View self = localPreview.findViewWithTag("self");
          videoView.setTag("self");

          if (self == null) {
              //show media on screen
              localPreview.removeView(videoView);
              localPreview.addView(videoView);
          } else {
              videoView.setLayoutParams(self.getLayoutParams());

              // If peer video exists, remove it first.
              View peer = remotePreview.findViewWithTag("peer");
              if (peer != null) {
                  remotePreview.removeView(peer);
              }

              // Remove the old self video and add the new one.
              localPreview.removeView(self);
              localPreview.addView(videoView);

              // Return the peer video, if it was there before.
              if (peer != null) {
                  remotePreview.addView(peer);
              }
          }
      }
  }



  @Override
  public void onVideoSizeChange(String peerId, Point size)
  {
      Log.d(TAG, "PeerId: " + peerId + " got size " + size.toString());
  }



  @Override
  public void onRemotePeerAudioToggle(String remotePeerId, boolean isMuted)
  {
      String message = isMuted ? "Your peer muted their audio" : "Your peer unmuted their audio";
      Log.d(TAG, message);
  }

  @Override
  public void onRemotePeerMediaReceive(String remotePeerId, GLSurfaceView videoView) {
  }

  @Override
  public void onRemotePeerVideoToggle(String peerId, boolean isMuted)
  {
      String message = isMuted ? "Your peer muted video" : "Your peer unmuted their video";
      Log.d(TAG, message);
  }



  private void initializeSkylinkConnection()
  {
    if (skylinkConnection == null)
    {
        skylinkConnection = SkylinkConnection.getInstance();
        //the app_key and app_secret is obtained from the temasys developer console.
        skylinkConnection.init(APP_KEY, getSkylinkConfig(), reactContext);
        //set listeners to receive callbacks when events are triggered
        skylinkConnection.setLifeCycleListener(this);
        skylinkConnection.setMediaListener(this);

        // TODO uncomment this
        //skylinkConnection.setRemotePeerListener(this);
    }
}

private SkylinkConfig getSkylinkConfig() {
    SkylinkConfig config = new SkylinkConfig();
    //AudioVideo config options can be NO_AUDIO_NO_VIDEO, AUDIO_ONLY, VIDEO_ONLY, AUDIO_AND_VIDEO;
    config.setAudioVideoSendConfig(SkylinkConfig.AudioVideoConfig.AUDIO_AND_VIDEO);
    config.setAudioVideoReceiveConfig(SkylinkConfig.AudioVideoConfig.AUDIO_AND_VIDEO);
    config.setHasPeerMessaging(true);
    config.setHasFileTransfer(true);
    config.setTimeout(TIME_OUT);
    config.setMirrorLocalView(true);
    return config;
}

}
