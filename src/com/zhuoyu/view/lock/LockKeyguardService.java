package com.zhuoyu.view.lock;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * 给MyReceiver调用,替换系统的LockScreen
 * @author Administrator
 *
 */
public class LockKeyguardService extends Service {
	private KeyguardManager mKeyguard;
	private KeyguardLock mKeylock;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// 替换系统的LockScreen
		mKeyguard = (KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
        mKeylock = mKeyguard.newKeyguardLock("zhuoyu");
        mKeylock.disableKeyguard();
//		Toast.makeText(LockKeyguardService.this, "重启服务成功,禁止解锁成功", Toast.LENGTH_LONG).show();
		
		super.onCreate();
//		new Thread(new Runnable(){
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				Log.d("H3c", "in Server");
//			}
//		}).start();
	}
}
