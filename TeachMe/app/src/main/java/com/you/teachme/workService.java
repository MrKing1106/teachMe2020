package com.you.teachme;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

public class workService extends Service {
    private final static String TAG = "workService";
    private static workService instance = new workService();
    private AccessibilityEvent event;
    private DoAction doAction;
    public workService() {

    }
    public static workService getInstance(){
        return instance;
    }


    public void updateAccessibilityEvent(AccessibilityEvent event,DoAction doAction){
        Log.d(TAG,"updateAccessibilityEvent");
      if(event!=null){
          this.event = event;
      }
      if(doAction!=null){
          this.doAction = doAction;
      }
    }

    public static boolean clickByNode(AccessibilityNodeInfo nodeInfo, String position, float x, float y){
        if(nodeInfo==null){
            Log.d(TAG,"nodeInfo==null:"+position);
            return false;
        }
        if(nodeInfo.getChildCount()>0){
            for(int i=0;i<nodeInfo.getChildCount();i++){
                position = position +","+String.valueOf(i);
                if(!clickByNode(nodeInfo.getChild(i),position,x,y)){
                    if(MathUtils.checkInView(nodeInfo,x,y)){
                        Log.d(TAG,"找到点击position:"+position);
                        Log.d(TAG,"找到点击getClassName:"+nodeInfo.getClassName());
                        return true;
                    }
                }
            }
        }else{
            if(MathUtils.checkInView(nodeInfo,x,y)){
                Log.d(TAG,"找到点击叶子节点 position:"+position);
                Log.d(TAG,"找到点击getClassName:"+nodeInfo.getClassName());
                return true;
            }
        }
        return false;
    }

    public boolean findNodeByXY(float x, float y){
        Log.d(TAG,"findNodeByXY x:"+x+"  y:"+y);
        AccessibilityNodeInfo nodeInfo =null;
        if(doAction!=null){
            nodeInfo = doAction.getRootInActiveWindow();
        }else if(event!=null){
            nodeInfo = event.getSource();
        }else{
            Log.d(TAG,"doAction event == null");
        }
        return clickByNode(nodeInfo,"",x,y);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand");
        if (!isAccessibilitySettingsOn()) {
            requestPermission();
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * 检测辅助功能是否开启
     */
    private boolean isAccessibilitySettingsOn() {
        int accessibilityEnabled = 0;
        String service = getPackageName() + "/" + DoAction.class.getCanonicalName();
        //检测辅助功能是否打开
        try {
            accessibilityEnabled = Settings.Secure.getInt(getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            Log.d(TAG, "Error finding setting, default accessibility to not found: " + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();

                    Log.d(TAG, "-------------- > accessibilityService :: " + accessibilityService + " " + service);
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        Log.d(TAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            Log.d(TAG, "***ACCESSIBILITY IS DISABLED***");
        }
        return false;
    }

    private void requestPermission() {
        try {
            //打开系统设置中辅助功能
            Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            Toast.makeText(workService.this, "开启无障碍服务", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
