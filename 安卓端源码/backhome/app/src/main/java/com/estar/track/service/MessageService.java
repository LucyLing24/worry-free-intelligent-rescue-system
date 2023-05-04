package com.estar.track.service;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.estar.track.Listener.WsManager;
import com.estar.track.entity.User;
import com.estar.track.utils.Address;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class MessageService  extends Service {

    public static String user = "";

    WsManager wsManager;

    Timer timer;

    //当前context
    static Context context;


    public static String getUser() {
        return user;
    }

    public static void setUser(String usera) {
        user = usera;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        context = getApplicationContext();
        if(!"".equals(user)){
            wsManager = new WsManager.Builder(getBaseContext())
                    .client(
                            new OkHttpClient().newBuilder()
                                    .pingInterval(15, TimeUnit.SECONDS)
                                    .retryOnConnectionFailure(true)
                                    .build()
                    )
                    .needReconnect(true)
                    .wsUrl(Address.WSIP + user).build();
            wsManager.startConnect();
            Log.d("WSStartSSSS","SSSS");
        }

        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                wsManager.sendMessage("ping");
            }
        };
        timer.schedule(task,0,1000);

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        wsManager.stopConnect();
        super.onDestroy();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }


    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
        builder.setMessage("有任务待接");
        builder.setPositiveButton("接受", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("OK","RRR");
            }
        });

        builder.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.i("NO", "No is clicked");
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);// 设置为系统Dialog
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();


    }

}
