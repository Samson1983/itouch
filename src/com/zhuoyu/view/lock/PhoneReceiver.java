package com.zhuoyu.view.lock;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.zhuoyu.hv4.lock.TopFloatService;
import com.zhuoyu.util.Constant;

public class PhoneReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		if (Constant.autoSvrFlag) { //表示已经锁屏			
			 String  action = arg1.getAction();
			if(action.equals("android.intent.action.PHONE_STATE")){   //如果是来电广播，则移除此view
//				Log.e(Constant.TAG, "来电话啦！！！"+action);
				doReceivePhone(arg0, arg1);
			}		 
          
		}	
	}
	
	/**
	 * 处理电话广播.
	 * @param context
	 * @param intent
	 */
	public void doReceivePhone(Context context, Intent intent) {
		String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
		TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		int state = telephony.getCallState();
//		Log.i(Constant.TAG, "电话状态:"+state);
		switch(state){
		case TelephonyManager.CALL_STATE_RINGING:
			Log.i(Constant.TAG, "[Broadcast]等待接电话="+phoneNumber);
			try {
				if(TopFloatService.tf != null){
					TopFloatService.tf.remove();
					TopFloatService.tf = null;
					LockService.setFirstLockFlag(true); // 设置已锁屏标记,目的为:在通话中,按电源键不出现锁屏				
				}
			} catch (Exception e) {
				Log.e(Constant.TAG, "[Broadcast]Exception="+e.getMessage(), e);
			}
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK:
			Log.i(Constant.TAG, "[Broadcast]通话中="+phoneNumber);
				LockService.setFirstLockFlag(true); // 设置已锁屏标记,目的为:在通话中,按电源键不出现锁屏				
			break;
		case TelephonyManager.CALL_STATE_IDLE:
			Log.i(Constant.TAG, "[Broadcast]电话挂断="+phoneNumber);
			LockService.setFirstLockFlag(false); // 设置未锁屏标记
			break;
		}
	}	
	

}
