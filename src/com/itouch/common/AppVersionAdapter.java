package com.itouch.common;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.util.Log;
import android.widget.Toast;

import com.itouch.hv4.layout.MainActivity;
import com.itouch.hv4.layout.TopFloatService;
import com.itouch.lv2.layout.SportActivity;
import com.itouch.util.Constant;
import com.itouch.view.lock.LockService;

public class AppVersionAdapter {
	
	private static int sysVersion = Constant.SDK_VERSION_4;
	
	
	/**
	 * 启动MainActivity
	 * @param context
	 * @param isLock
	 */
	public static void startMainActivity(Context context){
		
		Toast.makeText(context, "锁屏!",Toast.LENGTH_SHORT).show();
//		if (getSysVersion() >= Constant.SDK_VERSION_4){
			//4.0以上
//			Toast.makeText(context, "android version 4.0以上", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(context,MainActivity.class);
			intent.setAction(Constant.MAIN_ACTION);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
//		}else{
//			//2.0
////			Toast.makeText(context, "version 4.0以下", Toast.LENGTH_SHORT).show();
//			Intent intent = new Intent(context,SportActivity.class);
//			intent.setAction(Constant.MAIN_ACTION);
//			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			context.startActivity(intent);
//		}
		LockService.setFirstLockFlag(true); // 设置已锁屏标记		
		
	}
	
	/**
	 * 停止MainActivity
	 * @param context
	 */
	public static void stopMainActivity(SportActivity sa_v2){
//		if (getSysVersion() >= Constant.SDK_VERSION_4){
			//4.0以上
			TopFloatService.destory();
//		}else{
//			//2.0
//			if (sa_v2 !=null){
//				sa_v2.getBsv().surfaceDestroyed(sa_v2.getBsv().getHolder());//停止绘动主线程 
//				sa_v2.finish(); //关闭
//			}
//			
//		}
		LockService.setFirstLockFlag(false); // 设置已锁屏标记	
	}
	
	
	/**
	 * 防止主进程被 一键清理
	 * @param ser
	 * @param context
	 */
	public static void startNotification(Service ser,Context context){
		PendingIntent p_intent = null;
//		if (getSysVersion() >= Constant.SDK_VERSION_4){
			//4.0以上
			p_intent = PendingIntent.getActivity(context, 0,new Intent(context, MainActivity.class), 0);
//		}else{
//			//2.0
//			p_intent = PendingIntent.getActivity(context, 0,new Intent(context, SportActivity.class), 0);
//		}
		
		Notification notification = new Notification(0,
	               "my_service_name",
	                System.currentTimeMillis());
	        notification.setLatestEventInfo(context, "MyServiceNotification", "MyServiceNotification is Running!",   p_intent);
//	        Log.d(Constant.TAG, String.format("notification = %s", notification));
	        ser.startForeground(0x1982, notification);   // notification ID: 0x1982, you can name it as you will.			
		
	}

	public static Intent getIntent(Context context){
		Intent i= null;
//		if (getSysVersion() >= Constant.SDK_VERSION_4){
			//4.0以上
			i = new Intent(context, MainActivity.class);
//		}else{
//			//2.0
//			i = new Intent(context, SportActivity.class);
//		}
	return 	i;
	}
	
	/**
	 * 获取系统版本号
	 * @return
	 */
	public static int getSysVersion(){
		try {
			sysVersion = Integer.parseInt(VERSION.SDK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sysVersion; 
	}	
}
