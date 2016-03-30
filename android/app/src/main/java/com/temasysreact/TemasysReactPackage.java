package com.temasysreact;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.temasysreact.TemasysModule;
import com.temasysreact.TemasysViewManager;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.uimanager.ViewManager;
import com.facebook.react.bridge.JavaScriptModule;

import android.util.Log;



public class TemasysReactPackage implements ReactPackage
{
    public TemasysReactPackage() {
    }


    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext)
    {
        return Arrays.<NativeModule>asList(new TemasysModule(reactContext));
    }



    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext)
    {
        Log.d("TemasysReactPackage", "createViewManagers");

        return Arrays.<ViewManager>asList(new TemasysViewManager());
    }



    @Override
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }
}
