package com.example.xm.xianyc.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;


public class GuardService extends Service {
    private NotificationManager manager;
    public static final String id ="channel_1";
    public static final String name = "channel_name_1";
    public static Notification notification;
    @Nullable
     @Override
    public IBinder onBind(Intent intent) {
    return new IMyAidlInterface.Stub() {
    };
    }
 @RequiresApi(api = Build.VERSION_CODES.O)
 @Override
public int onStartCommand(Intent intent, int flags, int startId) {
         //绑定建立链接
     //startService(new Intent(GuardService.this,IMService.class));
         bindService(new Intent(this, IMService.class),
                 mServiceConnection, Context.BIND_IMPORTANT);
         return START_STICKY;
     }

 private ServiceConnection mServiceConnection = new ServiceConnection() {
 @Override
 public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//链接上
     //startService(new Intent(GuardService.this,IMService.class));
     //重新绑定
     bindService(new Intent(GuardService.this,IMService.class),
             mServiceConnection, Context.BIND_IMPORTANT);
 Log.d("test","GuardService:建立链接");
 }

 @Override
 public void onServiceDisconnected(ComponentName componentName) {
//断开链接
 startService(new Intent(GuardService.this,IMService.class));
 //重新绑定
 bindService(new Intent(GuardService.this,IMService.class),
                 mServiceConnection, Context.BIND_IMPORTANT);
 }
 };

}
