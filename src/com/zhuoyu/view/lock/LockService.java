package com.zhuoyu.view.lock;



import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.zhuoyu.util.Constant;
import com.zhuoyu.view.SportActivity;

/**
 * 锁屏处理类service
 * @author Administrator
 *
 */
public class LockService extends Service {

	private static AppIntentReceiver lockReceiver;
	
	private static boolean firstLockFlag = false;

	public static boolean isFirstLockFlag() {
		return firstLockFlag;
	}

	public static void setFirstLockFlag(boolean firstLockFlag) {
		LockService.firstLockFlag = firstLockFlag;
	}

	public static AppIntentReceiver getLockReceiver() {
		return lockReceiver;
	}

	public static void setLockReceiver(AppIntentReceiver lockReceiver) {
		LockService.lockReceiver = lockReceiver;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public void onCreate() {
//		super.onCreate();
//		Log.d(Constant.TAG, "LockService start");
//		if (lockReceiver == null){
//			registerIntentReceivers();
//		}
//		Log.d(Constant.TAG, "服务启动");
//	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.d(Constant.TAG, "LockService start");
//		if (lockReceiver == null){
			registerIntentReceivers();
//		}
		Log.d(Constant.TAG, "服务启动");
		flags = START_STICKY; //防止被系统杀掉：START_STICKY是service被kill掉后自动重写创建
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void registerIntentReceivers() { 
		Log.d(Constant.TAG, "注册广播收接器");
		// 按Power键的时候关闭屏幕和打开屏幕都会发出广播的,使用动作Intent.ACTION_SCREEN_OFF
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF); 
		filter.addAction("android.intent.action.SCREEN_ON"); 
		filter.addAction(Constant.MAIN_ACTION);
//		filter.addDataScheme("package"); 
		lockReceiver = new AppIntentReceiver();
		registerReceiver(lockReceiver, filter);
		} 
	
	
	@Override
	public void onDestroy() {
		System.out.println("lockService is onDestroy ,and lockReceiver is stop.");		
		 unregisterReceiver(lockReceiver);		 
		super.onDestroy();
		
		Intent localIntent = new Intent();
		localIntent.setClass(this, LockService.class); //销毁时重新启动Service,限在running Service
		
		this.startService(localIntent);

	}


	class AppIntentReceiver extends BroadcastReceiver { 
		
		@Override 
		public void onReceive(Context context, Intent intent) {
			//android 如何判断当前屏幕是否处在锁屏状态?
//			 String  action = intent.getAction();
//		      if(Intent.ACTION_SCREEN_ON.equals(action)){
//		       mScreenStateListener.onScreenOn();
//		      }else if(Intent.ACTION_SCREEN_OFF.equals(action)){
//		       mScreenStateListener.onScreenOff();
//		      }
			
//			ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
//			manager.restartPackage(getPackageName()); 
//			android.app.KeyguardManager mKeyguardManager = (KeyguardManager) context.getSystemService(context.KEYGUARD_SERVICE);
//			mKeyguardManager.inKeyguardRestrictedInputMode();

			Log.e(Constant.TAG, intent.getAction()+","+context.getPackageName());

//			Log.e(Constant.TAG, "--AppIntentReceiver------onReceive,"+Constant.autoSvrFlag);
//			
			if (Constant.autoSvrFlag) {
				// 点缶电源键,都会收到ACTION_SCREEN_ON和ACTION_SCREEN_OFF,无法判断是否锁屏，只能用标记
				if (firstLockFlag == false) {
					Log.d(Constant.TAG, "锁屏状态-------,已经锁屏");
					Toast.makeText(getApplicationContext(), "锁屏!",
							Toast.LENGTH_SHORT).show();
					// System.out.println("进入锁屏界面");

					// 显示main的activity
					Intent intent1 = new Intent(context, SportActivity.class);
					intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

					context.startActivity(intent1);
					System.out.println("进入锁屏界面");

					LockService.setFirstLockFlag(true); // 设置锁屏标记

				}

			}	
			
		}
	}
	

}
