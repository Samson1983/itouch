package com.itouch.common;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.itouch.hv4.layout.MainActivity;
import com.itouch.util.Constant;
import com.itouch.util.ZhuoyuUIUtil;
import com.itouch.view.R;

public class InitHelper {

	/**读配置文件*/
	public static void instance(Activity aty,Context context){
			if (Constant.prefs == null){
	  		 	/**不会为空*/
				Constant.prefs = PreferenceManager.getDefaultSharedPreferences(context) ;
			}
				//是否启用应用程序
				Constant.autoSvrFlag = Constant.prefs.getBoolean("startFroth", true);
//				System.out.println("----------Constant.prefs:"+Constant.prefs);
				// 获得屏幕尺寸
				DisplayMetrics dm = new DisplayMetrics();
				dm = context.getApplicationContext().getResources().getDisplayMetrics();
				Constant.screenWidth = dm.widthPixels;
				Constant.screenHeight = dm.heightPixels;		
				
				Constant.froth2bumpMusic = MediaPlayer.create(context, R.raw.froth2bump);
				if (Constant.musicValue == 0.0f){
					Constant.musicValue = (float) (Constant.prefs.getInt("musicValue", 3) * 0.1); //初始化读取,标配0.3f
				}
//				System.out.println("0000--------"+Constant.musicValue);
				Constant.froth2bumpMusic.setVolume(Constant.musicValue, Constant.musicValue); //根据进度条设置音量
				
				
				if (Constant.ballSpeed == 0){ //android4.0以上默认是0
					Constant.ballSpeed =  (Constant.prefs.getInt("frothSpeed", 3)*1 ); //初始化读取,标配3
				}
				Log.e(Constant.TAG, "init-------------ballSpeed:"+Constant.ballSpeed);
				
				Constant.imgList = ZhuoyuUIUtil.initImageList(context); //读取图片	

	 	       //获取图片
	    	   Constant.ballBitmap1 = ZhuoyuUIUtil.setImageResource(context,null,1, R.drawable.ball_man); 
	    	   Constant.ballBitmap2 = ZhuoyuUIUtil.setImageResource(context,null,2, R.drawable.ball_wife_son);    	   
//			}
			
	    	   aty.requestWindowFeature(Window.FEATURE_NO_TITLE); //去title
			Constant.fillScreen = Constant.prefs.getBoolean("fillScreen", false);
			if (Constant.fillScreen){ //是否全屏
				context.setTheme(android.R.style.Theme_NoTitleBar_Fullscreen);
				// 下两句为设置全屏
				aty.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
						WindowManager.LayoutParams.FLAG_FULLSCREEN);
				aty.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
				
			}
			
			Constant.vibrate = Constant.prefs.getBoolean("vibrate", false); //振动
	}
	
 
	
	
	/**
	 *  截屏方法
	 *  return 
	 */
	private static Bitmap shot(Activity aty) {       
			View view = aty.getWindow().getDecorView();  
            Display display = aty.getWindowManager().getDefaultDisplay();       
            view.layout(0, 0, display.getWidth(), display.getHeight()); 
            view.setDrawingCacheEnabled(true);//允许当前窗口保存缓存信息，这样getDrawingCache()方法才会返回一个Bitmap  
            Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache());  
     return bmp;  
    }
	
}
