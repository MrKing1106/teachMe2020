package com.you.teachme;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;
import android.widget.Toast;

public class DoAction extends AccessibilityService {

    private static final String TAG = "TeachMe_DoAction";
    public AccessibilityNodeInfo nodeInfo;
    private static DoAction instance = new DoAction();
    public static DoAction getInstance(){
        return instance;
    }
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG,"AccessibilityEvent:");
        nodeInfo = getRootInActiveWindow();
        Log.d(TAG,"nodeInfo:"+nodeInfo);
        if(event.getEventType()==AccessibilityEvent.TYPE_VIEW_CLICKED){
            Log.d(TAG,"TYPE_VIEW_CLICKED   event getSource:"+event.getSource());
        }
        workService.getInstance().updateAccessibilityEvent(event,instance);

    }

    @Override
    protected void onServiceConnected() {
        Log.d(TAG,"onServiceConnected:");
    }

    @Override
    public void onInterrupt() {

    }


}
