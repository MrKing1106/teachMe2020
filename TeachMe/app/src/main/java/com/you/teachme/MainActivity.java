package com.you.teachme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TeachMeMainActivity";
    DoAction doAction;
    workService mWorkService;
    MyReciever myReciever;
    public class MyReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            float x =intent.getFloatExtra("x",0);
            float y =intent.getFloatExtra("y",0);
            workService.getInstance().findNodeByXY(x,y);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
        setContentView(R.layout.activity_main);
        Button openFloatingWin = findViewById(R.id.open_floating_view);

        openFloatingWin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetTouch.initSystemWindow(MainActivity.this);
            }
        });
        myReciever = new MyReciever();
        IntentFilter filter = new IntentFilter();
        filter.addAction("findNode");
        registerReceiver(myReciever,filter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent serviceIntent = new Intent(this,workService.class);
        startService(serviceIntent);
//        mWorkService = new workService();
//        mWorkService.startForeground(1, new Notification.Builder(this).setContentTitle("work").setTicker("workTicker").build());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onCreate");
        GetTouch.clearwindow();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(myReciever!=null){
            unregisterReceiver(myReciever);
        }
    }
}