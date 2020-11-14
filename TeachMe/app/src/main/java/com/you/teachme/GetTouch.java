package com.you.teachme;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import java.util.logging.LogRecord;

public class GetTouch {

    private static final String TAG = "GetTouch";
    static WindowManager windowManager;
    static View floatView;
    static float x = 0;
    static float y = 0;
    /**
     * 利用系统弹窗实现悬浮窗
     *
     * @param mContext
     */
    public static void initSystemWindow(final Context mContext) {

        windowManager = SystemUtils.getWindowManager(mContext);
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        wmParams.packageName = mContext.getPackageName();
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_SCALED
                | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.gravity = Gravity.START | Gravity.TOP;


        wmParams.width = SystemUtils.getScreenWidth(mContext);
        wmParams.height = SystemUtils.getScreenHeight(mContext);
        wmParams.x = 0;
        wmParams.y = 0;
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        floatView = inflater.inflate(R.layout.floating_layout, null);
        floatView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG,"onTouch event:"+event.toString());
                Log.d(TAG,"onTouch event:"+v.toString());
                Log.d(TAG,"onTouch getRawX:"+event.getRawX());
                Log.d(TAG,"onTouch getX:"+event.getX());
                x = event.getRawX();
                y = event.getRawY();
                clearwindow();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        intent.setAction("findNode");
                        intent.putExtra("x",x);
                        intent.putExtra("y",y);
                        mContext.sendBroadcast(intent);
                    }
                },200);

                return false;
            }
        });
        windowManager.addView((View) floatView, wmParams);
    }

    public static void clearwindow(){
        if(windowManager!=null){
            windowManager.removeView(floatView);
        }
    }

}
