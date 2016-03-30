package com.temasysreact.sdk;

public interface TemasysCallBack
{
    public void onRoomConnected(boolean isSuccess, String message);
    public void onRoomDisconnect(int errorCode, String message);
    public void onLocalMediaCapture();
}
