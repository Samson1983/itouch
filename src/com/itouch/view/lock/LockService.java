package com.itouch.view.lock;



import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.itouch.common.AppVersionAdapter;
import com.itouch.util.Constant;
import com.itouch.util.ZhuoyuUIUtil;

/**
 * 监听：电源键开关的广播
 * 锁屏处理类service
 * @author Administrator
 *
 */
public class LockService extends Service {

	private static AppIntentReceiver lockReceiver;
	
	private static boolean firstLockFlag = false; //ture:已锁屏;false:未锁屏

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

	@Override
	public void onCreate() {
		super.onCreate();
		if (lockReceiver == null){
			Log.d(Constant.TAG, "onCreate服务创建");
			// 防止主进程被 一键清理
			AppVersionAdapter.startNotification(this,this);	
			//注册服务
			registerIntentReceivers();
		}		
	}	
	
	/**
	 * 每次调用startService(Intent)的时候，都会调用该Service对象的onStartCommand
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(Constant.TAG, "LockService start");
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
//		System.out.println("lockService is onDestroy ,and lockReceiver is stop."+lockReceiver);
		unregisterReceiver(lockReceiver);
		lockReceiver = null;
		super.onDestroy();
//		Intent localIntent = new Intent();
//		localIntent.setClass(this, LockService.class); //销毁时重新启动Service
//		this.startService(localIntent);

		
	}


	class AppIntentReceiver extends BroadcastReceiver { 
		
		@Override 
		public void onReceive(Context context, Intent intent) {
			//android 如何判断当前屏幕是否处在锁屏状态?
			 String  action = intent.getAction();
//		      if(Intent.ACTION_SCREEN_ON.equals(action)){
//		       mScreenStateListener.onScreenOn();
//		      }else if(Intent.ACTION_SCREEN_OFF.equals(action)){
//		       mScreenStateListener.onScreenOff();
//		      }
//			
//			ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
//			manager.restartPackage(getPackageName()); 
//			android.app.KeyguardManager mKeyguardManager = (KeyguardManager) context.getSystemService(context.KEYGUARD_SERVICE);
//			mKeyguardManager.inKeyguardRestrictedInputMode();

		        
			Log.e(Constant.TAG, intent.getAction()+","+context.getPackageName()+","+Constant.autoSvrFlag+","+firstLockFlag);
			if(Intent.ACTION_SCREEN_ON.equals(action)){
				
			}

			//干掉系统锁
			ZhuoyuUIUtil.clearSysLock(context);
			
			if (Constant.autoSvrFlag){ //是否启动锁屏应用
				if (firstLockFlag == false) {//未锁屏					
					if(Intent.ACTION_SCREEN_OFF.equals(action)){
						
						// System.out.println("进入锁屏界面");
						// 显示main的activity
						AppVersionAdapter.startMainActivity(context);
						
					}	
				}	
			}else{
				//恢复系统锁
//				ZhuoyuUIUtil.openSysLock(context);
				
			}
			
		}
	}



	

}
