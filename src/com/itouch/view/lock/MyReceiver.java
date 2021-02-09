package com.itouch.view.lock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.itouch.common.AppVersionAdapter;

/**
 * 监听：开机自启动
 * @author Administrator
 *
 */
public class MyReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {

			//启动屏保程序
		   AppVersionAdapter.startMainActivity(context);
	}

}
