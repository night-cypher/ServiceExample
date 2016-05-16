package com.example.questdot.serviceexample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telecom.Call;
import android.widget.Toast;

public class MyService extends Service {
    private boolean running=false;
    private String data ="default data";

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
      return new Binder();
    }



    public class Binder extends android.os.Binder{
        public void setData(String data){
               MyService.this.data= data;
           }
        public MyService getMyService(){
            return MyService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        data = intent.getStringExtra("data");
        return super.onStartCommand(intent, flags, startId);


    }

    @Override
    public void onCreate() {
        super.onCreate();
        running=true;

        new Thread(){
            @Override
            public void run() {
                super.run();
                int i = 0;
                while (running) {
                    i++;
                    String string = i+" : "+data;
                    System.out.println(string);

                    if(callback!=null){
                        callback.onDateChange(string);
                    }

                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }
        }.start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        running=false;

    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public Callback getCallback() {
        return callback;
    }

    private Callback callback= null;
    public static interface Callback{
        void onDateChange(String data);
    }
}
