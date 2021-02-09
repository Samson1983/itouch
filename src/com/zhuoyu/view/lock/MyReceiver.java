package com.zhuoyu.view.lock;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.Toast;

import com.zhuoyu.view.MainActivity;

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
//			//启动屏保程序
//			Intent b=new Intent(context,MainActivity.class);
//			b.setAction(Constant.MAIN_ACTION);
//			b.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			context.startActivity(b);
			
//	           //操作：发送一个广播，广播接收后Toast提示定时操作完成     Intent intent =new Intent(Main.this, alarmreceiver.class);
//			Intent localIntent3 = new Intent(context, LockService.class);
////			intent.setAction("short");
//		    PendingIntent sender=
//		        PendingIntent.getBroadcast(context, 0, localIntent3, 0);
//		    
//		    //设定一个五秒后的时间
//		    Calendar calendar=Calendar.getInstance();
//		    calendar.setTimeInMillis(System.currentTimeMillis());
//		    calendar.add(Calendar.SECOND, 5);
//		    
//		    AlarmManager alarm=(AlarmManager)context.getSystemService("alarm");
//		    alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
//		    //或者以下面方式简化
//		    //alarm.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+5*1000, sender);
		
			Intent intent1= new Intent(Intent.ACTION_MAIN); 
			intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //如果是服务里调用，必须加入new task标识   
			intent1.addCategory(Intent.CATEGORY_HOME);
			context.startActivity(intent1);   
			context.startActivity(intent1);   
			context.startActivity(intent1);   
			context.startActivity(intent1);   

		    AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			Intent localIntent3 = new Intent(context, LockService.class);
		    PendingIntent pendIntent = PendingIntent.getService(context, 0, localIntent3, PendingIntent.FLAG_UPDATE_CURRENT);
		    // 5秒后发送广播，只发送一次
		    int triggerAtTime = (int) (SystemClock.elapsedRealtime() + 5 * 1000);
		    alarmMgr.set(AlarmManager. ELAPSED_REALTIME_WAKEUP , triggerAtTime, pendIntent);		    
		    
		    Toast.makeText(context, "五秒后alarm开启", Toast.LENGTH_LONG).show();			
			
			
//		      Intent localIntent3 = new Intent(context, MainActivity.class);
//		      PendingIntent localPendingIntent = PendingIntent.getService(context, 0, localIntent3, 0);
//		      AlarmManager localAlarmManager = (AlarmManager)context.getSystemService("alarm");
//		      long l = SystemClock.elapsedRealtime() + 1000L;
//		      localAlarmManager.setRepeating(2, l, -17504L, localPendingIntent);
			
			
//		}
	}

}
