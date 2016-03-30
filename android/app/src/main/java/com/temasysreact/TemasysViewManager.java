package com.temasysreact;

import javax.annotation.Nullable;
import android.content.Context;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import com.temasysreact.TemasysRendererView;
import android.util.Log;



public class TemasysViewManager extends SimpleViewManager<TemasysRendererView>
{
  public static final String TAG = "TemasysRendererView";
  public static final String REACT_CLASS = "RCTTemasysRendererView";



  public TemasysViewManager() {
  }



  @Override
  public String getName() {
      return REACT_CLASS;
  }



  @Override
  public TemasysRendererView createViewInstance(ThemedReactContext context) {
      Log.d(TAG, "createViewInstance");
      return new TemasysRendererView(context);
  }



  @ReactProp(name = "preview")
  public void setPreviewStatus(TemasysRendererView view, Boolean preview) {
    Log.d(TAG, "createViewInstance");

    view.setPreviewStatus(preview);
    view.requestLayout();
  }



  @ReactProp(name = "callId")
  public void setCallId(TemasysRendererView view, @Nullable String callId) {
    Log.d(TAG, "callId");

    view.setCallId(callId != null ? callId : "");
  }
}
