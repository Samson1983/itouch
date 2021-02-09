package com.zhuoyu.view.lock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zhuoyu.util.Constant;
import com.zhuoyu.view.SportActivity;

/**
 * 开机自启动
 * @author Administrator
 *
 */
public class MyReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
//		Log.d("Constant.Tag", "--------onReceive,"+Constant.autoSvrFlag);
//		
//		if (Constant.autoSvrFlag){ 
			//禁止按键
			Intent i = new Intent(context,LockKeyguardService.class);
			context.startService(i);
			//启动屏保程序
			Intent b=new Intent(context,SportActivity.class);
			b.setAction(Constant.MAIN_ACTION);
			b.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(b);
//		}
	}

}
