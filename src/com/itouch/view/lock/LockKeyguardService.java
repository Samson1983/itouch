//package com.itouch.view.lock;
//
//import android.app.Service;
//import android.content.Intent;
//import android.os.Handler;
//import android.os.IBinder;
//import android.util.Log;
//
//import com.itouch.util.Constant;
//import com.itouch.util.ZhuoyuUIUtil;
//
///**
// * 监听:替换系统的LockScreen
// * @author Administrator
// *
// */
//public class LockKeyguardService extends Service {
//
//	@Override
//	public IBinder onBind(Intent intent) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void onCreate() {
//
//	}
//	
//	@Override
//	public int onStartCommand(Intent intent, int flags, int startId) {
//		// TODO Auto-generated method stub
//		Log.d(Constant.TAG, "LockKeyguardService start");
//		
//		Handler handler = new Handler();
//		Runnable r1 = new Runnable() {
//			public void run() {
//				//干掉系统锁
//				System.out.println("------------200ms");
//				ZhuoyuUIUtil.clearSysLock(LockKeyguardService.this);
//			};
//		};
//		handler.postDelayed(r1,200);
////		Toast.makeText(LockKeyguardService.this, "重启服务成功,禁止解锁成功", Toast.LENGTH_LONG).show();
//        flags = START_STICKY; //防止被系统杀掉：START_STICKY是service被kill掉后自动重写创建
//		return super.onStartCommand(intent, flags, startId);
//	}	
//}
