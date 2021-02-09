package com.itouch.common;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.itouch.hv4.layout.TopFloatService;
import com.itouch.lv2.layout.SportActivity;
import com.itouch.util.Constant;
import com.itouch.view.lock.LockService;

/***
* 继承PhoneStateListener类，我们可以重新其内部的各种监听方法
*然后通过手机状态改变时，系统自动触发这些方法来实现我们想要的功能
*/
public class MyPhoneStateListener extends PhoneStateListener{

	/**注意：按电源键也会触发电话状态,所以增加isCall来判断*/
	private static boolean isCall = false; //true:是来电;false:不是来电
		@Override
		public void onCallStateChanged(int state, String phoneNumber) {
			Log.i(Constant.TAG, "电话状态:"+state);
			switch(state){
			case TelephonyManager.CALL_STATE_RINGING:
				try {
					Log.i(Constant.TAG, "[Broadcast]等待接电话="+phoneNumber);
					isCall = true;
				    AppVersionAdapter.stopMainActivity(null);
//				    AppVersionAdapter.stopMainActivity(SportActivity.getBsv().getSportActivity());
					LockService.setFirstLockFlag(true); // 设置已锁屏标记,目的为:在通话中,再按电源键不出现锁屏				
				} catch (Exception e) {
					Log.e(Constant.TAG, "[Broadcast]Exception="+e.getMessage(), e);
				}
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				Log.i(Constant.TAG, "[Broadcast]通话中="+phoneNumber);
					isCall = true;
					LockService.setFirstLockFlag(true); // 设置已锁屏标记,目的为:在通话中,按电源键不出现锁屏				
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				if (isCall){
					Log.i(Constant.TAG, "[Broadcast]电话挂断="+phoneNumber+",isCall="+isCall);
					LockService.setFirstLockFlag(false); // 设置未锁屏标记
					isCall = false; //恢复默认值
				}else{
					Log.i(Constant.TAG, "[Broadcast]电话挂断="+phoneNumber+",isCall="+isCall);
				}
				break;
			}
			
			super.onCallStateChanged(state, phoneNumber);
		}
 	
 }	