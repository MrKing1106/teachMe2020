package com.you.teachme;

import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

public class MathUtils {
    private static final String TAG = "MathUtils";

    public static boolean checkInView(AccessibilityNodeInfo mNodeInfo,float x,float y){
        Rect rect = new Rect();
        mNodeInfo.getBoundsInScreen(rect);
        Log.d(TAG,"mNodeInfo rect.left:"+rect.left);
        Log.d(TAG,"mNodeInfo rect.top:"+rect.top);
        Log.d(TAG,"mNodeInfo rect.right:"+rect.right);
        Log.d(TAG,"mNodeInfo rect.bottom:"+rect.bottom);
        if(x>=rect.left&&x<=rect.right&&y>=rect.top&&y<=rect.bottom){
            return true;
        }else{
            return false;
        }
    }
}
