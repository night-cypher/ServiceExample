package com.example.questdot.serviceexample;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ServiceConnection {

    EditText editText;
    TextView textView;
    Intent i;
    private MyService.Binder binder=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        i= new Intent(this,MyService.class);
        editText=(EditText)findViewById(R.id.editText);
        textView= (TextView)findViewById(R.id.textView);

        findViewById(R.id.btnStart).setOnClickListener(this);
        findViewById(R.id.btnStop).setOnClickListener(this);
        findViewById(R.id.btnBind).setOnClickListener(this);
        findViewById(R.id.btnUnbind).setOnClickListener(this);
        findViewById(R.id.btnSyn).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.btnStart:

                i.putExtra("data",editText.getText().toString());
                startService(i);
                break;
            case R.id.btnStop:
                stopService(i);
                break;

            case R.id.btnBind:

               bindService(new Intent(this,MyService.class),this, Context.BIND_AUTO_CREATE);
                break;
            case R.id.btnUnbind:
                unbindService(this);
                break;

            case R.id.btnSyn:
               if(binder!=null){
                   binder.setData(editText.getText().toString());
               }
                break;
        }


    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (MyService.Binder) service;
        binder.getMyService().setCallback(new MyService.Callback() {
            @Override
            public void onDateChange(String data) {
                Message msg = new Message();
                Bundle b = new Bundle();
                b.putString("data",data);
                msg.setData(b);
                handler.sendMessage(msg);
            }
        });
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            textView.setText(msg.getData().getString("data"));
        }
    };
}
