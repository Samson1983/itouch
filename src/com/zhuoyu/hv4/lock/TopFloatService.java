package com.zhuoyu.hv4.lock;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.zhuoyu.hv4.layout.TopFrame;
import com.zhuoyu.util.Constant;



public class TopFloatService extends Service {

	public static TopFrame tf ;

	
//	@Override
//	public void onCreate() {
//		System.out.println("-------TopFloatService----onCreate----");
//		super.onCreate();
//		
//	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.d(Constant.TAG, "TopFloatService start");
		flags = START_STICKY; //防止被系统杀掉：START_STICKY是service被kill掉后自动重写创建
		return super.onStartCommand(intent, flags, startId);
	}	

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		tf = new TopFrame(this);
		
		Handler handler = new Handler();
		handler.post(new Runnable() {
			public void run() {
				tf.createView();
				
				tf.addView();
			};
		});

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		System.out.println("--------onDestroy----------");
		super.onDestroy();
		tf.remove();
	}


}